package com.samir.spotifyapi.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.DatabaseHelper;
import com.samir.spotifyapi.classes.InternalAlbums;
import com.samir.spotifyapi.classes.InternalArtists;
import com.samir.spotifyapi.classes.InternalTracks;
import com.samir.spotifyapi.classes.Tracks;
import com.samir.spotifyapi.loader.LoadParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TracksFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private EditText param;
    private TextView tvSearchTracks;
    private ImageView searchTracks;
    private CardView btPesq, locale;
    private String stringParam, country, address;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private FusedLocationProviderClient fusedLocation;
    private ArrayList<Tracks> arrayListTracks = new ArrayList<>();
    public static InternalTracks internalTracks;
    public static InternalArtists internalArtists;
    public static InternalAlbums internalAlbums;
    public static RecyclerView recyclerViewTrackFav;
    public static RecyclerView recyclerViewArtistsFav;
    public static RecyclerView recyclerViewAlbumsFav;
    int savedInstanceVisible;
    DatabaseHelper databaseHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLoaderManager().getLoader(0) != null) {
            getLoaderManager().initLoader(0, null, this);
        }

        fusedLocation = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        databaseHelper = new DatabaseHelper(getActivity());
//        databaseHelper.deleteAll();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        ref(view);

        internalTracks = new InternalTracks(getActivity());
        internalArtists = new InternalArtists(getActivity());
        internalAlbums = new InternalAlbums(getActivity());

        getLocation();

        locale.setOnClickListener(c -> {
            if (address == null) address = "";
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Localização");
            builder.setMessage("Seu endereço: " + address
                    + "\n\nDeseja procurar somente por músicas em seus país?");
            builder.setPositiveButton("Sim", (dialog, which) -> country = getActivity().getResources().getConfiguration().locale.getCountry())
                    .setNegativeButton("Não", (dialog, which) -> country = null);
            builder.create();
            builder.show();
        });

        btPesq.setOnClickListener(this::search);

        param.setOnEditorActionListener((v, actionId, event) -> {

            if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(v);
            }

            return false;
        });

        recyclerConfig();

        return view;
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocation.getLastLocation().addOnCompleteListener(task -> {
                try {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        address = addresses.get(0).getAddressLine(0);
                        Log.i("LOC", addresses.get(0).getAddressLine(0));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    private void recyclerConfig() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        TrackAdapter trackAdapter = new TrackAdapter(arrayListTracks, getActivity());
        recyclerView.setAdapter(trackAdapter);
    }

    private void search(View v) {
        stringParam = param.getText().toString();

        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        if (networkInfo != null && networkInfo.isConnected() && stringParam.length() != 0) {

            pesquisa();

        } else {
            if (stringParam.length() == 0) {
                param.setHint("Informe um nome");
                param.setHintTextColor(getResources().getColor(R.color.hint));
                param.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_baseline_error_outline_24), null, null, null);

            } else {
                Toast.makeText(getContext(), "Verifique sua conexão.", Toast.LENGTH_LONG).show();
            }
        }
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        String queryCountry = "";
        if (args != null) {
            queryString = args.getString("parameter");
            queryCountry = args.getString("country");
        }
        return new LoadParam(getContext(), queryString, queryCountry);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if (arrayListTracks.size() < 20) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject tracksPrincipal = jsonObject.getJSONObject("tracks");
                JSONArray itemsArray = tracksPrincipal.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {
                    Tracks tracks = new Tracks();
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONObject album = book.getJSONObject("album");
                    JSONArray img = album.getJSONArray("images");

                    tracks.setMusicUri(book.getString("uri"));
                    tracks.setMusicName(book.getString("name"));
                    tracks.setId(book.getString("id"));

                    JSONObject externalsUrl = book.getJSONObject("external_urls");
                    tracks.setUrlMusic(externalsUrl.getString("spotify"));

                    JSONObject largeImage = img.getJSONObject(1);
                    tracks.setImgUrl(largeImage.getString("url"));

                    JSONObject smallImage = img.getJSONObject(2);
                    tracks.setImgUrlSmaller(smallImage.getString("url"));

                    JSONArray artistaArray = book.getJSONArray("artists");

                    for (int a = 0; a < artistaArray.length(); a++) {
                        JSONObject book2 = artistaArray.getJSONObject(a);
                        tracks.setArtistName(book2.getString("name"));
                    }

                    databaseHelper.insertTrack(tracks);
                    arrayListTracks.add(tracks);
                }

                progressBar.setVisibility(View.GONE);

                TrackAdapter trackAdapter = new TrackAdapter(arrayListTracks, getActivity());
                recyclerView.setAdapter(trackAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    }

    public void pesquisa() {
        progressBar.setVisibility(View.VISIBLE);
        tvSearchTracks.setVisibility(View.GONE);
        searchTracks.setVisibility(View.GONE);
        if (arrayListTracks != null) arrayListTracks.clear();

        ArrayList<Tracks> listTrackDatabase = databaseHelper.getTracksList(stringParam);

        if (listTrackDatabase.size() >= 20) {
            /*for(int i = 0; i < listTrackDatabase.size(); i++){
                Log.i("DATABASE", listTrackDatabase.get(i).getMusicName());
            }*/

            TrackAdapter trackAdapter = new TrackAdapter(listTrackDatabase, getActivity());
            recyclerView.setAdapter(trackAdapter);
            progressBar.setVisibility(View.GONE);
        } else {
            Bundle queryBundle = new Bundle();
            queryBundle.putString("parameter", stringParam);
            queryBundle.putString("country", country);
            getActivity().getSupportLoaderManager().restartLoader(0, queryBundle, this);
        }
    }

    private void ref(View view) {
        param = view.findViewById(R.id.editText);
        btPesq = view.findViewById(R.id.btnPesqTrack);
        progressBar = view.findViewById(R.id.progressBarTracksRecycler);
        recyclerView = view.findViewById(R.id.recyclerTracks);
        locale = view.findViewById(R.id.localeTracks);
        tvSearchTracks = view.findViewById(R.id.tvSearchTracks);
        searchTracks = view.findViewById(R.id.imageViewSearchTracks);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        savedInstanceVisible = tvSearchTracks.getVisibility();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        tvSearchTracks.setVisibility(savedInstanceVisible);
        searchTracks.setVisibility(savedInstanceVisible);

        if (arrayListTracks.size() > 0) {
            tvSearchTracks.setVisibility(View.GONE);
            searchTracks.setVisibility(View.GONE);
        }
    }
}
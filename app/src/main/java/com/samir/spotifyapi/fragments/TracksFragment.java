package com.samir.spotifyapi.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.Tracks;
import com.samir.spotifyapi.loader.LoadParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TracksFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private EditText param;
    private ImageView pic, btSpot;
    private TextView artName, musicName;
    private Button btPesq;
    private String stringParam;
    private ProgressBar progressBar;
    private String uriSpotify = "";
    private RecyclerView recyclerView;
    private ArrayList<Tracks> arrayListTracks = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getLoaderManager().getLoader(0) != null) {
            getLoaderManager().initLoader(0, null, this);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        ref(view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        TrackAdapter trackAdapter = new TrackAdapter(arrayListTracks);
        recyclerView.setAdapter(trackAdapter);

        progressBar.setVisibility(View.GONE);
        btSpot.setVisibility(View.INVISIBLE);

        btPesq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }else {
                    if (stringParam.length() == 0) {
                        param.setHint("Informe um nome");
                        param.setHintTextColor( getResources().getColor(R.color.hint));
                        param.setCompoundDrawablesRelativeWithIntrinsicBounds(getResources().getDrawable(R.drawable.ic_baseline_error_outline_24),null,null, null);

                    } else {
                        Toast.makeText(getContext(), "Verifique sua conexão.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriSpotify != null) {
                    Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(uriSpotify));
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable Bundle args) {
        String queryString = "";
        if (args != null) {
            queryString = args.getString("parameter");
        }
        return new LoadParam(getContext(), queryString);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject tracksPrincipal = jsonObject.getJSONObject("tracks");
            JSONArray itemsArray = tracksPrincipal.getJSONArray("items");

            String trackName = null;
            String urlImg = null;
            String mscName = null;
            Tracks tracks = new Tracks();

            int i = 0;
            while (i < itemsArray.length() && trackName == null){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject album = book.getJSONObject("album");
                JSONArray img = album.getJSONArray("images");

                uriSpotify = book.getString("uri");
                mscName = book.getString("name");
                tracks.setMusicUri(uriSpotify);
                tracks.setMusicName(mscName);

                int im = 0;
                while (im < img.length() && trackName == null) {
                    JSONObject book2 = img.getJSONObject(1);
                    urlImg  = book2.getString("url");
                    tracks.setImgUrl(urlImg);
                    im++;
                }

                JSONArray artistaArray = book.getJSONArray("artists");

                int a = 0;
                while (a < artistaArray.length() && trackName == null) {
                    JSONObject book2 = artistaArray.getJSONObject(a);
                    trackName  = book2.getString("name");
                    tracks.setArtistName(trackName);
                    a++;
                }
                arrayListTracks.add(tracks);
                recyclerView.getAdapter().notifyDataSetChanged();
                i++;
            }

            if (trackName != null && mscName != null && urlImg != null) {
                progressBar.setVisibility(View.GONE);
                artName.setText(trackName);
                musicName.setText(mscName);

                Uri uriimg = Uri.parse(urlImg);
                Glide.with(this)
                        .load(uriimg)
                        .into(pic);

            } else {
                artName.setText("Nenhum resltado");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void pesquisa(){
        progressBar.setVisibility(View.VISIBLE);
        btSpot.setVisibility(View.VISIBLE);
        Bundle queryBundle = new Bundle();
        queryBundle.putString("parameter", stringParam);
        getActivity().getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    private void ref(View view) {
        param = view.findViewById(R.id.editText);
        pic = view.findViewById(R.id.imageView);
        artName = view.findViewById(R.id.tvNameArtist);
        musicName = view.findViewById(R.id.textViewNameTrack);
        btPesq = view.findViewById(R.id.btnPesqTrack);
        progressBar = view.findViewById(R.id.progressBar);
        btSpot = view.findViewById(R.id.btOpenSpot);
        recyclerView = view.findViewById(R.id.recyclerTracks);
    }

}
package com.samir.spotifyapi.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
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
import com.samir.spotifyapi.adapters.AlbumsAdapter;
import com.samir.spotifyapi.adapters.TrackAdapter;
import com.samir.spotifyapi.classes.Albums;
import com.samir.spotifyapi.classes.Tracks;
import com.samir.spotifyapi.loader.LoadParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AlbumsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private EditText param;
    private String stringParam, country;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private CardView btPesq, locale;
    private TextView tvPesq;
    private ImageView searchAlb;
    private ArrayList<Albums> albumsArrayList = new ArrayList<>();

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
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ref(view);

        progressBar.setVisibility(View.GONE);

        btPesq.setOnClickListener(v -> {
            search(v);
        });

        param.setOnEditorActionListener((v, actionId, event) -> {

            if (event != null && KeyEvent.KEYCODE_ENTER == event.getKeyCode() || actionId == EditorInfo.IME_ACTION_SEARCH) {
                search(v);
            }

            return false;
        });

        locale.setOnClickListener(c -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setCancelable(false);
            builder.setTitle("Localização");
            builder.setMessage("Você está no país: " + getActivity().getResources().getConfiguration().locale.getDisplayCountry()
                    + "\n\nDeseja procurar somente por álbuns em seus país?");
            builder.setPositiveButton("Sim", (dialog, which) -> country = getActivity().getResources().getConfiguration().locale.getCountry())
                    .setNegativeButton("Não", (dialog, which) -> country = null);
            builder.create();
            builder.show();
        });

        recyclerConfig();

        return view;
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

    private void recyclerConfig() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayout.VERTICAL));

        AlbumsAdapter albumsAdapter = new AlbumsAdapter(albumsArrayList, getActivity());
        recyclerView.setAdapter(albumsAdapter);
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
        if (albumsArrayList.size() < 20) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject tracksPrincipal = jsonObject.getJSONObject("albums");
                JSONArray itemsArray = tracksPrincipal.getJSONArray("items");

                for (int i = 0; i < itemsArray.length(); i++) {

                    Albums albums = new Albums();
                    JSONObject book = itemsArray.getJSONObject(i);

                    albums.setAlbumName(book.getString("name"));
                    albums.setTotalTracks(book.getString("total_tracks"));
                    albums.setAlbumUri(book.getString("uri"));
                    albums.setAlbumId(book.getString("id"));

                    JSONArray img = book.getJSONArray("images");

                    JSONObject book2 = img.getJSONObject(1);
                    albums.setUrlImg(book2.getString("url"));
                    JSONObject book3 = img.getJSONObject(2);
                    albums.setUrlImgSmall(book3.getString("url"));

                    JSONArray artArray = book.getJSONArray("artists");
                    JSONObject art = artArray.getJSONObject(0);
                    albums.setAlbumArtName(art.getString("name"));

                    albumsArrayList.add(albums);

                }

                progressBar.setVisibility(View.GONE);

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
        searchAlb.setVisibility(View.GONE);
        tvPesq.setVisibility(View.GONE);
        if (albumsArrayList != null) albumsArrayList.clear();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("parameter", stringParam);
        queryBundle.putString("country", country);
        getActivity().getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    private void ref(View view) {
        param = view.findViewById(R.id.editTextAlbum);
        progressBar = view.findViewById(R.id.progressBarAlbumRecycler);
        btPesq = view.findViewById(R.id.btnPesqAlbum);
        locale = view.findViewById(R.id.localeAlbums);
        recyclerView = view.findViewById(R.id.recyclerAlbums);
        tvPesq = view.findViewById(R.id.tvSearchAlbums);
        searchAlb = view.findViewById(R.id.imageViewSearchAlbums);
    }

}
package com.samir.spotifyapi.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.samir.spotifyapi.R;
import com.samir.spotifyapi.adapters.ArtistAdapter;
import com.samir.spotifyapi.classes.Artists;
import com.samir.spotifyapi.loader.LoadParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String> {
    private EditText param;
    private String stringParam;
    private ProgressBar progressBar;
    private CardView btPesq;
    private ArrayList<Artists> arrayListArtists = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView tvSearchArt;
    private ImageView searchArt;

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
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        ref(view);

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

        ArtistAdapter artistAdapter = new ArtistAdapter(arrayListArtists, getActivity());
        recyclerView.setAdapter(artistAdapter);
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
        if (arrayListArtists.size() < 20) {
            try {
                JSONObject jsonObject = new JSONObject(data);
                JSONObject tracksPrincipal = jsonObject.getJSONObject("artists");
                JSONArray itemsArray = tracksPrincipal.getJSONArray("items");

                ArrayList<String> generosString = new ArrayList<>();

                int i = 0;
                while (i < itemsArray.length()) {
                    Artists artists = new Artists();
                    JSONObject book = itemsArray.getJSONObject(i);
                    JSONArray generos = book.getJSONArray("genres");

                    artists.setArtUri(book.getString("uri"));
                    artists.setArtName(book.getString("name"));
                    artists.setIdArt(book.getString("uri"));

                    JSONObject externalsUrl = book.getJSONObject("external_urls");
                    artists.setArtUrl(externalsUrl.getString("spotify"));

                    JSONArray img = book.getJSONArray("images");

                    JSONObject book2 = img.getJSONObject(1);
                    artists.setUrlImgArt(book2.getString("url"));

                    JSONObject book3 = img.getJSONObject(2);
                    artists.setUrlImgArtSmall(book3.getString("url"));

                    for (int gn = 0; gn < generos.length(); gn++) {
                        generosString.add(generos.get(gn).toString());
                    }

                    String generosFim = generosString.toString();
                    generosFim = generosFim.replace("[", "");
                    generosFim = generosFim.replace("]", "");
                    artists.setGenres(generosFim);

                    arrayListArtists.add(artists);
                    i++;
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
        tvSearchArt.setVisibility(View.GONE);
        searchArt.setVisibility(View.GONE);
        if (arrayListArtists != null) arrayListArtists.clear();
        Bundle queryBundle = new Bundle();
        queryBundle.putString("parameter", stringParam);
        getActivity().getSupportLoaderManager().restartLoader(0, queryBundle, this);
    }

    private void ref(View view) {
        param = view.findViewById(R.id.editTextArt);
        progressBar = view.findViewById(R.id.progressBarArtRecycler);
        btPesq = view.findViewById(R.id.btnPesqArtist);
        recyclerView = view.findViewById(R.id.recyclerArtists);
        tvSearchArt = view.findViewById(R.id.tvSearchArt);
        searchArt = view.findViewById(R.id.imageViewSearchArtists);
    }

}
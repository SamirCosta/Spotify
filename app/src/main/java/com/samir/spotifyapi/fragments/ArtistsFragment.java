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

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.loader.LoadParam;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private EditText param;
    private ImageView pic, btSpot;
    private TextView artName, tvGenero;
    private Button btPesq;
    private String stringParam;
    private ProgressBar progressBar;
    private String uriSpotify = "";

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
        param = view.findViewById(R.id.editTextArt);
        pic = view.findViewById(R.id.imageViewArt);
        artName = view.findViewById(R.id.textViewNameArt);
        btPesq = view.findViewById(R.id.btnPesqArt);
        progressBar = view.findViewById(R.id.progressBarAlbum);
        btSpot = view.findViewById(R.id.btOpenSpotArt);
        tvGenero = view.findViewById(R.id.tvGen);

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
            JSONObject tracksPrincipal = jsonObject.getJSONObject("artists");
            JSONArray itemsArray = tracksPrincipal.getJSONArray("items");

            String trackName = null;
            String urlImg = null;
            ArrayList<String> generosString = new ArrayList<>();

            int i = 0;
            while (i < itemsArray.length() && trackName == null){
                JSONObject book = itemsArray.getJSONObject(i);

                JSONArray generos = book.getJSONArray("genres");
                int gn = 0;
                while (gn < generos.length()){

                    generosString.add(generos.get(gn).toString());
                    gn++;
                }

                JSONArray img = book.getJSONArray("images");

                uriSpotify = book.getString("uri");
                trackName = book.getString("name");

                int im = 0;
                while (im < img.length()) {
                    JSONObject book2 = img.getJSONObject(1);
                    urlImg  = book2.getString("url");
                    im++;
                }

                i++;
            }

            if (trackName != null) {
                progressBar.setVisibility(View.GONE);
                artName.setText(trackName);
                String generosFim = generosString.toString();
                generosFim = generosFim.replace("[","");
                generosFim = generosFim.replace("]","");
                Log.i("ARRAY", generosFim);
                tvGenero.setText("Gêneros: "+generosFim);

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
}
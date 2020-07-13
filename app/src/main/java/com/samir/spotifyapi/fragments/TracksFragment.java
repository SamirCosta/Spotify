package com.samir.spotifyapi.fragments;

import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CursorAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TracksFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TracksFragment extends Fragment implements LoaderManager.LoaderCallbacks<String>{
    private EditText param;
    private ImageView pic, btSpot;
    private TextView artName;
    private Button btPesq;
    private String stringParam;
    private ProgressBar progressBar;
    private String uriSpotify = "";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TracksFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TracksFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TracksFragment newInstance(String param1, String param2) {
        TracksFragment fragment = new TracksFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        if (getLoaderManager().getLoader(0) != null) {
            getLoaderManager().initLoader(0, null, this);
        }



    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tracks, container, false);
        param = view.findViewById(R.id.editText);
        pic = view.findViewById(R.id.imageView);
        artName = view.findViewById(R.id.textViewName);
        btPesq = view.findViewById(R.id.btnPesqTrack);
        progressBar = view.findViewById(R.id.progressBar);
        btSpot = view.findViewById(R.id.btOpenSpot);

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

            int i = 0;
            while (i < itemsArray.length() && trackName == null){
                JSONObject book = itemsArray.getJSONObject(i);
                JSONObject album = book.getJSONObject("album");
                JSONArray img = album.getJSONArray("images");

                uriSpotify = book.getString("uri");

                int im = 0;
                while (im < img.length() && trackName == null) {
                    JSONObject book2 = img.getJSONObject(1);
                    urlImg  = book2.getString("url");
                    //Log.d(LOG_TAG, urlImg);
                    im++;
                }

                JSONArray artistaArray = book.getJSONArray("artists");

                int a = 0;
                while (a < artistaArray.length() && trackName == null) {
                    JSONObject book2 = artistaArray.getJSONObject(a);
                    trackName  = book2.getString("name");

                    a++;
                }
                i++;
            }

            if (trackName != null) {
                progressBar.setVisibility(View.GONE);
                artName.setText(trackName);

                Uri uriimg = Uri.parse(urlImg);
                Glide.with(this)
                        .load(uriimg)
                        .into(pic);

            } else {
                artName.setText("Nenhum resltado");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //Log.d(LOG_TAG, "AQUI");
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
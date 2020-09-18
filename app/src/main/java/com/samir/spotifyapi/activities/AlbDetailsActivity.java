package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;

public class AlbDetailsActivity extends AppCompatActivity {
    private ImageView pic, btSpot;
    private TextView artName, tvNumFaixas;
    private Button btPesq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alb_details);

       /* pic = view.findViewById(R.id.imageViewAlbum);
        artName = view.findViewById(R.id.textViewNameAlbum);
        btPesq = view.findViewById(R.id.btnPesqAlbum);
        btSpot = view.findViewById(R.id.btOpenSpotArt);
        tvNumFaixas = view.findViewById(R.id.tvNumFaixas);*/

        /*if (trackName != null && uriSpotify != null && urlImg != null && totalTracks != null) {
            progressBar.setVisibility(View.GONE);
            artName.setText(trackName);
            tvNumFaixas.setText("Número de faixas: " + totalTracks);

            Uri uriimg = Uri.parse(urlImg);
            Glide.with(this)
                    .load(uriimg)
                    .into(pic);

        } else {
            artName.setText("Nenhum resltado");
        }*/

        /*btSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriSpotify != null) {
                    Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(uriSpotify));
                    startActivity(intent);
                }
            }
        });*/

    }
}
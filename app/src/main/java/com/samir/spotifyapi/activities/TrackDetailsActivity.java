package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.classes.Tracks;

public class TrackDetailsActivity extends AppCompatActivity {
    private ImageView pic, btSpot;
    private TextView artName, musicName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        ref();

        Bundle bundle = getIntent().getExtras();
        Tracks tracks = (Tracks) bundle.getSerializable("object");

        if (tracks != null) {
                artName.setText(tracks.getArtistName());
                musicName.setText(tracks.getMusicName());

                Uri uriimg = Uri.parse(tracks.getImgUrl());
                Glide.with(this)
                        .load(uriimg)
                        .into(pic);

            } else {
                artName.setText("Nenhum resltado");
            }

        btSpot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tracks.getMusicUri() != null) {
                    Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(tracks.getMusicUri()));
                    startActivity(intent);
                }
            }
        });

    }

    private void ref() {
        pic = findViewById(R.id.imageView);
        artName = findViewById(R.id.tvNameArtist);
        musicName = findViewById(R.id.textViewNameTrack);
        btSpot = findViewById(R.id.btOpenSpot);
    }
}
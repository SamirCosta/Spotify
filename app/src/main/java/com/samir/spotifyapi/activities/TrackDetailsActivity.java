package com.samir.spotifyapi.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.classes.Tracks;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.samir.spotifyapi.fragments.TracksFragment.internalTracks;

public class TrackDetailsActivity extends AppCompatActivity {
    private ImageView pic, star, share;
    private CardView btSpot;
    private TextView artName, musicName;
    boolean fav;
    private Tracks tracks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_details);
        ref();

        Toolbar toolbar = findViewById(R.id.toolbarTrack);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        Bundle bundle = getIntent().getExtras();
        tracks = (Tracks) bundle.getSerializable("object");

        for(int i = 0; i < internalTracks.getListInternalTracks().size(); i++){
            Tracks tracksFav = internalTracks.getListInternalTracks().get(i);
            if (tracksFav.getId().equals(tracks.getId())){
                star.setImageResource(R.drawable.ic_baseline_star_24);
                fav = true;
            }
        }

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

        pic.setOnClickListener(a -> {
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("img", tracks.getImgUrl());
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    pic,
                    ViewCompat.getTransitionName(pic));
            startActivity(intent, activityOptionsCompat.toBundle());
        });

        btSpot.setOnClickListener(v -> {
            if (tracks.getMusicUri() != null) {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(tracks.getMusicUri()));
                startActivity(intent);
            }
        });

        star.setOnClickListener(c -> {
            if (fav){
                star.setImageResource(R.drawable.ic_baseline_star_border_24);
                fav = false;
            }else{
                star.setImageResource(R.drawable.ic_baseline_star_24);
                saveInternal();
                fav = true;
            }
        });

        share.setOnClickListener(a -> {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Já ouviu essa música no Spotify?\n" + tracks.getUrlMusic());
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveInternal() {
        try {
            internalTracks.setListInternalTracks(tracks);

            FileOutputStream fos = new FileOutputStream(this.getFileStreamPath("streamFile"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(internalTracks);
            oos.close();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void ref() {
        pic = findViewById(R.id.imageView);
        artName = findViewById(R.id.tvNameArtist);
        musicName = findViewById(R.id.textViewNameTrack);
        btSpot = findViewById(R.id.btOpenSpot);
        star = findViewById(R.id.imageViewStar);
        share = findViewById(R.id.imageViewShare);
    }
}
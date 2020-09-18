package com.samir.spotifyapi.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
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
import com.samir.spotifyapi.classes.Albums;
import com.samir.spotifyapi.classes.InternalAlbums;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.samir.spotifyapi.fragments.TracksFragment.internalAlbums;

public class AlbDetailsActivity extends AppCompatActivity {
    private ImageView pic;
    private CardView btSpot;
    private TextView artName, tvNumFaixas, albumName;
    private Button btPesq;
    private ImageView star, share;
    private Bundle bundle;
    private Albums albums;
    private boolean fav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alb_details);
        ref();

        internalAlbums = new InternalAlbums(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbarAlb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        bundle = getIntent().getExtras();
        albums = (Albums) bundle.getSerializable("album");

        for (int i = 0; i < internalAlbums.getAlbumsArrayList().size(); i++) {
            Albums albumFav = internalAlbums.getAlbumsArrayList().get(i);
            if (albumFav.getAlbumId().equals(albums.getAlbumId())) {
                star.setImageResource(R.drawable.ic_baseline_star_24);
                fav = true;
            }
        }

        if (albums != null) {
            albumName.setText(albums.getAlbumName());
            artName.setText(albums.getAlbumArtName());
            tvNumFaixas.setText(albums.getTotalTracks());

            Uri uriimg = Uri.parse(albums.getUrlImg());
            Glide.with(this)
                    .load(uriimg)
                    .into(pic);

        }

        btSpot.setOnClickListener(v -> {
            if (albums.getAlbumUri() != null) {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(albums.getAlbumUri()));
                startActivity(intent);
            }
        });

        pic.setOnClickListener(a -> {
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("img", albums.getUrlImg());
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    pic,
                    ViewCompat.getTransitionName(pic));
            startActivity(intent, activityOptionsCompat.toBundle());
        });

        share.setOnClickListener(a -> {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Já viu esse álbum no Spotify?\n" + albums.getAlbumUrl());
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
            }
        });

        star.setOnClickListener(c -> {
            if (fav) {
                star.setImageResource(R.drawable.ic_baseline_star_border_24);
                removeInternal();
                fav = false;
            } else {
                star.setImageResource(R.drawable.ic_baseline_star_24);
                saveInternal();
                fav = true;
            }
        });

    }

    private void removeInternal() {
        internalAlbums.removeInternalAlbums(albums);
        save();
    }

    private void saveInternal() {
        internalAlbums.setAlbumsArrayList(albums);
        save();
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream(this.getFileStreamPath("albums"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(internalAlbums);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ref() {
        pic = findViewById(R.id.imageViewAlbum);
        artName = findViewById(R.id.tvNameArtistAlb);
        albumName = findViewById(R.id.textViewNameAlbum);
        tvNumFaixas = findViewById(R.id.tvNumFaixas);
        btSpot = findViewById(R.id.btOpenSpotAlb);
        star = findViewById(R.id.imageViewStarAlb);
        share = findViewById(R.id.imageViewShareAlb);
    }
}
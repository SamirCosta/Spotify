package com.samir.spotifyapi.activities;

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
import com.samir.spotifyapi.classes.Artists;
import com.samir.spotifyapi.classes.InternalArtists;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import static com.samir.spotifyapi.fragments.TracksFragment.internalArtists;

public class ArtDetailsActivity extends AppCompatActivity {
    private ImageView pic, star, share;
    private CardView btSpot;
    private TextView artName, gen;
    boolean fav;
    private Artists artists;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_art_details);
        ref();

        internalArtists = new InternalArtists(getApplicationContext());

        Toolbar toolbar = findViewById(R.id.toolbarArt);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        bundle = getIntent().getExtras();
        artists = (Artists) bundle.getSerializable("artist");

        for(int i = 0; i < internalArtists.getArtistsArrayList().size(); i++){
            Artists artistsFav = internalArtists.getArtistsArrayList().get(i);
            if (artistsFav.getIdArt().equals(artists.getIdArt())){
                star.setImageResource(R.drawable.ic_baseline_star_24);
                fav = true;
            }
        }

        if (artists != null) {
            artName.setText(artists.getArtName());
            gen.setText(artists.getGenres());

            Uri uriimg = Uri.parse(artists.getUrlImgArt());
            Glide.with(this)
                    .load(uriimg)
                    .into(pic);

        } else {
            artName.setText("Nenhum resltado");
        }

        share.setOnClickListener(a -> {
            Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
            whatsappIntent.setType("text/plain");
            whatsappIntent.setPackage("com.whatsapp");
            whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Voçê conhece esse artista?\n" + artists.getArtUrl());
            try {
                startActivity(whatsappIntent);
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(this, "WhatsApp não instalado", Toast.LENGTH_SHORT).show();
            }
        });

        btSpot.setOnClickListener(v -> {
            if (artists.getArtUri() != null) {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, Uri.parse(artists.getArtUri()));
                startActivity(intent);
            }
        });

        pic.setOnClickListener(a -> {
            Intent intent = new Intent(this, ZoomActivity.class);
            intent.putExtra("img", artists.getUrlImgArt());
            ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this,
                    pic,
                    ViewCompat.getTransitionName(pic));
            startActivity(intent, activityOptionsCompat.toBundle());
        });

        star.setOnClickListener(c -> {
            if (fav){
                star.setImageResource(R.drawable.ic_baseline_star_border_24);
                removeInternal();
                fav = false;
            }else{
                star.setImageResource(R.drawable.ic_baseline_star_24);
                saveInternal();
                fav = true;
            }
        });

    }

    private void removeInternal() {
//        internalArtists.removeInternalArtists(bundle.getInt("position"));
        internalArtists.removeInternalArtists(artists);
        save();
    }

    private void saveInternal() {
        internalArtists.setArtistsArrayList(artists);
        save();
    }

    private void save() {
        try {
            FileOutputStream fos = new FileOutputStream(this.getFileStreamPath("favArts"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(internalArtists);
            oos.close();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ref() {
        pic = findViewById(R.id.imageViewArt);
        star = findViewById(R.id.imageViewStarArt);
        share = findViewById(R.id.imageViewShareArt);
        btSpot = findViewById(R.id.btOpenSpotArt);
        artName = findViewById(R.id.textViewNameArt);
        gen = findViewById(R.id.tvGen);
    }
}
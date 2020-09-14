package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;

public class ZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);

        Bundle bundle = getIntent().getExtras();

        ImageView imageView = findViewById(R.id.imageViewZoom);
        Uri uriimg = Uri.parse(bundle.getString("img"));
        Glide.with(this)
                .load(uriimg)
                .into(imageView);

    }
}
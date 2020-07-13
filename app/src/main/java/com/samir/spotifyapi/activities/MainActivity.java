package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.samir.spotifyapi.R;

public class MainActivity extends AppCompatActivity {
    private int fragment;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openTracks(View view){
        fragment = 0;
        intent = new Intent(this, SearchActivity.class);
        intent.putExtra("fragment", fragment);
        startActivity(intent);
    }

    public void openArtists(View view){
        fragment = 1;
        intent = new Intent(this, SearchActivity.class);
        intent.putExtra("fragment", fragment);
        startActivity(intent);
    }

    public void openAlbums(View view){
        fragment = 2;
        intent = new Intent(this, SearchActivity.class);
        intent.putExtra("fragment", fragment);
        startActivity(intent);
    }

}
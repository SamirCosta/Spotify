package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.fragments.AlbumsFavFragment;
import com.samir.spotifyapi.fragments.AlbumsFragment;
import com.samir.spotifyapi.fragments.ArtistsFavFragment;
import com.samir.spotifyapi.fragments.ArtistsFragment;
import com.samir.spotifyapi.fragments.TracksFavFragment;
import com.samir.spotifyapi.fragments.TracksFragment;

public class FavsActivity extends AppCompatActivity {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favs);
        smartTabLayout = findViewById(R.id.viewPagerTabFav);
        viewPager = findViewById(R.id.viewPagerFav);

        setSupportActionBar(findViewById(R.id.toolbarFav));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("Tracks", TracksFavFragment.class)
                        .add("Artists", ArtistsFavFragment.class)
                        .add("Albums", AlbumsFavFragment.class)
                        .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

    }
}
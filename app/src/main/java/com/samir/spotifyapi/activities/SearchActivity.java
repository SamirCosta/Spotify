package com.samir.spotifyapi.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.samir.spotifyapi.R;
import com.samir.spotifyapi.fragments.AlbumsFragment;
import com.samir.spotifyapi.fragments.ArtistsFragment;
import com.samir.spotifyapi.fragments.TracksFragment;

import static com.samir.spotifyapi.activities.LoginActivity.ARQUIVO_LOGIN;

public class SearchActivity extends AppCompatActivity {
    private SmartTabLayout smartTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        smartTabLayout = findViewById(R.id.viewPagerTab);
        viewPager = findViewById(R.id.viewPager);

        setSupportActionBar(findViewById(R.id.toolbar));

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                FragmentPagerItems.with(this)
                .add("Tracks", TracksFragment.class)
                .add("Artists", ArtistsFragment.class)
                .add("Albums", AlbumsFragment.class)
                .create()
        );

        viewPager.setAdapter(adapter);
        smartTabLayout.setViewPager(viewPager);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            viewPager.setCurrentItem(bundle.getInt("shortcut"));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_fav:
                startActivity(new Intent(SearchActivity.this, FavsActivity.class));
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
                break;
            case R.id.menu_sair:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        SharedPreferences pref = getSharedPreferences(ARQUIVO_LOGIN, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.clear();
        editor.commit();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}
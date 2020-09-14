package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.SearchView;

import com.google.android.material.textfield.TextInputEditText;
import com.samir.spotifyapi.R;

public class LoginActivity extends AppCompatActivity {
    public static final String ARQUIVO_LOGIN = "ArqLogin";
    private CardView signin;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private final String KEY_TRACKS = "com.samir.spotifyapi.SHORT_TRACK";
    private final String KEY_ARTISTS = "com.samir.spotifyapi.SHORT_ARTISTS";
    private final String KEY_ALBUMS = "com.samir.spotifyapi.SHORT_ALBUMS";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signin = findViewById(R.id.loginSignin);

        pref = getSharedPreferences(ARQUIVO_LOGIN, 0);
        editor = pref.edit();

        if (pref.contains("user")) {
            openMain();
            findShortcut();
        }

        signin.setOnClickListener(c -> {
            sharedConfig();
        });

    }

    private void openMain() {
        startActivity(new Intent(this, SearchActivity.class));
        finish();
    }

    private void sharedConfig() {
        String usuOK = "a";
        String senOK = "a";

        TextInputEditText editUser = findViewById(R.id.editUser);
        TextInputEditText editPass = findViewById(R.id.editPassword);
        String user = editUser.getText().toString();
        String pass = editPass.getText().toString();
        if (user.equals(usuOK) && pass.equals(senOK)) {
            editor.putString("user", user);
            editor.putString("password", pass);
            editor.commit();
            openMain();
        }
    }

    private void findShortcut() {
        if(getIntent().getAction() == KEY_TRACKS){
            openMainWhithShortcuts(0);
        }

        if(getIntent().getAction() == KEY_ARTISTS){
            openMainWhithShortcuts(1);
        }

        if(getIntent().getAction() == KEY_ALBUMS){
            openMainWhithShortcuts(2);
        }
    }

    private void openMainWhithShortcuts(int i) {
        Intent intent = new Intent(this, SearchActivity.class);
        intent.putExtra("shortcut", i);
        startActivity(intent);
        finish();
    }

}
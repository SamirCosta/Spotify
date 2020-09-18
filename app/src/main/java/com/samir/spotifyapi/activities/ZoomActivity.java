package com.samir.spotifyapi.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.samir.spotifyapi.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ZoomActivity extends AppCompatActivity{
    private CardView btDownload;

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

        btDownload = findViewById(R.id.btDownload);
        btDownload.setOnClickListener(c -> {
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);
            }

            createDir(bundle.getString("img"));
        });

    }

    public void createDir(String img) {
            File direct = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + System.currentTimeMillis());

            if (!direct.exists()) direct.mkdir();

            DownloadManager dm = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(img);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("" + System.currentTimeMillis())
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                             File.separator + "/Spotify/" + System.currentTimeMillis());

            assert dm != null;
            dm.enqueue(request);
            Toast.makeText(this, "Downloaded", Toast.LENGTH_SHORT).show();

    }
}
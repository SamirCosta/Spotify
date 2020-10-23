package com.samir.spotifyapi.classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SpotifyDatabase";
    private static final String ID = "id";
    private static final String MUSIC_NAME = "musicName";
    private static final String ARTIST_NAME = "artistName";
    private static final String IMG_URL = "imgUrl";
    private static final String IMG_URL_SMALLER = "imgUrlSmaller";
    private static final String MUSIC_URI = "musicUri";
    private static final String URL_MUSIC = "urlMusic";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(
                "create table TRACKS " +
                        "(id text primary key, musicName text, artistName text," +
                        " imgUrl text, imgUrlSmaller text, musicUri text, urlMusic text)"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS TRACKS");
        onCreate(db);
    }

    public void insertTrack (Tracks tracks) {
        boolean contains = true;
        ArrayList<String> ids = this.getIds();
        for (int i = 0; i < ids.size(); i++){
            if (ids.get(i).equals(tracks.getId())) contains = false;
        }
        if (contains){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ID, tracks.getId());
            contentValues.put(MUSIC_NAME, tracks.getMusicName());
            contentValues.put(ARTIST_NAME, tracks.getArtistName());
            contentValues.put(IMG_URL, tracks.getImgUrl());
            contentValues.put(IMG_URL_SMALLER, tracks.getImgUrlSmaller());
            contentValues.put(MUSIC_URI, tracks.getMusicUri());
            contentValues.put(URL_MUSIC, tracks.getUrlMusic());
            db.insert("TRACKS", null, contentValues);
            Log.i("AQUI", "INSERIU");
        }
    }

    public ArrayList<Tracks> getTracksList(String name) {
        ArrayList<Tracks> lista = new ArrayList<>() ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from TRACKS where musicName like '" + name + "%'", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            Tracks tracks = new Tracks();
            tracks.setId(res.getString(res.getColumnIndex(ID)));
            tracks.setMusicName(res.getString(res.getColumnIndex(MUSIC_NAME)));
            tracks.setArtistName(res.getString(res.getColumnIndex(ARTIST_NAME)));
            tracks.setImgUrl(res.getString(res.getColumnIndex(IMG_URL)));
            tracks.setImgUrlSmaller(res.getString(res.getColumnIndex(IMG_URL_SMALLER)));
            tracks.setMusicUri(res.getString(res.getColumnIndex(MUSIC_URI)));
            tracks.setUrlMusic(res.getString(res.getColumnIndex(URL_MUSIC)));

            lista.add(tracks);
            res.moveToNext();
        }

        return lista;
    }

    public ArrayList<String> getIds() {
        ArrayList<String> array_list = new ArrayList<String>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select id from TRACKS", null );
        res.moveToFirst();

        while(res.isAfterLast() == false){
            array_list.add(res.getString(res.getColumnIndex(ID)));
            res.moveToNext();
        }

        return array_list;
    }

    public Integer deleteAll () {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("TRACKS",
                null,
                null);
    }

}

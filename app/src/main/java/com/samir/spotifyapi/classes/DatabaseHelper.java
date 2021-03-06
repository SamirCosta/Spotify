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

    private static final String COLUMN_TRACK = "TRACKS";
    private static final String ID = "id";
    private static final String MUSIC_NAME = "musicName";
    private static final String ARTIST_NAME = "artistName";
    private static final String IMG_URL = "imgUrl";
    private static final String IMG_URL_SMALLER = "imgUrlSmaller";
    private static final String MUSIC_URI = "musicUri";
    private static final String URL_MUSIC = "urlMusic";

    private static final String COLUMN_ARTISTS = "ARTISTS";
    private static final String ART_ID = "idArt";
    private static final String ART_NAME = "artName";
    private static final String GENRES = "genres";
    private static final String URL_IMG_ART = "urlImgArt";
    private static final String URL_IMG_ART_SMALL = "urlImgArtSmall";
    private static final String ART_URI = "artUri";
    private static final String ART_URL = "artUrl";

    private static final String COLUMN_ALBUMS = "ALBUMS";
    private static final String ALB_ID = "albumId";
    private static final String ALB_NAME = "albumName";
    private static final String ALB_ART_NAME = "albumArtName";
    private static final String TOTAL_TRACKS = "totalTracks";
    private static final String ALB_URL_IMG_SMALL = "urlImgSmall";
    private static final String ALB_URL_IMG = "urlImg";
    private static final String ALB_URI = "albumUri";
    private static final String ALB_URL = "albumUrl";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL( String.format(
                "create table %s" +
                        "(%s text primary key, %s text, %s text, %s text, %s text, %s text, %s text)",
                COLUMN_TRACK,
                ID,
                MUSIC_NAME,
                ARTIST_NAME,
                IMG_URL,
                IMG_URL_SMALLER,
                MUSIC_URI,
                URL_MUSIC)
        );

        db.execSQL( String.format(
                "create table %s " +
                        "(%s text primary key, %s text, %s text, %s text, %s text, %s text, %s text)",
                COLUMN_ARTISTS,
                ART_ID,
                ART_NAME,
                GENRES,
                URL_IMG_ART,
                URL_IMG_ART_SMALL,
                ART_URI,
                ART_URL)
        );

        db.execSQL( String.format(
                "create table %s " +
                        "(%s text primary key, %s text, %s text, %s text, %s text, %s text, %s text, %s text)",
                COLUMN_ALBUMS,
                ALB_ID,
                ALB_NAME,
                ALB_ART_NAME,
                TOTAL_TRACKS,
                ALB_URL_IMG_SMALL,
                ALB_URL_IMG,
                ALB_URI,
                ALB_URL)
        );

//        Log.i("AQUI", "CRIOU");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + COLUMN_TRACK);
        db.execSQL("DROP TABLE IF EXISTS " + COLUMN_ARTISTS);
        db.execSQL("DROP TABLE IF EXISTS " + COLUMN_ALBUMS);
        onCreate(db);
    }

    public void insertTrack (Tracks tracks) {
        boolean contains = true;
        ArrayList<String> ids = this.getIds();
        for (String id: ids){
            if (id.equals(tracks.getId())) contains = false;
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
            db.insert(COLUMN_TRACK, null, contentValues);
//            Log.i("AQUI", "INSERIU");
        }
    }

    public ArrayList<Tracks> getTracksList(String name) {
        ArrayList<Tracks> lista = new ArrayList<>() ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from TRACKS where musicName like '" + name + "%'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
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
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select %s from %s", ID, COLUMN_TRACK), null );
        res.moveToFirst();

        while(!res.isAfterLast()){
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

    public void insertArtists (Artists artists) {
        boolean contains = true;
        ArrayList<String> ids = this.getArtIds();
        for (String id: ids){
            if (id.equals(artists.getIdArt())) contains = false;
        }
        if (contains){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ART_ID, artists.getIdArt());
            contentValues.put(ART_NAME, artists.getArtName());
            contentValues.put(GENRES, artists.getGenres());
            contentValues.put(URL_IMG_ART, artists.getUrlImgArt());
            contentValues.put(URL_IMG_ART_SMALL, artists.getUrlImgArtSmall());
            contentValues.put(ART_URI, artists.getArtUri());
            contentValues.put(ART_URL, artists.getArtUrl());
            db.insert(COLUMN_ARTISTS, null, contentValues);
//            Log.i("AQUI", "INSERIU");
        }

    }

    public ArrayList<Artists> getArtistsList(String name) {
        ArrayList<Artists> lista = new ArrayList<>() ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ARTISTS where artName like '" + name + "%'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Artists artists = new Artists();
            artists.setIdArt(res.getString(res.getColumnIndex(ART_ID)));
            artists.setArtName(res.getString(res.getColumnIndex(ART_NAME)));
            artists.setGenres(res.getString(res.getColumnIndex(GENRES)));
            artists.setUrlImgArt(res.getString(res.getColumnIndex(URL_IMG_ART)));
            artists.setUrlImgArtSmall(res.getString(res.getColumnIndex(URL_IMG_ART_SMALL)));
            artists.setArtUri(res.getString(res.getColumnIndex(ART_URI)));
            artists.setArtUrl(res.getString(res.getColumnIndex(ART_URL)));

            lista.add(artists);
            res.moveToNext();
        }

        return lista;
    }

    public ArrayList<String> getArtIds() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select %s from %s", ART_ID, COLUMN_ARTISTS), null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(ART_ID)));
            res.moveToNext();
        }

        return array_list;
    }

    public void insertAlbums (Albums albums) {
        boolean contains = true;
        ArrayList<String> ids = this.getAlbIds();
        for (String id: ids){
            if (id.equals(albums.getAlbumId())) contains = false;
        }

        if (contains){
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ALB_ID, albums.getAlbumId());
            contentValues.put(ALB_NAME, albums.getAlbumName());
            contentValues.put(ALB_ART_NAME, albums.getAlbumArtName());
            contentValues.put(TOTAL_TRACKS, albums.getTotalTracks());
            contentValues.put(ALB_URL_IMG_SMALL, albums.getUrlImgSmall());
            contentValues.put(ALB_URL_IMG, albums.getUrlImg());
            contentValues.put(ALB_URI, albums.getAlbumUri());
            contentValues.put(ALB_URL, albums.getAlbumUrl());
            db.insert(COLUMN_ALBUMS, null, contentValues);
//            Log.i("AQUI", "INSERIU");
        }

    }

    public ArrayList<Albums> getAlbumsList(String name) {
        ArrayList<Albums> lista = new ArrayList<>() ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from ALBUMS where albumName like '" + name + "%'", null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            Albums albums = new Albums();
            albums.setAlbumId(res.getString(res.getColumnIndex(ALB_ID)));
            albums.setAlbumName(res.getString(res.getColumnIndex(ALB_NAME)));
            albums.setAlbumArtName(res.getString(res.getColumnIndex(ALB_ART_NAME)));
            albums.setTotalTracks(res.getString(res.getColumnIndex(TOTAL_TRACKS)));
            albums.setUrlImgSmall(res.getString(res.getColumnIndex(ALB_URL_IMG_SMALL)));
            albums.setUrlImg(res.getString(res.getColumnIndex(ALB_URL_IMG)));
            albums.setAlbumUri(res.getString(res.getColumnIndex(ALB_URI)));
            albums.setAlbumUrl(res.getString(res.getColumnIndex(ALB_URL)));

            lista.add(albums);
            res.moveToNext();
        }

        return lista;
    }

    public ArrayList<String> getAlbIds() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( String.format("select %s from %s", ALB_ID, COLUMN_ALBUMS), null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(ALB_ID)));
            res.moveToNext();
        }

        return array_list;
    }

}
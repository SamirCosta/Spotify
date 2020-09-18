package com.samir.spotifyapi.classes;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class InternalArtists implements Serializable {

    ArrayList<Artists> artistsArrayList;

    public InternalArtists(Context context) {
        this.artistsArrayList = getArrayArtists(context);
    }

    public ArrayList<Artists> getArtistsArrayList() {
        return artistsArrayList;
    }

    public void setArtistsArrayList(Artists artists) {
        this.artistsArrayList.add(artists);
    }

    public void removeInternalArtists(Artists artists) {
        for (int i = 0; i < artistsArrayList.size(); i++) {
            Artists artTemp = artistsArrayList.get(i);
            if (artists.getIdArt().equals(artTemp.getIdArt())) {
                this.artistsArrayList.remove(artistsArrayList.indexOf(artTemp));
                Log.i("ARRAY1", "AQUI: " + artistsArrayList.size());
            }
        }
    }

    public static ArrayList<Artists> getArrayArtists(Context context) {
        ArrayList<Artists> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(context.getFileStreamPath("favArts"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            InternalArtists internalArtists = (InternalArtists) ois.readObject();
            list = internalArtists.getArtistsArrayList();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}

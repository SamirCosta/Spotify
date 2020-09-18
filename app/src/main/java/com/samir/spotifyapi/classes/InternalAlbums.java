package com.samir.spotifyapi.classes;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class InternalAlbums implements Serializable {

    ArrayList<Albums> albumsArrayList;

    public InternalAlbums(Context context) {
        this.albumsArrayList = getArrayAlbums(context);
    }

    public ArrayList<Albums> getAlbumsArrayList() {
        return albumsArrayList;
    }

    public void setAlbumsArrayList(Albums albums) {
        this.albumsArrayList.add(albums);
    }

    public void removeInternalAlbums(Albums albums) {
        for (int i = 0; i < albumsArrayList.size(); i++) {
            Albums albTemp = albumsArrayList.get(i);
            if (albTemp.getAlbumId().equals(albums.getAlbumId())) {
                this.albumsArrayList.remove(albumsArrayList.indexOf(albTemp));
            }
        }
    }

    public static ArrayList<Albums> getArrayAlbums(Context context) {
        ArrayList<Albums> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(context.getFileStreamPath("albums"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            InternalAlbums internalAlbums = (InternalAlbums) ois.readObject();
            list = internalAlbums.getAlbumsArrayList();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

}

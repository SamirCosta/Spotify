package com.samir.spotifyapi.classes;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class InternalTracks implements Serializable {

    ArrayList<Tracks> listInternalTracks = new ArrayList<>();

    public InternalTracks(Context context) {
        this.listInternalTracks = getArrayTracks(context);
    }

    public ArrayList<Tracks> getListInternalTracks() {
        return listInternalTracks;
    }

    public void setListInternalTracks(Tracks internalTracks) {
        this.listInternalTracks.add(internalTracks);
    }

    public static ArrayList<Tracks> getArrayTracks(Context context) {
        ArrayList<Tracks> list = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(context.getFileStreamPath("streamFile"));
            ObjectInputStream ois = new ObjectInputStream(fis);
            InternalTracks internalTracks = (InternalTracks) ois.readObject();
            list = internalTracks.getListInternalTracks();

            ois.close();
            fis.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return list;
    }

    /*private void saveInternal() {

        File file = new File(this.getFilesDir(), FILENAME);
        FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        fos.write("asd".getBytes());
        fos.close();
        try {
            ArrayList<Tracks> listPhotos = new ArrayList<>();
            listPhotos.add(photo);
            internalTracks.setListInternalTracks(tracks);

            FileOutputStream fos = new FileOutputStream(this.getFileStreamPath("streamFile"));
            ObjectOutputStream oos = new ObjectOutputStream(fos);

            oos.writeObject(internalTracks);
            oos.close();
            fos.close();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }*/

}

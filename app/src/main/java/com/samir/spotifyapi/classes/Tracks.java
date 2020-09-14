package com.samir.spotifyapi.classes;

import java.io.Serializable;

public class Tracks implements Serializable {

    String musicName;
    String artistName;
    String imgUrl;
    String imgUrlSmaller;
    String musicUri;
    String urlMusic;
    String id;

    public String getImgUrlSmaller() {
        return imgUrlSmaller;
    }

    public void setImgUrlSmaller(String imgUrlSmaller) {
        this.imgUrlSmaller = imgUrlSmaller;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlMusic() {
        return urlMusic;
    }

    public void setUrlMusic(String urlMusic) {
        this.urlMusic = urlMusic;
    }

    public String getMusicUri() {
        return musicUri;
    }

    public void setMusicUri(String musicUri) {
        this.musicUri = musicUri;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}

package com.samir.spotifyapi.classes;

import java.io.Serializable;

public class Albums implements Serializable {

    String albumId;
    String albumName;
    String albumArtName;
    String totalTracks;
    String urlImgSmall;
    String urlImg;
    String albumUri;
    String albumUrl;

    public String getAlbumUrl() {
        return albumUrl;
    }

    public void setAlbumUrl(String albumUrl) {
        this.albumUrl = albumUrl;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumId() {
        return albumId;
    }

    public void setAlbumId(String albumId) {
        this.albumId = albumId;
    }

    public String getAlbumArtName() {
        return albumArtName;
    }

    public void setAlbumArtName(String albumArtName) {
        this.albumArtName = albumArtName;
    }

    public String getTotalTracks() {
        return totalTracks;
    }

    public void setTotalTracks(String totalTracks) {
        this.totalTracks = totalTracks;
    }

    public String getUrlImgSmall() {
        return urlImgSmall;
    }

    public void setUrlImgSmall(String urlImgSmall) {
        this.urlImgSmall = urlImgSmall;
    }

    public String getUrlImg() {
        return urlImg;
    }

    public void setUrlImg(String urlImg) {
        this.urlImg = urlImg;
    }

    public String getAlbumUri() {
        return albumUri;
    }

    public void setAlbumUri(String albumUri) {
        this.albumUri = albumUri;
    }
}

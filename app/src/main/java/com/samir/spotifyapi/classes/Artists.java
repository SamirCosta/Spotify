package com.samir.spotifyapi.classes;

import java.io.Serializable;

public class Artists implements Serializable {

    String idArt;
    String artName;
    String genres;
    String urlImgArt;
    String urlImgArtSmall;
    String artUri;
    String artUrl;

    public String getArtUrl() {
        return artUrl;
    }

    public void setArtUrl(String artUrl) {
        this.artUrl = artUrl;
    }

    public String getIdArt() {
        return idArt;
    }

    public void setIdArt(String idArt) {
        this.idArt = idArt;
    }

    public String getArtName() {
        return artName;
    }

    public void setArtName(String artName) {
        this.artName = artName;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getUrlImgArt() {
        return urlImgArt;
    }

    public void setUrlImgArt(String urlImgArt) {
        this.urlImgArt = urlImgArt;
    }

    public String getUrlImgArtSmall() {
        return urlImgArtSmall;
    }

    public void setUrlImgArtSmall(String urlImgArtSmall) {
        this.urlImgArtSmall = urlImgArtSmall;
    }

    public String getArtUri() {
        return artUri;
    }

    public void setArtUri(String artUri) {
        this.artUri = artUri;
    }
}

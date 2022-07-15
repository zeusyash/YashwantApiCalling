package com.app.apicalling.Model;

public class Artworks {
    String title, artist_display;
    int fiscal_year, artist_id;

    public Artworks(String title, String artist_display, int fiscal_year, int artist_id) {
        this.title = title;
        this.artist_display = artist_display;
        this.fiscal_year = fiscal_year;
        this.artist_id = artist_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist_display() {
        return artist_display;
    }

    public void setArtist_display(String artist_display) {
        this.artist_display = artist_display;
    }

    public int getFiscal_year() {
        return fiscal_year;
    }

    public void setFiscal_year(int fiscal_year) {
        this.fiscal_year = fiscal_year;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }
}

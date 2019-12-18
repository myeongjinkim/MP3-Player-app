package com.example.hw_4r.ui.home;


public class HomeModel{

    private String title;
    private String artist;
    private String lyrics;

    public HomeModel(){

    }
    public HomeModel(String title, String artist, String lyrics){
        this.title=title;
        this.artist = artist;
        this.lyrics = lyrics;
    }
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
    public String getLyrics(){
        return lyrics;
    }



}

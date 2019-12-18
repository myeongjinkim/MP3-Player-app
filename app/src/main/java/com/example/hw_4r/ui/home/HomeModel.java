package com.example.hw_4r.ui.home;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

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

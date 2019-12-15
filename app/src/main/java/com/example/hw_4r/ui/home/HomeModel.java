package com.example.hw_4r.ui.home;

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
    private String lyrics;
    private String title;
    private String artist;
    private String name;
    private StringBuilder path;
    private File fs;

    private int music_num;
    private int file_num;
    private int file_size;
    private int now;
    private  DatabaseReference myRef;
    public HomeModel(){
        file_size=0;
        file_num=0;
        music_num=0;
        path= new StringBuilder();
        path.append("/data/data/com.example.hw_4r/music/");


        myRef = FirebaseDatabase.getInstance().getReference();
        fs = new File(path.toString());

    }


}

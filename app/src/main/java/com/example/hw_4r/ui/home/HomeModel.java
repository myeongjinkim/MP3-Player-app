package com.example.hw_4r.ui.home;

import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeModel{
    private String lyrics;
    private String title;
    private String artist;
    private StringBuilder path;
    private File fs;
    private HashMap<Object, String> hm;
    private int num;
    public HomeModel(){
    }
    public void Firebase(){

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("music_list");
        hm = new HashMap<>();
        num=0;
        path= new StringBuilder();
        path.append("/data/data/com.example.hw_4r/music/");
        fs = new File(path.toString());
        if(fs.isDirectory()){
            String decoding = "ISO-8859-1";
            String encoding = "EUC-KR";

            File list[] = fs.listFiles();
            for(File f : list){
                num++;
                hm.put(Integer.toString(num), f.toString());
                myRef.setValue(hm);
            }
        }
        else {
        }


    }
}

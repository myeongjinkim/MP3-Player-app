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
    private HashMap<Object, String> hm;
    private int music_num;
    private int file_num;
    private int file_size;
    private int now;
    private FirebaseDatabase database;
    private  DatabaseReference myRef;
    public HomeModel(){
        file_size=0;
        file_num=0;
        music_num=0;
        path= new StringBuilder();
        path.append("/data/data/com.example.hw_4r/music/");
        hm = new HashMap<>();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
    }
    public void check(){

        fs = new File(path.toString());
        File[] list = fs.listFiles();
        file_num= list.length;
        if(fs.isDirectory()){
            String decoding = "ISO-8859-1";
            String encoding = "EUC-KR";
            for(File f : list){
                file_size+=f.length();
            }
        }

        System.out.println(file_num);
        System.out.println(file_size);

        myRef.child("file").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        hm = (HashMap<Object, String>) (dataSnapshot.getValue());
                        if(hm==null){
                            hm = new HashMap<>();
                            hm.put(Integer.toString(file_num),Integer.toString(file_size));
                            myRef.child("file").setValue(hm);
                            Firebase();
                        }else{
                            for(Object key : hm.keySet()){

                                String value = hm.get(key);

                                if(key.equals(Integer.toString(file_num))&&value.equals(Integer.toString(file_size))){
                                }else{
                                    Firebase();
                                }
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });





    }
    public void Firebase(){
        hm = new HashMap<>();
        fs = new File(path.toString());
        if(fs.isDirectory()){
            String decoding = "ISO-8859-1";
            String encoding = "EUC-KR";

            File list[] = fs.listFiles();
            for(File f : list){
                music_num++;
                hm.put(Integer.toString(music_num), f.toString());
                myRef.child("music_list").setValue(hm);
                myRef.child("current").setValue(1);
            }
        }
        else {
        }

    }
    public String nowPlayMusic(){

        myRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Get user value
                        now = ((Long)dataSnapshot.child("current").getValue()).intValue();

                        for (DataSnapshot postSnapshot: dataSnapshot.child("music_list").getChildren()) {
                            if(Integer.parseInt(postSnapshot.getKey())==now){
                                name= (String) postSnapshot.getValue();
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        return name;
    }
}

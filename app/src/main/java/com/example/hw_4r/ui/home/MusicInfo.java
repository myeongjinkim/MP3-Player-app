package com.example.hw_4r.ui.home;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class MusicInfo {
    private Long num;
    private String name;
    public MusicInfo(){
        // Default constructor required for calls to DataSnapshot.getValue(FirebasePost.class)
    }

    public MusicInfo(Long num, String name) {
        this.num= num;
        this.name = name;
    }

    @Exclude
    public Map<Object,String> toMap() {
        HashMap<Object, String> result = new HashMap<>();
        result.put("num", Long.toString(num));
        result.put("name", name);
        return result;
    }
    public String getname(){
        return name;
    }
}

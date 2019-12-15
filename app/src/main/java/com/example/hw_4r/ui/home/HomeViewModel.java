package com.example.hw_4r.ui.home;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.HashMap;
import java.util.Queue;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> lyrics;
    private Boolean check;

    private File fs;
    private String path;

    private HashMap<Object, String> hm;
    private DatabaseReference myRef;
    private int music_num;
    private int file_num;
    private int file_size;
    private int now;

    private Callback callback;

    public HomeViewModel() {
        lyrics = new MutableLiveData<>();
        lyrics.setValue("This is home fragment");
        check = true;

        path=("/data/data/com.example.hw_4r/music/");
        fs = new File(path);
        hm = new HashMap<>();
        myRef = FirebaseDatabase.getInstance().getReference();
        callback = null;
    }


    public Boolean getCheck(){
        return check;
    }
    public void setCheck(){
        if(check){
            check = false;
        }else{
            check=true;
        }
    }
    public LiveData<String> getLyrics(){
        return lyrics;
    }
    public void LyricsSetting(String text){
        //가사 가져오는 부분
        lyrics.setValue(text);
    }
    public String getPath(){
        return path.toString();
    }
    public void setAlbum(){ } // 앨범자켓 세팅하는 부분
    private void albumSetting(){ }//앨범자켓 가져오는 부분


    public void check(){
        File[] list = fs.listFiles();
        file_num= list.length;
        if(fs.isDirectory()){
            System.out.println("디렉토리가 맞음");
            for(File f : list){
                file_size+=f.length();
            }
            System.out.println(file_num);
            System.out.println(file_size);

            String s = myRef.child("file").getKey();


            System.out.println("뭔데"+s);
            System.out.println(myRef.child("file").equals(hm));
            System.out.println("뭔데"+myRef.child("file").equals(hm));
            hm.put(Integer.toString(file_num),Integer.toString(file_size));
            System.out.println("뭔데"+myRef.child("file").equals(hm));
            myRef.child("file").addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get user value
                            System.out.println("d언제되는지");
                            hm = (HashMap<Object, String>) (dataSnapshot.getValue());
                            if(hm==null){
                                System.out.println("디비에 없음 완전 새로");
                                hm = new HashMap<>();
                                hm.put(Integer.toString(file_num),Integer.toString(file_size));
                                myRef.child("file").setValue(hm);
                                Firebase();
                            }else{
                                for(Object key : hm.keySet()){

                                    String value = hm.get(key);

                                    if(key.equals(Integer.toString(file_num))&&value.equals(Integer.toString(file_size))){
                                        System.out.println("디비랑 같음");
                                        break;
                                    }else{
                                        System.out.println("디비랑 다름. 새로 입력");
                                        hm = new HashMap<>();
                                        hm.put(Integer.toString(file_num),Integer.toString(file_size));
                                        myRef.child("file").setValue(hm);
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
            nowPlayMusic();
        }



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
                                path=(String) postSnapshot.getValue();
                                System.out.println(now+"찾음 "+path);
                                callback.success(path);
                                break;
                            }

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                    }
                });
        return path;
    }

    public interface Callback{
        void success(String s);
    }
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
}
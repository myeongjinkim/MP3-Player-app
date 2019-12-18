package com.example.hw_4r.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.util.HashMap;
import java.util.Queue;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> lyrics;
    private Boolean check;

    private File fs;
    private String path;

    private HashMap<Object, String> music_list_hm;
    private HashMap<Object, String> file_hm;
    private DatabaseReference myRef;
    private int music_num;
    private int file_num;
    private int file_size;
    private int now;
    private String artist;
    private String title;
    private String lyricsDate;
    private byte[] b;

    private HomeModel homeModel;

    private Callback callback;

    public HomeViewModel() {
        lyrics = new MutableLiveData<>();
        lyrics.setValue("This is home fragment");
        check = true;

        path=("/data/data/com.example.hw_4r/music/");
        fs = new File(path);
        music_list_hm = new HashMap<>();
        file_hm = new HashMap<>();

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
    private void LyricsSetting(String text){
        //가사 가져오는 부분
        lyrics.setValue(text);
    }
    public String getPath(){
        return path.toString();
    }


    public byte[] getAlbum(){
        return b;
    }


    public boolean check(){
        File[] list = fs.listFiles();
        if(list!=null){

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
                System.out.println(myRef.child("file").equals(file_hm));
                System.out.println("뭔데"+myRef.child("file").equals(file_hm));
                file_hm.put(Integer.toString(file_num),Integer.toString(file_size));
                System.out.println("뭔데"+myRef.child("file").equals(file_hm));


                myRef.child("file").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // Get user value
                                System.out.println("d언제되는지");
                                file_hm = (HashMap<Object, String>) (dataSnapshot.getValue());
                                if(file_hm==null){
                                    System.out.println("디비에 없음 완전 새로");
                                    file_hm = new HashMap<>();
                                    file_hm.put(Integer.toString(file_num),Integer.toString(file_size));
                                    myRef.child("file").setValue(file_hm);
                                    Firebase();
                                }else{
                                    for(Object key : file_hm.keySet()){

                                        String value = file_hm.get(key);

                                        if(key.equals(Integer.toString(file_num))&&value.equals(Integer.toString(file_size))){
                                            System.out.println("디비랑 같음");
                                            Firebase();
                                            break;
                                        }else{
                                            System.out.println("디비랑 다름. 새로 입력");
                                            file_hm = new HashMap<>();
                                            file_hm.put(Integer.toString(file_num),Integer.toString(file_size));
                                            myRef.child("file").setValue(file_hm);
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
                System.out.println("이게 분명 뒤지");
                nowPlayMusic();
            }


            return true;
        }
        else{
            return false;
        }


    }
    public void Firebase(){
        music_list_hm = new HashMap<>();
        fs = new File(path.toString());
        if(fs.isDirectory()){
            String decoding = "ISO-8859-1";
            String encoding = "EUC-KR";

            File list[] = fs.listFiles();
            for(File f : list){
                music_num++;
                music_list_hm.put(Integer.toString(music_num), f.toString());
                myRef.child("music_list").setValue(music_list_hm);
                myRef.child("current").setValue(1);

                try{
                    MP3File mp3 = (MP3File) AudioFileIO.read(f);
                    AbstractID3v2Tag tag2 = mp3.getID3v2Tag();
                    if(tag2.hasField("Cover Art")){
                        b = tag2.getFirstArtwork().getBinaryData();
                        System.out.println("이미지: "+b);
                    }

                    Tag tag = mp3.getTag();
                    title = tag.getFirst(FieldKey.TITLE);
                    artist = tag.getFirst(FieldKey.ARTIST);
                    lyricsDate = tag.getFirst(FieldKey.LYRICS);
                    homeModel = new HomeModel(title,artist,lyricsDate,b);
                    myRef.child("music_data").child(Integer.toString(music_num)).setValue(homeModel);
                }catch(Exception ex){
                    ex.printStackTrace();
                }



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
                        path=(String)dataSnapshot.child("music_list").child(Integer.toString(now)).getValue();

                        System.out.println(now+"찾음 "+path);

                        homeModel= dataSnapshot.child("music_data").child(Integer.toString(now)).getValue(HomeModel.class);
                        title=homeModel.getTitle();
                        artist=homeModel.getArtist();
                        LyricsSetting(homeModel.getLyrics());
                        b=homeModel.getImageByte();
                        callback.success(path);

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
    public String getTitle(){
        return title;
    }
    public String getArtist(){
        return artist;
    }
}
package com.example.hw_4r.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> lyrics;
    private Boolean check;

    private File fs;
    private String path;
    private String imgPath;

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
    private Bitmap bitmap;

    private HomeModel homeModel;

    private Callback callback;

    public HomeViewModel() {
        lyrics = new MutableLiveData<>();
        lyrics.setValue("This is home fragment");
        check = true;

        path=("/data/data/com.example.hw_4r/music/");
        imgPath=("/data/data/com.example.hw_4r/musicImage/");
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
        return path;
    }
    public String getImgPath(){
        return imgPath;
    }



    public void check(){
        File[] list = fs.listFiles();
        if(list!=null){

            file_num= list.length;
            if(fs.isDirectory()){
                for(File f : list){
                    file_size+=f.length();
                }

                myRef.child("file").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                file_hm = (HashMap<Object, String>) (dataSnapshot.getValue());
                                if(file_hm==null){
                                    file_hm = new HashMap<>();
                                    file_hm.put(Integer.toString(file_num),Integer.toString(file_size));
                                    myRef.child("file").setValue(file_hm);
                                    Firebase();
                                }else{
                                    for(Object key : file_hm.keySet()){

                                        String value = file_hm.get(key);

                                        if(key.equals(Integer.toString(file_num))&&value.equals(Integer.toString(file_size))){
                                            break;
                                        }else{
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
                nowPlayMusic();
            }
        }
        else{
        }
    }
    public void Firebase(){
        music_list_hm = new HashMap<>();
        fs = new File(path);
        if(fs.isDirectory()){
            String decoding = "ISO-8859-1";
            String encoding = "EUC-KR";

            File list[] = fs.listFiles();
            for(File f : list) {
                if (f.isFile()) {

                    music_num++;
                    music_list_hm.put(Integer.toString(music_num), f.toString());
                    myRef.child("music_list").setValue(music_list_hm);
                    myRef.child("current").setValue(1);

                    try{
                        MP3File mp3 = (MP3File) AudioFileIO.read(f);
                        AbstractID3v2Tag tag2 = mp3.getID3v2Tag();

                        Tag tag = mp3.getTag();
                        title = tag.getFirst(FieldKey.TITLE);
                        artist = tag.getFirst(FieldKey.ARTIST);
                        lyricsDate = tag.getFirst(FieldKey.LYRICS);

                        bitmap = null;

                        byte[] b = tag2.getFirstArtwork().getBinaryData();
                        bitmap = BitmapFactory.decodeByteArray( b, 0, b.length ) ;

                        //저장할 파일 이름
                        String fileName = title + ".jpg";

                        File imgDir = new File(imgPath);

                        //storage 에 파일 인스턴스를 생성합니다.
                        File tempFile = new File(imgPath, fileName);

                        try {
                            // 자동으로 빈 파일을 생성합니다.
                            tempFile.createNewFile();

                            // 파일을 쓸 수 있는 스트림을 준비합니다.
                            FileOutputStream out = new FileOutputStream(tempFile);

                            // compress 함수를 사용해 스트림에 비트맵을 저장합니다.
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

                            // 스트림 사용후 닫아줍니다.
                            out.close();

                        } catch (FileNotFoundException e) {
                            Log.e("MyTag","FileNotFoundException : " + e.getMessage());
                        } catch (IOException e) {
                            Log.e("MyTag","IOException : " + e.getMessage());
                        }

                        homeModel = new HomeModel(title,artist,lyricsDate);

                        myRef.child("music_data").child(Integer.toString(music_num)).setValue(homeModel);
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }

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

                        homeModel= dataSnapshot.child("music_data").child(Integer.toString(now)).getValue(HomeModel.class);
                        title=homeModel.getTitle();
                        artist=homeModel.getArtist();
                        LyricsSetting(homeModel.getLyrics());

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
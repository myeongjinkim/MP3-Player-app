package com.example.hw_4r.ui.home;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> lyrics;
    private Boolean check;
    private String lyricsText;

    public HomeViewModel() {
        lyrics = new MutableLiveData<>();
        lyrics.setValue("This is home fragment");
        check = true;
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
    public void setAlbum(){ } // 앨범자켓 세팅하는 부분
    private void albumSetting(){ }//앨범자켓 가져오는 부분


}
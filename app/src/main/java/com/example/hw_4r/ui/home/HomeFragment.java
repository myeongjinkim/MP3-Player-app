package com.example.hw_4r.ui.home;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw_4r.R;
import com.example.hw_4r.databinding.FragmentHomeBinding;

import java.io.File;
import java.io.IOException;

import static java.lang.Thread.sleep;

public class HomeFragment extends Fragment {


    //ui 및 구조
    private HomeViewModel homeViewModel;
    private FragmentTransaction ft;
    public FragmentHomeBinding binding;
    private HomeLyricsFragment homeLyricsFragment;
    private HomeJacketFragment homeJacketFragment;
    private boolean start = false;
    private boolean check =true;
    private TextView titleText;
    private TextView artistText;

    //mp3
    private MediaPlayer mediaPlayer;
    private SeekBar seekbar;
    private int nowSeek;
    private int maxSeek;
    private TextView maxSeekText;
    private TextView nowSeekText;
    private File fs;

    private String path;

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 화면 전환 프래그먼트 선언 및 초기 화면 설정
        homeLyricsFragment = new HomeLyricsFragment();
        homeJacketFragment = new HomeJacketFragment();

    }
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);
        titleText = (TextView) rootView.findViewById(R.id.music_title);
        artistText = (TextView) rootView.findViewById(R.id.music_artist);
        nowSeekText = (TextView) rootView.findViewById(R.id.nowSeekTextView);
        maxSeekText = (TextView) rootView.findViewById(R.id.maxSeekTextView);
        seekbar = (SeekBar)rootView.findViewById(R.id.seekBar);

        homeViewModel.check();

        HomeViewModel.Callback callback = new HomeViewModel.Callback() {
            @Override
            public void success(String s) {
                path=s;
                already();
            }
        };
        homeViewModel.setCallback(callback);

        binding = DataBindingUtil.bind(rootView);
        binding.setFragment(this);
        return rootView;
    }

    public void replaceToJacket(){
        ft = getChildFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.replace, homeJacketFragment);
        ft.commit();
    }
    public void replaceToLyrics(){
        ft = getChildFragmentManager().beginTransaction();
        ft.addToBackStack(null);
        ft.replace(R.id.replace, homeLyricsFragment);
        ft.commit();
    }
    public void pressPlay(View v){
        if(mediaPlayer!=null){
            if(mediaPlayer.isPlaying()){
                ((ImageButton)v).setImageResource(R.drawable.ic_action_play);
                mediaPlayer.stop();
                try {
                    mediaPlayer.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else {
                ((ImageButton)v).setImageResource(R.drawable.ic_action_stop);
                nowSeek = mediaPlayer.getCurrentPosition();
                mediaPlayer.seekTo(nowSeek); // 일시정지 시점으로 이동
                mediaPlayer.start();
                Thread();
            }
        }

    }
    public void Thread(){
        Runnable task = new Runnable(){
            public void run(){
                while(mediaPlayer.isPlaying()){
                    try {
                        sleep(1000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Message msg =handler.obtainMessage();
                    handler.sendMessage(msg);
                }
            }
        };
        Thread thread = new Thread(task);
        thread.start();
    }
    Handler handler = new Handler(){

        public void handleMessage(Message msg){
            nowSeek = mediaPlayer.getCurrentPosition();
            seekbar.setProgress(nowSeek);
            nowSeekText.setText(changeTime(nowSeek));
        }
    };




    public void already(){

        path= homeViewModel.getPath();
        fs = new File(path);

        if(fs.isFile()){
            //musicData(fs); 여기
            titleText.setText(homeViewModel.getTitle());
            artistText.setText(homeViewModel.getArtist());
            mediaPlayer = MediaPlayer.create(getActivity(), Uri.parse(path));
            maxSeek= mediaPlayer.getDuration();
            seekbar.setMax(maxSeek);

            maxSeekText.setText(changeTime(maxSeek));

            seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    // TODO Auto-generated method stub
                    if(fromUser) {
                        mediaPlayer.seekTo(progress);
                        nowSeek = progress;
                        nowSeekText.setText(changeTime(nowSeek));
                    }
                }
            });


        }

        if( homeViewModel.getCheck()){
            replaceToJacket();
        }else{
            replaceToLyrics();
        }
    }
    public native String changeTime(int intTime);




}
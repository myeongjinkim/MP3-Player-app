package com.example.hw_4r.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw_4r.R;
import com.example.hw_4r.databinding.FragmentHomeLyricsBinding;

public class HomeLyricsFragment extends Fragment {
    private TextView lyricsTextView;
    private HomeViewModel homeViewModel;
    HomeFragment homeFragment;
    public FragmentHomeLyricsBinding binding;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_lyrics, container, false);
        lyricsTextView = rootView.findViewById(R.id.lyrics);
        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        homeViewModel.getLyrics().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                lyricsTextView.setText(s);
            }
        });

        binding = DataBindingUtil.bind(rootView);
        binding.setFragment(this);
        return rootView;
    }
    public void pressLyrics(View view){
        homeFragment = (HomeFragment)getParentFragment();
        homeFragment.replaceToJacket();
        homeViewModel.setCheck();
    }

}

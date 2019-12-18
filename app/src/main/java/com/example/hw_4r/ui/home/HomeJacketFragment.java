package com.example.hw_4r.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.hw_4r.R;
import com.example.hw_4r.databinding.FragmentHomeJacketBinding;

public class HomeJacketFragment  extends Fragment {
    private HomeViewModel homeViewModel;
    private HomeFragment homeFragment;
    private ImageView imageView;
    private byte[] b;
    public FragmentHomeJacketBinding binding;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home_jacket, container, false);

        homeViewModel = ViewModelProviders.of(getActivity()).get(HomeViewModel.class);

        b=homeViewModel.getAlbum();
        if(b!=null){
            System.out.println("이미지?: "+b);
            imageView = (ImageView) rootView.findViewById(R.id.Jacket);
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            imageView.setImageBitmap(bitmap) ;
        }



        binding = DataBindingUtil.bind(rootView);
        binding.setFragment(this);
        return rootView;
    }
    public void pressJacket(View view){
        homeFragment = (HomeFragment)getParentFragment();
        homeFragment.replaceToLyrics();
        homeViewModel.setCheck();
    }

}

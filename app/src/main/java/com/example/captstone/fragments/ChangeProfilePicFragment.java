package com.example.captstone.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.captstone.R;
import com.example.captstone.viewPagerAdapters.HomeViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ChangeProfilePicFragment extends Fragment {
    private static final String TAG = "ChangeProfilePicFragment";

    TabLayout tabLayout;
    ViewPager2 viewPager;
    HomeViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_profile_pic, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}

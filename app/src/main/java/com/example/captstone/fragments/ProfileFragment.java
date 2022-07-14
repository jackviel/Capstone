package com.example.captstone.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.captstone.HomeViewPagerAdapter;
import com.example.captstone.LoginScreenActivity;
import com.example.captstone.ProfileViewPagerAdapter;
import com.example.captstone.R;
import com.google.android.material.tabs.TabLayout;
import com.parse.ParseUser;

public class ProfileFragment extends Fragment {
    public static final String TAG = "SettingsFragment";

    private ImageView ivProfilePic;
    private TextView tvUsername;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    ProfileViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUsername = view.findViewById(R.id.tvUsername);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new ProfileViewPagerAdapter(this);
        viewPager.setAdapter(adapter);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
    }
}
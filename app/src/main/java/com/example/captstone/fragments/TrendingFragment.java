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
import com.example.captstone.TrendingViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class TrendingFragment extends Fragment {
    public static final String TAG = "TrendingFragment";

    private TabLayout tabLayout;
    private ViewPager2 viewPager;
    private TrendingViewPagerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);
        adapter = new TrendingViewPagerAdapter(this);
        viewPager.setUserInputEnabled(false);
        viewPager.setAdapter(adapter);

    }
}
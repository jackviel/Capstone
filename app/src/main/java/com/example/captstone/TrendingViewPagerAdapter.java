package com.example.captstone;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.captstone.fragments.ReviewFeedFragment;
import com.example.captstone.fragments.TrendingFeedFragment;

public class TrendingViewPagerAdapter extends FragmentStateAdapter {

    public TrendingViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new TrendingFeedFragment();
            default:
                return new TrendingFeedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
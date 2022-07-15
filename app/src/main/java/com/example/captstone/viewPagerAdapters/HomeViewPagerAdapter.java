package com.example.captstone.viewPagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.captstone.fragments.ReviewFeedFragment;

public class HomeViewPagerAdapter extends FragmentStateAdapter {

    public HomeViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ReviewFeedFragment();
            case 1:
                return new ReviewFeedFragment();
            default:
                return new ReviewFeedFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

package com.example.captstone.viewPagerAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.captstone.fragments.MyListFragment;
import com.example.captstone.fragments.MyReviewsFragment;
import com.example.captstone.fragments.SettingsFragment;

public class ProfileViewPagerAdapter extends FragmentStateAdapter {

    public ProfileViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyReviewsFragment();
            case 1:
                return new MyListFragment();
            case 2:
                return new SettingsFragment();
            default:
                return new MyReviewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}

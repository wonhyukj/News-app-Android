package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.google.android.material.tabs.TabLayout;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int numOfTabs;

    public PagerAdapter(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new HeadlineTabFragment("World");
            case 1:
                return new HeadlineTabFragment("Business");
            case 2:
                return new HeadlineTabFragment("Politics");
            case 3:
                return new HeadlineTabFragment("Sports");
            case 4:
                return new HeadlineTabFragment("Technology");
            case 5:
                return new HeadlineTabFragment("Science");

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

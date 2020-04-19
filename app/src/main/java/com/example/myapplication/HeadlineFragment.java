package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class HeadlineFragment extends Fragment {
    TabLayout tabLayout;
    public static HeadlineFragment newInstance() {
        HeadlineFragment fragment = new HeadlineFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.headline_layout, container, false);
        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        TabItem world = (TabItem) view.findViewById(R.id.World);
        TabItem business = (TabItem) view.findViewById(R.id.Business);
        TabItem politics = (TabItem) view.findViewById(R.id.Politics);
        TabItem sports = (TabItem) view.findViewById(R.id.Sports);
        TabItem technology = (TabItem) view.findViewById(R.id.Technology);
        TabItem science = (TabItem) view.findViewById(R.id.Science);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

//        PagerAdapter pagerAdapter = new PagerAdapter(getFragmentManager(), tabLayout.getTabCount()) {
//            @Override
//            public int getCount() {
//                return 0;
//            }
//
//            @Override
//            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
//                return false;
//            }
//        };
//        // tabLayout.getTabCount()
//        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;

    }
}

package com.nttung.oufood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.nttung.oufood.Adapter.ViewPagerMyOrderAdapter;
import com.nttung.oufood.R;


public class OrderFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);

        ViewPagerMyOrderAdapter viewPageMyOrderAdapter = new ViewPagerMyOrderAdapter(requireActivity());

        mViewPager.setAdapter(viewPageMyOrderAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Đang giao");
                    break;
                case 1:
                    tab.setText("Đã giao");
                    break;
            }
        }).attach();
        return view;
    }
}
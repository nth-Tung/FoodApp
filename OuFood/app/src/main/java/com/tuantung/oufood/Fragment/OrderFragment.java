package com.tuantung.oufood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Adapter.ViewPagerMyOrderAdapter;
import com.tuantung.oufood.Class.User;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import io.paperdb.Paper;


public class OrderFragment extends Fragment {

    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

//        Paper.init(requireActivity());
//
//
//
        mTabLayout = view.findViewById(R.id.tabLayout);
        mViewPager = view.findViewById(R.id.viewPager);
//
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
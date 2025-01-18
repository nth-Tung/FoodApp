package com.nttung.oufood.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


import com.nttung.oufood.Fragment.HistoryFragment;
import com.nttung.oufood.Fragment.OnGoingFragment;

public class ViewPagerMyOrderAdapter extends FragmentStateAdapter {


    public ViewPagerMyOrderAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }


    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new OnGoingFragment();
            case 1:
                return new HistoryFragment();
            default:
                return new OnGoingFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}

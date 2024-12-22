package com.tuantung.oufood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.tuantung.oufood.R;


public class OnGoingFragment extends Fragment {
    RecyclerView recyclerView;

    DatabaseReference data_requests;

//    MyOrderAdapter adapter;

    TextView null_orders;

    final String status = "0";

    FrameLayout frameLayout;

    public OnGoingFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_on_going, container, false);

        return view;
    }
}
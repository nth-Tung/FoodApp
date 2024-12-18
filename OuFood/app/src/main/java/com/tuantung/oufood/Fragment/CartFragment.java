package com.tuantung.oufood.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tuantung.oufood.Adapter.CartAdapter;
import com.tuantung.oufood.Class.Order;
import com.tuantung.oufood.Database.Database;
import com.tuantung.oufood.R;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private RecyclerView recyclerView;
    private Database databaseSqlite;
    List<Order> cart = new ArrayList<>();
    private TextView textViewNoData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        textViewNoData = view.findViewById(R.id.no_data);
        recyclerView = view.findViewById(R.id.recyclerView);

        databaseSqlite = new Database(requireActivity());

        initList();
        return view;
    }

    private void initList(){

        cart = databaseSqlite.getCarts();
        if(cart.isEmpty()){
           textViewNoData.setVisibility(View.VISIBLE);
//            binding.scrollViewCart.setVisibility(View.GONE);
        }else{
            textViewNoData.setVisibility(View.GONE);
//            binding.scrollViewCart.setVisibility(View.VISIBLE);
        }

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        RecyclerView.Adapter  adapter= new CartAdapter(databaseSqlite.getCarts());
//        {
//            @Override
//            public void change() {
//                calculateCart();
//            }
//        };
        recyclerView.setAdapter(adapter);
    }
}
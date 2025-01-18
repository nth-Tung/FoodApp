package com.nttung.oufood.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Activity.AddressListActivity;
import com.nttung.oufood.Activity.CartActivity;
import com.nttung.oufood.Activity.SaleListActivity;
import com.nttung.oufood.Activity.SearchActivity;
import com.nttung.oufood.Adapter.CategoryAdapter;
import com.nttung.oufood.Adapter.FoodListAdapter;
import com.nttung.oufood.Adapter.SaleListAdapter;
import com.nttung.oufood.Class.Category;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MenuFragment extends Fragment {

    RecyclerView recyclerView_bestSeller, recyclerView_categories, recyclerView_all_food;
    ConstraintLayout buttonSearch;
    private ImageView imageView_cart;
    private TextView textView_topSale;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        textView_topSale = view.findViewById(R.id.top_sale);
        imageView_cart = view.findViewById(R.id.imageView_cart);
        buttonSearch = view.findViewById(R.id.button_search);

        //move to searchActivity
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SearchActivity.class));
            }
        });

        textView_topSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SaleListActivity.class));
            }
        });

        //move to cartActivity
        imageView_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), CartActivity.class));
            }
        });

//        recycler categories
        recyclerView_categories = view.findViewById(R.id.recycler_categories);
        setupRecyclerCategories();

        recyclerView_bestSeller = view.findViewById(R.id.recycler_best_seller);
        recyclerView_all_food = view.findViewById(R.id.recyclerView_all_food);
        setupRecyclerViewFoods();
        return view;
    }

    private void setupRecyclerCategories() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    category1.setId(dataSnapshot.getKey());
                    list.add(category1);
                }
                Category other = list.get(0);
                list.remove(other);
                list.add(other);

                CategoryAdapter adapter = new CategoryAdapter(list);
                recyclerView_categories.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.HORIZONTAL,false));
                recyclerView_categories.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerViewFoods() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Food> list = new ArrayList<>();
                List<Food> listAllFood = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());

                    if (food.getDiscount().equals("0")) {
                        listAllFood.add(food);
                    } else list.add(food);
                }

                list.sort(Food::sortByDiscount);
                Collections.reverse(list);

                List<Food> listTopFood = list.subList(0, Math.min(Common.TOP_BEST_SELLER, list.size()));

                SaleListAdapter bestSellerAdapter = new SaleListAdapter(listTopFood);
                recyclerView_bestSeller.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
                recyclerView_bestSeller.setAdapter(bestSellerAdapter);


                FoodListAdapter foodAdapter = new FoodListAdapter(listAllFood);
                recyclerView_all_food.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
                recyclerView_all_food.setAdapter(foodAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
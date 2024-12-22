package com.tuantung.oufood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Activity.CartActivity;
import com.tuantung.oufood.Activity.SaleListActivity;
import com.tuantung.oufood.Activity.SearchActivity;
import com.tuantung.oufood.Adapter.CategoryAdapter;
import com.tuantung.oufood.Adapter.FoodListAdapter;
import com.tuantung.oufood.Adapter.SaleListAdapter;
import com.tuantung.oufood.Class.Category;
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MenuFragment extends Fragment {
    
    RecyclerView recyclerView_bestSeller, recyclerView_categories, recyclerView_all_food;
    private ProgressBar mProgressBarCategory;
    private ProgressBar mProgressBarBestFood;
    private TextView mButtonFlashSale;
    private ImageView imageView_cart;
    private TextView searchView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        imageView_cart = view.findViewById(R.id.imageView_cart);
        imageView_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), CartActivity.class));
            }
        });


        //button flashSale
        mButtonFlashSale = view.findViewById(R.id.button_flashSale);
        mButtonFlashSale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SaleListActivity.class));
            }
        });


//        recycler categories
        mProgressBarCategory = view.findViewById(R.id.progressBar_category);
        recyclerView_categories = view.findViewById(R.id.recycler_categories);
        setupRecyclerCategories();

//        recycler best seller
        mProgressBarBestFood = view.findViewById(R.id.progressBar_bestFood);
        recyclerView_bestSeller = view.findViewById(R.id.recycler_best_sale);
        setupRecyclerViewBestSeller();

//        recycler all food
        recyclerView_all_food = view.findViewById(R.id.recyclerView_all_food);
        setupRecyclerViewAllFood();




        searchView = view.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(requireActivity(), SearchActivity.class));
            }
        });
//
//
//        searchView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        if (query.trim().isEmpty()) {
//                            frameLayoutSearch.setVisibility(View.GONE);
//                        } else {
//                            homeSearchFragment.setKeySearch(query.toLowerCase());
//                            frameLayoutSearch.setVisibility(View.VISIBLE);
//                            getChildFragmentManager().beginTransaction().replace(R.id.fragment_search,homeSearchFragment).commit();
//                        }
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        if (newText.trim().isEmpty()) {
//                            frameLayoutSearch.setVisibility(View.GONE);
//                        } else {
//                            homeSearchFragment.setKeySearch(newText.toLowerCase());
//                            frameLayoutSearch.setVisibility(View.VISIBLE);
//                            getChildFragmentManager().beginTransaction()
//                                    .replace(R.id.fragment_search, homeSearchFragment)
//                                    .commit();
//                        }
//                        return true;
//                    }
//                });
//            }
//        });




        return view;
    }





    private void setupRecyclerCategories() {
        mProgressBarCategory.setVisibility(View.VISIBLE);
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Category> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Category category1 = dataSnapshot.getValue(Category.class);
                    category1.setId(dataSnapshot.getKey());
                    list.add(category1);
                }

                if(list.size()>0){
                    recyclerView_categories.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false));
                    RecyclerView.Adapter adapter = new CategoryAdapter(list);
                    recyclerView_categories.setAdapter(adapter);
                }
                mProgressBarCategory.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerViewBestSeller() {
        mProgressBarBestFood.setVisibility(View.VISIBLE);
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }


                for (int i = list.size() - 1; i >= 0; i--) {
                    if (list.get(i).getDiscount().equals("0")) {
                        list.remove(i);
                    }
                }

                for (int i = list.size() - 1; i >= Common.TOP_BEST_SELLER; i--) {
                    list.remove(i);
                }

                if(list.size()>0){
                    recyclerView_bestSeller.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.HORIZONTAL,false));
                    RecyclerView.Adapter adapter = new SaleListAdapter(list);
                    recyclerView_bestSeller.setAdapter(adapter);
                }
                mProgressBarBestFood.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecyclerViewAllFood() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

                Collections.shuffle(list);

                for(int i = list.size() - 1;i>=0;i--){
                    if(!list.get(i).getDiscount().equals("0"))
                        list.remove(i);
                }


                if(list.size()>0){
                    recyclerView_all_food.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
                    RecyclerView.Adapter adapter = new FoodListAdapter(list);
                    recyclerView_all_food.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
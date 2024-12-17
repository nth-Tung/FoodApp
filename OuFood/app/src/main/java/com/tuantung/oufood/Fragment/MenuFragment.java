package com.tuantung.oufood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Adapter.FoodListAdapter;
import com.tuantung.oufood.Class.Category;
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.ArrayList;
import java.util.List;


public class MenuFragment extends Fragment {

//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public MenuFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment MenuFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static MenuFragment newInstance(String param1, String param2) {
//        MenuFragment fragment = new MenuFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    View include_category1;

    RecyclerView recyclerView_bestSeller, recyclerView_categories, recyclerView_all_food;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);

//        recycler categories
//        recyclerView_categories = view.findViewById(R.id.recycler_categories);
//        setupRecyclerCategories();

////        recycler best seller
//        recyclerView_bestSeller = view.findViewById(R.id.recycler_best_sale);
//        setupRecyclerViewBestSeller();

//        recycler all food
        recyclerView_all_food = view.findViewById(R.id.recyclerView_all_food);
        setupRecyclerViewAllFood();

        return view;
    }

//    private void setupRecyclerCategories() {
//        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Category> list = new ArrayList<>();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    Category category1 = dataSnapshot.getValue(Category.class);
//                    category1.setId(dataSnapshot.getKey());
//                    list.add(category1);
//                }
//                CategoryAdapter adapter = new CategoryAdapter(list, getContext());
//                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_categories, adapter, 2, androidx.recyclerview.widget.RecyclerView.HORIZONTAL);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }
//
//    private void setupRecyclerViewBestSeller() {
//        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                List<Food> list = new ArrayList<>();
//                Food food;
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    food = dataSnapshot.getValue(Food.class);
//                    food.setId(dataSnapshot.getKey());
//                    list.add(food);
//                }
//
//                list.sort((a, b) -> -a.sortForBestSeller(b));
//
//                for (int i = list.size() - 1; i >= Common.TOP_BEST_SELLER; i--) {
//                    list.remove(i);
//                }
//                BestSellerAdapter bestSellerAdapter = new BestSellerAdapter(getContext(), list);
//                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_bestSeller, bestSellerAdapter, 1, RecyclerView.HORIZONTAL);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
//    }

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
                if(list.size()>0){
                    recyclerView_all_food.setLayoutManager(new GridLayoutManager(requireActivity(),2));
                    RecyclerView.Adapter adapter = new FoodListAdapter(list);
                    recyclerView_all_food.setAdapter(adapter);
                }

//                list.sort((a, b) -> a.sortForBestSeller(b));
//
//                List<Food> listAdapter = new ArrayList<>();
//
//                for (int i = Common.TOP_BEST_SELLER; i < list.size(); i++) {
//                    listAdapter.add(list.get(i));
//                }
//
//                FoodListAdapter foodAdapter = new FoodListAdapter(listAdapter, getContext());
//                SetUpRecyclerView.setupGridLayout(getContext(), recyclerView_all_food, foodAdapter, 2, RecyclerView.VERTICAL);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
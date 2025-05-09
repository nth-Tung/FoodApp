package com.nttung.oufood.Activity;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Adapter.FoodListAdapter;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.util.ArrayList;

public class SaleListActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sale_list);

        toolbar = findViewById(R.id.toolbar_FoodList);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_sale);
        setupRecyclerViewBestSeller();
    }

    private void setupRecyclerViewBestSeller() {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addListenerForSingleValueEvent(new ValueEventListener() {
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



                if (list.size() > 0) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(SaleListActivity.this, LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter adapter = new FoodListAdapter(list);
                    recyclerView.setAdapter(adapter);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
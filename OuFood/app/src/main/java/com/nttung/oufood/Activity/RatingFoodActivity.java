package com.nttung.oufood.Activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Adapter.RatingFoodAdapter;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.Class.Request;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.util.ArrayList;
import java.util.List;

public class RatingFoodActivity extends AppCompatActivity {

    MaterialToolbar toolbar;
    RecyclerView recyclerView;

    RatingFoodAdapter adapter;

    String idRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rating_food);

        if (getIntent() != null) {
            idRequest = getIntent().getStringExtra("idRequest");
        }

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);

        Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Request request = snapshot.getValue(Request.class);
                List<Order> list = new ArrayList<>();
                for (Order order : request.getOrders()) {
                    if (!order.getIsRate()) {
                        list.add(order);
                    }
                }

                adapter = new RatingFoodAdapter(list);
                recyclerView.setLayoutManager(new LinearLayoutManager(RatingFoodActivity.this, LinearLayoutManager.VERTICAL, false));
                recyclerView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_rating_activity, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        if (item.getItemId() == R.id.action_send) {
            List<Order> list = adapter.getItems();
            for (Order order : list) {
                order.setIsRate(true);
                Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Food food = snapshot.getValue(Food.class);
                        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).child("countRating").setValue(food.getCountRating() + 1);
                        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).child("countStars").setValue(food.getCountStars() + order.getCountStars());
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).child("orders").setValue(list);

            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
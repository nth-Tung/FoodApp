package com.tuantung.oufood.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Adapter.FoodListAdapter;
import com.tuantung.oufood.Class.Category;
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;

public class FoodListActivity extends AppCompatActivity {

    DatabaseReference data_foods;

    RecyclerView recyclerView;

    String categoryId = "";

    SearchView searchView;
    Toolbar toolbar;
    FoodListAdapter adapter;

    TextView tv_noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_food_list);

        tv_noData = findViewById(R.id.tv_noData);

        toolbar = findViewById(R.id.toolbar_FoodList);

        toolbar.setTitle("Category");
        setSupportActionBar(toolbar);

//         firebase
        data_foods = Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS);

        recyclerView = findViewById(R.id.recycler_food);

//        get intent here
        if (getIntent() != null) {
            categoryId = getIntent().getStringExtra("categoryId");
        }
        if (!categoryId.isEmpty() && categoryId != "") {
            setupRecycler(categoryId);
        }

//        title
        Common.FIREBASE_DATABASE.getReference(Common.REF_CATEGORIES).child(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.getValue(Category.class).getName();
                getSupportActionBar().setTitle(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        setupRecycler(categoryId);

    }

    public static String removeDiacritics(String input) {
        // Chuẩn hóa chuỗi Unicode thành dạng Normal Form Decomposed (NFD)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Loại bỏ các dấu bằng cách chỉ giữ lại các ký tự không thuộc nhóm ký tự tổ hợp (diacritics)
        return normalized.replaceAll("\\p{M}", "");
    }


    private void filter(String text) {
        data_foods.orderByChild("categoryId").equalTo(categoryId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                ArrayList<Food> list = new ArrayList<>();
                Food food;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    food = dataSnapshot.getValue(Food.class);
                    if (removeDiacritics(food.getName().toLowerCase().trim()).contains(text)||food.getName().toLowerCase().trim().contains(text)) {
                        food.setId(dataSnapshot.getKey());
                        list.add(food);
                    }
                }

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    adapter.setList(list);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setupRecycler(String categoryId) {
        data_foods.orderByChild("categoryId").equalTo(categoryId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> list = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Food food = dataSnapshot.getValue(Food.class);
                    food.setId(dataSnapshot.getKey());
                    list.add(food);
                }

                if (list.size() > 0) {
                    recyclerView.setLayoutManager(new LinearLayoutManager(FoodListActivity.this, LinearLayoutManager.VERTICAL, false));
                    adapter = new FoodListAdapter(list);
                    recyclerView.setAdapter(adapter);
                } else {
                    tv_noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_food_list, menu);

        MenuItem item_search = menu.findItem(R.id.action_search);

        searchView = (SearchView) item_search.getActionView();
        searchView.setQueryHint("Tìm món ngon");
        searchView.clearFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query.toLowerCase().trim());
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText.toLowerCase().trim());
                return true;
            }
        });
        searchView.setOnCloseListener(() -> {
            searchView.onActionViewCollapsed();
            return true;
        });

        return true;
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

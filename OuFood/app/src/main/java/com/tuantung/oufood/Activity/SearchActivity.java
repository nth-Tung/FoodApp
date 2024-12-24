package com.tuantung.oufood.Activity;

import android.os.Bundle;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Adapter.FilterFoodsAdapter;
import com.tuantung.oufood.Adapter.FoodListAdapter;
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView tv_noData;

    FilterFoodsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView =findViewById(R.id.recycler_food);
        loadListFood();

        tv_noData = findViewById(R.id.tv_noData);
    }

    private void loadListFood(){
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

                Collections.shuffle(list);

                if(list.size()>0){
                    recyclerView.setLayoutManager(new GridLayoutManager(SearchActivity.this,2));
                    adapter = new FilterFoodsAdapter(list);
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String removeDiacritics(String input) {
        // Chuẩn hóa chuỗi Unicode thành dạng Normal Form Decomposed (NFD)
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        // Loại bỏ các dấu bằng cách chỉ giữ lại các ký tự không thuộc nhóm ký tự tổ hợp (diacritics)
        return normalized.replaceAll("\\p{M}", "");
    }

    private void filter(String text) {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).addListenerForSingleValueEvent(new ValueEventListener() {
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

                Collections.shuffle(list);

                if (list.isEmpty()) {
                    tv_noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tv_noData.setVisibility(View.GONE);
                    adapter.setItems(list);
                    adapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_food,menu);

        MenuItem item_search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) item_search.getActionView();
        searchView.setQueryHint("Tìm món ngon");
        searchView.setIconified(false); // Mở rộng SearchView
        searchView.requestFocus();     // Focus vào ô tìm kiếm

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()  {
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
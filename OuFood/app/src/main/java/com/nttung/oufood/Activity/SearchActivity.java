package com.nttung.oufood.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Adapter.FilterFoodsAdapter;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Collections;

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

                // Ẩn bàn phím
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                        searchView.clearFocus();
                    }
                }
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
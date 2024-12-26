package com.tuantung.oufood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tuantung.oufood.R;

public class AddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView imageView_updateAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);

        imageView_updateAddress = findViewById(R.id.imageView_updateAddress);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //move to SelectAddressActivity
        imageView_updateAddress.setOnClickListener(v -> {
            startActivity(new Intent(AddressActivity.this, SelectAddressActivity.class));
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
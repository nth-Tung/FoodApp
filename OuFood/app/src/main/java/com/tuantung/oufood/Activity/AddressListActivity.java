package com.tuantung.oufood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tuantung.oufood.R;

public class AddressListActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textViewUpdate;
    private AppCompatButton buttonAddAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_list);

        textViewUpdate = findViewById(R.id.textView_update);
        buttonAddAddress = findViewById(R.id.button_addAddress);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //move to AddressActivity
        textViewUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressListActivity.this, AddressActivity.class));
            }
        });

        //move to AddressActivity
        buttonAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressListActivity.this, AddressActivity.class));
            }
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
package com.tuantung.oufood.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

public class EditNameUserActivity extends AppCompatActivity {
    AppCompatButton btn_luu;
     ImageView buttonBack;
    TextInputEditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name_user);

        //button back
        buttonBack= findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_luu = findViewById(R.id.btn_luu);
        input = findViewById(R.id.input);

        input.setText(Common.currentUser.getName());


        btn_luu.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();
            Common.currentUser.setName(newName);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.currentUser.getIdUser()).child("name").setValue(newName);

            finish();
        });

    }
}
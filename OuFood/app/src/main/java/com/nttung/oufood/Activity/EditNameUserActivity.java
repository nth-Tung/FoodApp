package com.nttung.oufood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

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

        input.setText(Common.CURRENTUSER.getName());


        btn_luu.setOnClickListener(v -> {
            String newName = input.getText().toString().trim();
            Common.CURRENTUSER.setName(newName);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.CURRENTUSER.getIdUser()).child("name").setValue(newName);

            finish();
        });

    }
}
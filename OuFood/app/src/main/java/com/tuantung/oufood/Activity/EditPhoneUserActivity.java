package com.tuantung.oufood.Activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

public class EditPhoneUserActivity extends AppCompatActivity {
    TextView btn_luu;
    TextInputEditText input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_phone_user);

        btn_luu = findViewById(R.id.btn_luu);
        input = findViewById(R.id.input);

        input.setText(Common.currentUser.getPhone());

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString().trim();
                if (newText.equals(Common.currentUser.getPhone()) || newText.length() != 10) {
                    btn_luu.setTextColor(getResources().getColor(R.color.gray));
                    btn_luu.setEnabled(false);
                } else {
                    btn_luu.setTextColor(getResources().getColor(R.color.blue));
                    btn_luu.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String newText = s.toString().trim();
                if (newText.equals(Common.currentUser.getPhone()) || newText.length() != 10) {
                    btn_luu.setTextColor(getResources().getColor(R.color.gray));
                    btn_luu.setEnabled(false);
                } else {
                    btn_luu.setTextColor(getResources().getColor(R.color.blue));
                    btn_luu.setEnabled(true);
                }
            }
        });

        btn_luu.setOnClickListener(v -> {
            String newText = input.getText().toString().trim();
            Common.currentUser.setPhone(newText);
            Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.currentUser.getIdUser()).child("phone").setValue(newText);
            finish();
//            bottomSheetDialogSave.onSave(Common.currentUser);

        });
    }
}
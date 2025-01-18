package com.nttung.oufood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.nttung.oufood.R;

public class ForgotPasswordActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private AppCompatButton btn_xac_nhan;
    private TextInputEditText edit_email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edit_email = findViewById(R.id.edit_email);
        btn_xac_nhan = findViewById(R.id.btn_xac_nhan);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//      butotn login
        btn_xac_nhan.setOnClickListener(v -> {
            if (edit_email.getText().toString().trim().isEmpty()) {
                Toast.makeText(this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            checkEmailIsExist(edit_email.getText().toString().trim());
        });
    }

    private void sendNewPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Gửi email thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmailIsExist(String email) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, "1").addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Exception exception = task.getException();
                    if (exception instanceof FirebaseAuthInvalidUserException) {
                        Toast.makeText(ForgotPasswordActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    sendNewPassword(email);
                    startActivity(new Intent(ForgotPasswordActivity.this,SignInActivity.class));
                }
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
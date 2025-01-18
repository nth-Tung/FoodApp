package com.nttung.oufood.Activity;

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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edit_email = findViewById(R.id.edit_email);
        btn_xac_nhan = findViewById(R.id.btn_xac_nhan);



//      butotn login
        btn_xac_nhan.setOnClickListener(v -> {
            if (edit_email.getText().toString().trim().isEmpty()) {
//                CuteToast.ct(ForgotPasswordActivity.this, "Hãy nhập email!", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
                Toast.makeText(this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            checkEmailIsExist(edit_email.getText().toString().trim());
        });
    }

    private void sendNewPassword(String email) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                CuteToast.ct(ForgotPasswordActivity.this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
            } else {
//                CuteToast.ct(ForgotPasswordActivity.this, "Gửi email thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
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
//                        CuteToast.ct(ForgotPasswordActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                        Toast.makeText(ForgotPasswordActivity.this, "Tài khoản không tồn tại", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.d("ZZZZZ ForgorPasswordActivity-checkEmailIsExist()", exception.getMessage());
                    }
                } else {
                    sendNewPassword(email);
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
package com.tuantung.oufood.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.tuantung.oufood.R;

public class ForgotPasswordActivity extends BaseActivity {

    private AppCompatButton btn_xac_nhan;
    private TextInputEditText edit_email;
    private ImageView buttonBack;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_forgot_password);

        edit_email = findViewById(R.id.edit_email);
        btn_xac_nhan = findViewById(R.id.btn_xac_nhan);

        buttonBack = findViewById(R.id.buttonBack);


//      butotn login
        btn_xac_nhan.setOnClickListener(v -> {
            if (TextUtils.isEmpty(edit_email.getText().toString().trim())) {
//                CuteToast.ct(ForgotPasswordActivity.this, "Hãy nhập email!", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
                Toast.makeText(this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }
            checkEmailIsExist(edit_email.getText().toString().trim());
        });

        //button back
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void sendNewPassword(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
//                CuteToast.ct(ForgotPasswordActivity.this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                Toast.makeText(this, "Email đặt lại mật khẩu đã được gửi", Toast.LENGTH_SHORT).show();
            } else {
//                CuteToast.ct(ForgotPasswordActivity.this, "Gửi email thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
//                Toast.makeText(this, "Gửi email thất bại. Vui lòng thử lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkEmailIsExist(String email) {
        mAuth.signInWithEmailAndPassword(email, "1").addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<AuthResult>() {
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
}
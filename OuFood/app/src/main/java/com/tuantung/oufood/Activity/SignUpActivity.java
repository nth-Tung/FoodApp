package com.tuantung.oufood.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.tuantung.oufood.Class.User;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.ArrayList;

public class SignUpActivity extends AppCompatActivity {
    TextInputEditText edit_name, edit_email, edit_password, edit_confPass, edit_phone;
    AppCompatButton btn_signup;
    TextView tv_loginnow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edit_name = findViewById(R.id.edit_name_user);
        edit_email = findViewById(R.id.edit_email_signup);
        edit_password = findViewById(R.id.edit_password_signup);
        edit_confPass = findViewById(R.id.edit_confPass);
        btn_signup = findViewById(R.id.btn_signup);
        tv_loginnow = findViewById(R.id.tv_loginNow);
        edit_phone = findViewById(R.id.edit_phone);

//         button Signup - mbtn_Signup
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = edit_name.getText().toString();
                String email = edit_email.getText().toString();
                String password = edit_password.getText().toString();
                String confPass = edit_confPass.getText().toString();
                if (name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Hãy nhập tên!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (email.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Hãy nhập mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (confPass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Nhập lại mật khẩu!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra mật khẩu với biểu thức chính quy
                String passwordPattern = "(?=.*[A-Z])(?=.*[!@#$%^&*()_+\\-={}|;:'\",<.>/?])(?=.*\\d).{8,}";
                if (!password.matches(passwordPattern)) {
                    Toast.makeText(SignUpActivity.this, "Mật khẩu phải có ít nhất 8 ký tự, một ký tự đặc biệt và một ký tự hoa!", Toast.LENGTH_SHORT).show();
                    return;
                }

                createAccount(email, password);
            }
        });

//         tv_loginnow
        tv_loginnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void createAccount(String email, String password) {
        // [START create_user_with_email]
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                createInfo(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            } else {
                                Toast.makeText(getApplicationContext(), "1 Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getApplicationContext(), "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void createInfo(String userUid) {
        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                User user = new User(userUid, edit_name.getText().toString().trim(), edit_phone.getText().toString().trim(), edit_email.getText().toString().trim(), "https://res.cloudinary.com/dqe19i7og/image/upload/v1731659931/pngtree-account-avatar-user-abstract-circle-background-flat-color-icon-png-image_1650938_xqovwm.jpg", new ArrayList<>());
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(SignUpActivity.this);
        alert.setTitle("Successfully!");
        alert.setMessage("Please check your email and click the URL to activate your account");
        alert.setIcon(R.drawable.baseline_info_24);

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                startActivity(intent);
                finish();
            }
        });
        alert.show();
    }
}
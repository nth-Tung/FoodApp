package com.nttung.oufood.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Helper.Customer_LoadingDialog;
import com.nttung.oufood.Class.User;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import io.paperdb.Paper;

public class SignInActivity extends AppCompatActivity {

    private AppCompatButton btn_signin;
    private TextView tv_forgotPassword;
    private TextView tv_signupNow;
    private TextInputEditText edit_email;
    private TextInputEditText  edit_password;
    private CheckBox checkBox;
    private Customer_LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edit_email = findViewById(R.id.edit_email);
        edit_password = findViewById(R.id.edit_password);
        tv_forgotPassword = findViewById(R.id.tv_ForgotPass);
        btn_signin = findViewById(R.id.btn_xac_nhan);
        tv_signupNow = findViewById(R.id.tv_SignupNow);
        checkBox = findViewById(R.id.checkbox_remember);

        Paper.init(this);
        loadingDialog = new Customer_LoadingDialog(this, null);


//      butotn login
        btn_signin.setOnClickListener(v -> {
            if (edit_email.getText().toString().isEmpty()) {
                Toast.makeText(SignInActivity.this, "Hãy nhập email!", Toast.LENGTH_SHORT).show();
                return;
            }

            if (edit_password.getText().toString().isEmpty()) {
                Toast.makeText(SignInActivity.this, "Hãy nhập password!", Toast.LENGTH_SHORT).show();
                return;
            }


            login(edit_email.getText().toString(), edit_password.getText().toString());
            loadingDialog.show();

        });

        // textview Forgot password - mtv_ForgotPass
        tv_forgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        tv_signupNow.setOnClickListener(v -> {
            Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        });

    }


    private void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(SignInActivity.this, task -> {
            if (task.isSuccessful()) {
                if (FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()) {

                    if (checkBox.isChecked()) {
                        Paper.book().write(Common.USERNAME_KEY, email);
                        Paper.book().write(Common.PASSWORD_KEY, password);
                    }

                    String userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User user = snapshot.getValue(User.class);
                            Common.CURRENTUSER = user;

                            Intent homeIntent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(homeIntent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });
                } else {
                    loadingDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(SignInActivity.this);
                    alert.setTitle("Thông báo");
                    alert.setMessage("Vui lòng xác thực tài khoản, trong email của bạn");
                    alert.setIcon(R.drawable.info_ic);

                    alert.setPositiveButton("OK", (dialog, which) -> {
                    });

                    alert.show();
                }
            } else {
                loadingDialog.dismiss();
                Toast.makeText(this, "Tài khoản không đúng!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
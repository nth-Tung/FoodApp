package com.tuantung.oufood.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import io.paperdb.Paper;

public class ChangePasswordActivity extends AppCompatActivity {
    AppCompatButton btn_luu;
    TextInputEditText input_oldPassword, input_newPassword_1, input_newPassword_2;

    TextInputLayout layout_oldPassword, layout_newPassword1, layout_newPassword2;

    String password, password_1, password_2;

    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        buttonBack= findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_luu = findViewById(R.id.btn_luu);
        input_oldPassword = findViewById(R.id.old_password);
        input_newPassword_1 = findViewById(R.id.new_password_1);
        input_newPassword_2 = findViewById(R.id.new_password_2);

        layout_oldPassword = findViewById(R.id.layout_oldPassword);
        layout_newPassword1 = findViewById(R.id.layout_newPassword1);
        layout_newPassword2 = findViewById(R.id.layout_newPassword2);

        btn_luu.setOnClickListener(v -> {
            password = input_oldPassword.getText().toString().trim();
            password_1 = input_newPassword_1.getText().toString().trim();
            password_2 = input_newPassword_2.getText().toString().trim();

            if (isNotError()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(Common.currentUser.getEmail(), password);

                    user.reauthenticate(credential).addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            user.updatePassword(password_1).addOnCompleteListener(task1 -> {
                                if (task1.isSuccessful()) {
                                    Paper.init(ChangePasswordActivity.this);
                                    Paper.book().write(Common.PASSWORD_KEY, password_1);
//                                    CuteToast.ct(getContext(), "Mật khẩu đã được thay đổi", CuteToast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                                    Toast.makeText(ChangePasswordActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
//                                    CuteToast.ct(getContext(), "Đã xảy ra lỗi, hãy thử lại", CuteToast.LENGTH_SHORT, CuteToast.ERROR, true).show();
                                    Toast.makeText(ChangePasswordActivity.this, "Đã xảy ra lỗi, hãy thử lại", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            }

        });
    }

    private boolean isNotError() {

        if (password.isEmpty()) {
            layout_oldPassword.setError("isBlank");
            return false;
        } else {
            layout_oldPassword.setError(null);
        }

        if (password_1.isEmpty()) {
            layout_newPassword1.setError("isBlank");
            return false;
        } else {
            layout_newPassword1.setError(null);
        }

        if (password_2.isEmpty()) {
            layout_newPassword2.setError("isBlank");
            return false;
        } else {
            layout_newPassword2.setError(null);
        }

        if (password_1.equals(password)) {
            layout_newPassword1.setError("Trùng với mật khẩu cũ");
            return false;
        } else {
            layout_newPassword1.setError(null);
        }

        if (!password_1.equals(password_2)) {
            layout_newPassword2.setError("Xác nhập không đúng");
            return false;
        } else {
            layout_newPassword2.setError(null);
        }

        return true;
    }
}
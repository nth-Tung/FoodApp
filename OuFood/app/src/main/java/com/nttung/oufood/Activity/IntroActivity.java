package com.nttung.oufood.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Class.User;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.util.Objects;

import io.paperdb.Paper;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Paper.init(this);
        String user = Paper.book().read(Common.USERNAME_KEY);
        String password = Paper.book().read(Common.PASSWORD_KEY);

        if (user != null && password != null && !user.isEmpty() && !password.isEmpty()) {
            login(user, password);
        } else {
            startActivity(new Intent(IntroActivity.this, SignInActivity.class));
            finish();
        }
    }

    void login(String email, String password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener(IntroActivity.this, task -> {
            if (task.isSuccessful()) {
                String userUid = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
                Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(userUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Common.CURRENTUSER = snapshot.getValue(User.class);
                        startActivity(new Intent(IntroActivity.this, HomeActivity.class));
                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
            else{
                Paper.book().destroy();
                startActivity(new Intent(IntroActivity.this, SignInActivity.class));
                finish();
            }
        });
    }
}
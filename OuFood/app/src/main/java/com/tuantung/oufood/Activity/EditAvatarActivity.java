package com.tuantung.oufood.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.HashMap;
import java.util.Map;

public class EditAvatarActivity extends AppCompatActivity {
    static final int PIC_REQ = 1;
    Uri uri;

    ImageView pic;
    TextView btn_luu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        initConfig();

        btn_luu = findViewById(R.id.btn_luu);
        btn_luu.setOnClickListener(v -> {
            if (uri == null) {
                return;
            }
            MediaManager.get().upload(uri).callback(new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    Common.currentUser.setUrl((String) resultData.get("url"));
                    Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.currentUser.getIdUser()).child("url").setValue(Common.currentUser.getUrl());
                    finish();
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                }
            }).dispatch();

        });

        pic = findViewById(R.id.pic);
        pic.setOnClickListener(v -> {
            requestPermissions();
        });

    }

    private void initConfig() {
        Map config = new HashMap();
        config.put("cloud_name", "dv2zyrxsv");
        config.put("api_key", "564718357889346");
        config.put("api_secret", "TfxQE6I3edoX7yeQrglD0avshDQ");
        MediaManager.init(EditAvatarActivity.this, config);
    }



    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(EditAvatarActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            selectPic();
        } else {
            ActivityCompat.requestPermissions((Activity) EditAvatarActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PIC_REQ);
        }
    }

    private void selectPic() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PIC_REQ);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PIC_REQ && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            uri = data.getData();
            Picasso.get().load(uri).into(pic);
        }
    }
}
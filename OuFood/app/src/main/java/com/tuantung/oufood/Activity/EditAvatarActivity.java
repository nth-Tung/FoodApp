package com.tuantung.oufood.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.squareup.picasso.Picasso;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.Map;

public class EditAvatarActivity extends AppCompatActivity {
    static final int PIC_REQ = 1;
    Uri uri;

    ImageView pic;
    private AppCompatButton btn_luu;
    private ImageView buttonChangeImage;
    private ImageView buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);

        //Load Image
        pic = findViewById(R.id.pic);
        Picasso.get().load(Common.currentUser.getUrl()).into(pic);

        //Change Image
        buttonChangeImage= findViewById(R.id.button_changeImage);
        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermissions();
                selectPic();
            }
        });

        //button back
        buttonBack= findViewById(R.id.button_back);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //Button save
        btn_luu = findViewById(R.id.btn_luu);
        btn_luu.setOnClickListener(v -> {
            if (uri == null) {
                Toast.makeText(this, "Chưa chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
            else {
                MediaManager.get().upload(uri).callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {
                        Toast.makeText(EditAvatarActivity.this, "Đang lưu!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {

                    }

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        Common.currentUser.setUrl((String) resultData.get("secure_url"));
                        Common.FIREBASE_DATABASE.getReference(Common.REF_USERS).child(Common.currentUser.getIdUser()).child("url").setValue(Common.currentUser.getUrl());
                        Toast.makeText(EditAvatarActivity.this, "Lưu thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {

                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {

                    }
                }).dispatch();
            }
        });



    }




    private void requestPermissions() {
        if (ContextCompat.checkSelfPermission(EditAvatarActivity.this, android.Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
            selectPic();
        } else {
            ActivityCompat.requestPermissions( EditAvatarActivity.this, new String[]{android.Manifest.permission.READ_MEDIA_IMAGES}, PIC_REQ);
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
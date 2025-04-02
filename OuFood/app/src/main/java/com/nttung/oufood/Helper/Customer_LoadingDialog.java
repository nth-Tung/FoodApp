package com.nttung.oufood.Helper;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.nttung.oufood.R;

public class Customer_LoadingDialog extends Dialog {
    private String message;
    private TextView tvMessage;

    public Customer_LoadingDialog(@NonNull Context context, String message) {
        super(context);
        this.message = message;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_loading_dialog);

        setCancelable(false);

        // Áp dụng nền trong suốt
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        // Gán tin nhắn nếu có
        tvMessage = findViewById(R.id.tv_message);
        if (message != null && !message.isEmpty()) {
            tvMessage.setText(message);
        }
    }
}

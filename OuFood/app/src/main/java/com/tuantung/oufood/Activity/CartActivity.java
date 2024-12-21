package com.tuantung.oufood.Activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tuantung.oufood.Adapter.CartAdapter;
import com.tuantung.oufood.Class.AnAddress;
import com.tuantung.oufood.Class.Order;
import com.tuantung.oufood.Class.Request;
import com.tuantung.oufood.Class.Ward;
import com.tuantung.oufood.Database.Database;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.AddressType;
import com.tuantung.oufood.common.Common;

import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    Database database;
    Toolbar toolbar;
    TextView textViewBasketTotal;
    TextView textViewDelivery;
    TextView textViewDiscount;
    TextView textViewTotal_a;
    TextView textViewTotal_b;
    TextView textViewNoData;
    AppCompatButton buttonOrder;

    List<Order> cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        database = new Database(CartActivity.this);

        textViewBasketTotal = findViewById(R.id.basketTotal);
        textViewDelivery = findViewById(R.id.delivery);
        textViewDiscount = findViewById(R.id.discount);
        textViewTotal_a = findViewById(R.id.total_a);
        textViewTotal_b = findViewById(R.id.total_b);
        textViewNoData = findViewById(R.id.no_data);


        toolbar = findViewById(R.id.toolbar_Cart);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        loadListCart();

        buttonOrder = findViewById(R.id.btn_order);
        buttonOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickBtn_order();
            }
        });

    }

    private void loadListCart() {
        cart = database.getCarts();
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
        RecyclerView.Adapter adapter = new CartAdapter(cart);
        recyclerView.setAdapter(adapter);

        updateBill();

        if (adapter.getItemCount() > 0) {
            textViewNoData.setVisibility(View.GONE);
        } else {
            textViewNoData.setVisibility(View.VISIBLE);
        }

    }

    public void updateBill() {
        try (Database database = new Database(getBaseContext())) {
            List<Order> orders = database.getCarts();
            double basketTotal = 0; // Tổng giá trị
            double delivery = 10000;
            double discount = 0; // Giảm giá
            if (!orders.isEmpty()) {
                for (Order order : orders) {
                        int price = Integer.parseInt(order.getPrice());
                        int quantity = Integer.parseInt(order.getQuantity());
                        int itemTotal = price * quantity;
                        basketTotal += itemTotal;

                        // Thêm logic giảm giá (nếu có discount)
                        discount += itemTotal * Integer.parseInt(order.getDiscount()) / 100.0; // Ví dụ giảm giá theo %

                }
                textViewBasketTotal.setText(String.valueOf(basketTotal));
                textViewDelivery.setText(String.valueOf(delivery));
                textViewDiscount.setText(String.valueOf(discount));
                double total = (basketTotal + delivery) - discount;
                textViewTotal_a.setText(String.valueOf(total));
                textViewTotal_b.setText(String.valueOf(total));
            }
        }
    }

    private void clickBtn_order() {
//        if (diaChi == null) {
//            CuteToast.ct(getBaseContext(), "Hãy chọn địa chỉ", Toast.LENGTH_SHORT, CuteToast.WARN, true).show();
//        } else {
                String id = String.valueOf(System.currentTimeMillis());
                Request request = new Request(id, Common.currentUser.getIdUser(), textViewTotal_a.getText().toString(), cart, new AnAddress("OU", false, "Tùng", "0912458796", AddressType.HOME, new Ward("70", "fsf", "fds", "fsd")), "0");
                scheduleNotification(Common.DELAY_TIME, id);
                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(id).setValue(request);
                database.cleanCart();
//            CuteToast.ct(getBaseContext(), "Thank you", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
                Toast.makeText(this, "Đặt hàng thành công!", Toast.LENGTH_SHORT).show();
                finish();
//        }
    }

    private void scheduleNotification(int delayTime, String idRequest) {
        Intent notificationIntent = new Intent(CartActivity.this, Notification.class);

        notificationIntent.putExtra("idRequest", idRequest);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(CartActivity.this, Common.NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + delayTime; // Thời gian kích hoạt
            alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);

        }

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
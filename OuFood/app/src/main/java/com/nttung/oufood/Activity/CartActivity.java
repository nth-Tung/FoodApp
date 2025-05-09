package com.nttung.oufood.Activity;

import android.app.AlarmManager;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nttung.oufood.Adapter.CartAdapter;
import com.nttung.oufood.Class.AnAddress;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.Class.Request;
import com.nttung.oufood.Class.Ward;
import com.nttung.oufood.Database.Database;
import com.nttung.oufood.Helper.Notification;
import com.nttung.oufood.R;
import com.nttung.oufood.Helper.AddressType;
import com.nttung.oufood.common.Common;

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

    CartAdapter adapter;


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

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    public void loadListCart() {
        cart = database.getCarts();
        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this, RecyclerView.VERTICAL, false));
        adapter = new CartAdapter(cart);
        recyclerView.setAdapter(adapter);

        updateBill();

        if (adapter.getItemCount() > 0) {
            textViewNoData.setVisibility(View.GONE);
        } else {
            textViewNoData.setVisibility(View.VISIBLE);
        }

    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(CartActivity.this);
            builder.setTitle("Delete a requests?");
            builder.setMessage("Are you sure?");
            builder.setPositiveButton("Yes", (dialog, which) -> {
                int position = viewHolder.getAdapterPosition();
                adapter.removeOrder(position);
                adapter.notifyItemRemoved(position);
                updateBill();

                if (adapter.getItems() == null || adapter.getItems().isEmpty()) {
                    textViewNoData.setVisibility(View.VISIBLE);
                } else {
                    textViewNoData.setVisibility(View.GONE);
                }

            });

            builder.setNegativeButton("No", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()));

            builder.show();
        }
    };

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
                Request request = new Request(id, Common.CURRENTUSER.getIdUser(), textViewTotal_a.getText().toString(), cart, new AnAddress("OU", false, "Tùng", "0912458796", AddressType.HOME, new Ward("70", "fsf", "fds", "fsd")), "0");
                scheduleNotification(Common.DELAY_TIME, id);
                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(id).setValue(request);
                database.cleanCart();
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
            long triggerTime = System.currentTimeMillis() + delayTime;
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
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
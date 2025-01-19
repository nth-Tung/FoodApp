package com.nttung.oufood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Activity.RatingFoodActivity;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.Class.Request;
import com.nttung.oufood.Database.Database;
import com.nttung.oufood.Fragment.OrderDetailFragment;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.util.ArrayList;
import java.util.List;

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder> {
    private List<Request> items;
    private Context context;

    public MyOrderAdapter (ArrayList<Request> items){
        this.items = items;
    }

    public List<Request> getItems() {
        return items;
    }

    public void setItems(List<Request> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_my_order,parent, false);
        return new ViewHolder(inflate);
    }


    @Override
    public void onBindViewHolder(@NonNull MyOrderAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        if (items.get(position).getStatus().equals("0")) { //ongoing
            holder.textViewStatus.setText("Đang giao");
            holder.buttonLayoutHistory.setVisibility(View.GONE);
        } else { // history
            holder.buttonLayoutOngoing.setVisibility(View.GONE);
            holder.textViewStatus.setText("Đã giao");
            holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.blue));

            for (Order order : items.get(position).getOrders()) {
                if (!order.getIsRate()) {
                    holder.buttonRate.setVisibility(View.VISIBLE);
                    break;
                }
            }

            if (items.get(position).getStatus().equals("-1")) {
                holder.textViewStatus.setText("Đã hủy");
                holder.textViewStatus.setTextColor(ContextCompat.getColor(context, R.color.red));
                holder.buttonRate.setVisibility(View.GONE);
            }
        }

        holder.textViewOrderId.setText(items.get(position).getIdRequest());
        holder.textViewToTal.setText(items.get(position).getTotal());
        holder.textViewTime.setText("TIME");

        int countItems = (items.get(position).getOrders()).size();
        holder.textViewCountItems.setText(String.valueOf(countItems));


        holder.buttonRate.setOnClickListener(v -> {

            Intent intent = new Intent(context, RatingFoodActivity.class);
            intent.putExtra("idRequest", items.get(position).getIdRequest());
            context.startActivity(intent);

        });

        holder.buttonReOrder.setOnClickListener(v -> {

            List<Order> orders = items.get(position).getOrders();

            for (Order order : orders) {
                order.setCountStars(5);
                order.setIsRate(false);
                Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(order.getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Food food = snapshot.getValue(Food.class);
                        order.setDiscount(food.getDiscount());
                        order.setPrice(food.getPrice());
                        new Database(context).addToCart(order);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            Toast.makeText(context, "Đã thêm vào giõ hàng", Toast.LENGTH_SHORT).show();

        });

        holder.buttonCancel.setOnClickListener(v -> {
            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(items.get(position).getIdRequest()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("Warming");
//                            builder.setIcon()
                        builder.setMessage("Bạn chắc chắn muốn hủy đơn hàng này?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(items.get(position).getIdRequest()).child("status").setValue("-1");
                            }
                        });

                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });

                        builder.show();


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        });

        holder.itemView.setOnClickListener(v -> {
            OrderDetailFragment fragment = new OrderDetailFragment(items.get(position).getIdRequest());
            AppCompatActivity activity = (AppCompatActivity) context;
            fragment.show(activity.getSupportFragmentManager(), "OrderDetailFragment");
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewStatus;
        private TextView textViewOrderId;
        private TextView textViewCountItems;
        private TextView textViewToTal;
        private TextView textViewTime;
        private AppCompatButton buttonRate;
        private AppCompatButton buttonReOrder;
        private AppCompatButton buttonCancel;
        private LinearLayout buttonLayoutHistory;
        private LinearLayout buttonLayoutOngoing;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStatus = itemView.findViewById(R.id.status);
            textViewOrderId = itemView.findViewById(R.id.order_id);
            textViewCountItems = itemView.findViewById(R.id.countItems);
            textViewToTal = itemView.findViewById(R.id.total);
            textViewTime = itemView.findViewById(R.id.time);
            buttonRate = itemView.findViewById(R.id.rate);
            buttonReOrder = itemView.findViewById(R.id.reOrder);
            buttonCancel = itemView.findViewById(R.id.cancel);
            buttonLayoutHistory = itemView.findViewById(R.id.buttonLayoutHistory);
            buttonLayoutOngoing = itemView.findViewById(R.id.buttonLayoutOngoing);
        }
    }
}

package com.nttung.oufood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.nttung.oufood.Activity.FoodDetailActivity;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    private List<Order> items;
    private Context context;

    public OrderDetailAdapter(List<Order> items){
        this.items = items;
    }

    public List<Order> getItems() {
        return items;
    }

    public void setItems(List<Order> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View inflate = LayoutInflater.from(context).inflate(R.layout.item_order_detail, parent, false);
       return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        holder.name.setText(items.get(position).getProductName());
        holder.price.setText(items.get(position).getPrice());
        holder.quantity.setText(items.get(position).getQuantity());
        holder.discount.setText(items.get(position).getDiscount());

        holder.pic.setOnClickListener(v -> {
            Intent foodDetail = new Intent(context, FoodDetailActivity.class);
            foodDetail.putExtra("FoodId", items.get(position).getProductId());
            context.startActivity(foodDetail);
        });

        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(items.get(position).getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Food food = snapshot.getValue(Food.class);
                    Picasso.get().load(food.getUrl()).fit().centerCrop().into(holder.pic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private TextView name;
        private TextView price;
        private TextView quantity;
        private TextView discount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic = itemView.findViewById(R.id.image);
            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price_before_discount);
            quantity = itemView.findViewById(R.id.quantity);
            discount = itemView.findViewById(R.id.discount);

        }
    }
}

package com.nttung.oufood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.nttung.oufood.Activity.CartActivity;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.Database.Database;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.Viewholder> {
    List<Order> items;
    Context context;

    public CartAdapter(List<Order> items) {
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
    public CartAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new Viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(items.get(position).getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);
                String pathURL = currentFood.getUrl();
                Picasso.get().load(pathURL).fit().centerCrop().into(holder.pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.textViewName.setText(items.get(position).getProductName());

        String priceString = items.get(position).getPrice();
        double price = Double.parseDouble(priceString);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(price);
        holder.textViewPrice.setText(formattedPrice);


        holder.textViewQuantity.setText(items.get(position).getQuantity());



        holder.buttonIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
                quantity += 1;
                holder.textViewQuantity.setText(String.valueOf(quantity));
                items.get(position).setQuantity(String.valueOf(quantity));
                new Database(context).updateQuantity(items.get(position).getProductId(), holder.textViewQuantity.getText().toString());

                ((CartActivity) context).updateBill();
            }
        });

        holder.buttonDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = Integer.parseInt(holder.textViewQuantity.getText().toString());
                if (quantity > 1) {
                    quantity -= 1;
                    new Database(context).updateQuantity(items.get(position).getProductId(), holder.textViewQuantity.getText().toString());
//                    update quantiry to quantity_cart_item
                    holder.textViewQuantity.setText(String.valueOf(quantity));
//                    update quantity to list_order
                    items.get(position).setQuantity(String.valueOf(quantity));
                    ((CartActivity) context).updateBill();
                } else {
                    if (quantity == 1) {
                        new Database(context).removeItems(items.get(position));
                        ((CartActivity) context).loadListCart();
                    }
                }
            }
        });
    }

    public void removeOrder(int pos) {
        items.remove(pos);
        new Database(context).cleanCart();
        Database database = new Database(context);
        database.add_list_to_cart(items);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView pic;
        TextView textViewQuantity;
        TextView textViewName;
        TextView textViewPrice;
        AppCompatButton buttonIncrease;
        AppCompatButton buttonDecrease;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.pic);
            textViewQuantity = itemView.findViewById(R.id.textView_quantity);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewPrice = itemView.findViewById(R.id.textView_price);
            buttonIncrease = itemView.findViewById(R.id.button_increase_cart);
            buttonDecrease = itemView.findViewById(R.id.button_decrease_cart);
        }
    }
}

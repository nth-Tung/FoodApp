package com.tuantung.oufood.Adapter;

import android.content.Context;
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
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.Class.Order;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.viewholder> {
    List<Order> items;
    Context context;
//    private ManagmentCart managmentCart;
//    ChangeNumberItemsListener changeNumberItemsListener;

//    public CartAdapter(ArrayList<Foods> list, Context context, ChangeNumberItemsListener changeNumberItemsListener){
//        this.list = list;
//        managmentCart = new ManagmentCart(context);
//        this.changeNumberItemsListener = changeNumberItemsListener;
//    }

    public CartAdapter(List<Order> items){
        this.items = items;
    }

    @NonNull
    @Override
    public CartAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_cart,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.viewholder holder, int position) {
        holder.textViewName.setText(items.get(position).getProductName());
        holder.textViewQuantity.setText(items.get(position).getQuantity()+"");

        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(items.get(position).getProductId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food currentFood = snapshot.getValue(Food.class);
                String pathURL = currentFood.getURL();
                Picasso.get().load(pathURL).into(holder.pic);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        holder.buttonPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notifyDataSetChanged();
//            }
//        });
//
//        holder.buttonMinus.setOnClickListener(v -> {
//            managmentCart.minusNumberItem(list, position, () -> {
//                notifyDataSetChanged();
//                changeNumberItemsListener.change();
//            });
//        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder{
        TextView textViewName;
        TextView textViewQuantity;
        TextView buttonIncrease;
        TextView buttonDecrease;
        ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);

            textViewName = itemView.findViewById(R.id.textView_name);
            textViewQuantity = itemView.findViewById(R.id.textView_quantity);
            buttonIncrease = itemView.findViewById(R.id.button_increase);
            buttonDecrease = itemView.findViewById(R.id.button_Decrease);
            pic = itemView.findViewById(R.id.pic);
        }
    }
}

package com.nttung.oufood.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.nttung.oufood.Activity.FoodDetailActivity;
import com.nttung.oufood.Class.Food;
import com.nttung.oufood.Class.Order;
import com.nttung.oufood.Database.Database;
import com.nttung.oufood.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SaleListAdapter extends RecyclerView.Adapter<SaleListAdapter.viewholder>{
    List<Food> items;
    Context context;

    public SaleListAdapter(List<Food> items){
        this.items = items;
    }
    @NonNull
    @Override
    public SaleListAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food_sale,parent,false);
        return new viewholder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull SaleListAdapter.viewholder holder, int position) {
        holder.textViewName.setText(items.get(position).getName());

        String priceString = items.get(position).getPrice();
        double price = Double.parseDouble(priceString);
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(price);
        holder.textViewPrice.setText(formattedPrice);

        holder.textViewDiscount.setText(items.get(position).getDiscount()+"%\nOFF");

        if(items.get(position).getCountRating()>0){
            holder.textViewStar.setText(String.format("%.1f",(double) items.get(position).getCountStars()/items.get(position).getCountRating()));
        }
        else{
            holder.textViewStar.setText("5");
        }

        Picasso.get()
                .load(items.get(position).getUrl()) // URL của ảnh
                .fit() // Tự động điều chỉnh kích thước ảnh để khớp với ImageView
                .centerCrop() // Cắt và điều chỉnh trung tâm ảnh
                .into(holder.pic); // ImageView của bạn

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodDetailActivity.class);
            intent.putExtra("FoodId", items.get(position).getId());
            context.startActivity(intent);
        });

        holder.textViewPlus.setOnClickListener(v -> {
//            CuteToast.ct(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT, CuteToast.SUCCESS, true).show();
            Toast.makeText(context, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            Order order = new Order(items.get(position).getId(), items.get(position).getName(), items.get(position).getPrice(), "1", items.get(position).getDiscount());
            Database database1 = new Database(context);
            database1.addToCart(order);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewPrice;
        private TextView textViewStar;
        private TextView textViewDiscount;
        private TextView textViewPlus;

        private ImageView pic;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textView_name);
            textViewPrice = itemView.findViewById(R.id.textView_price);
            textViewStar = itemView.findViewById(R.id.textView_star);
            textViewDiscount = itemView.findViewById(R.id.textView_discount);
            textViewPlus = itemView.findViewById(R.id.textView_plus);

            pic = itemView.findViewById(R.id.pic);
        }
    }
}
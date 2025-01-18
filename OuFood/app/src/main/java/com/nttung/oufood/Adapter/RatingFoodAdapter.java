package com.nttung.oufood.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class RatingFoodAdapter extends RecyclerView.Adapter<RatingFoodAdapter.ViewHolder> {
    private List<Order> items;
    private Context context;

    public RatingFoodAdapter(List<Order> items){
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
    public RatingFoodAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       context = parent.getContext();
       View inflate = LayoutInflater.from(context).inflate(R.layout.item_rating_food, parent, false);
       return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingFoodAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Common.FIREBASE_DATABASE.getReference(Common.REF_FOODS).child(items.get(position).getProductId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Food food = snapshot.getValue(Food.class);
                Picasso.get().load(food.getUrl()).fit().centerCrop().into(holder.imageFood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        holder.nameOfFood.setText(items.get(position).getProductName());
        holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int int_rating = (int) rating;

                String s;
                int color;

                switch (int_rating) {
                    case 1:
                        s = "Chất lượng sản phẩm:\nTệ";
                        color = context.getResources().getColor(R.color.ratingVeryBad);
                        break;
                    case 2:
                        s = "Chất lượng sản phẩm:\nKhông hài lòng";
                        color = context.getResources().getColor(R.color.ratingBad);
                        break;
                    case 3:
                        s = "Chất lượng sản phẩm:\nBình thường";
                        color = context.getResources().getColor(R.color.ratingNeutral);
                        break;
                    case 4:
                        s = "Chất lượng sản phẩm:\nHài lòng";
                        color = context.getResources().getColor(R.color.ratingGood);
                        break;
                    default:
                        s = "Chất lượng sản phẩm:\nTuyệt vời";
                        color = context.getResources().getColor(R.color.ratingVeryGood);
                        break;
                }
                items.get(position).setCountStars(int_rating);
                holder.ratingText.setText(s);
                holder.ratingBar.setProgressTintList(ColorStateList.valueOf(color));
                holder.ratingBar.setProgressBackgroundTintList(ColorStateList.valueOf(context.getResources().getColor(R.color.ratingDefault, null)));
                holder.ratingBar.setRating(int_rating);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("FoodId", items.get(position).getProductId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{
        private ImageView imageFood;
        private TextView nameOfFood;
        private TextView ratingText;
        private RatingBar ratingBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageFood = itemView.findViewById(R.id.image_food);
            nameOfFood = itemView.findViewById(R.id.name_of_food);
            ratingText = itemView.findViewById(R.id.ratingText);
            ratingBar = itemView.findViewById(R.id.ratingBar);

        }
    }
}

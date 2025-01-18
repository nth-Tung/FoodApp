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

import com.squareup.picasso.Picasso;
import com.nttung.oufood.Activity.FoodListActivity;
import com.nttung.oufood.Class.Category;
import com.nttung.oufood.R;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.viewHolder> {
    List<Category> items;
    Context context;

    public CategoryAdapter(List<Category> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CategoryAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category,parent,false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.viewHolder holder, int position) {
        holder.mTextViewName.setText(items.get(position).getName());

        Picasso.get()
                .load(items.get(position).getURL()) // URL của ảnh
                .fit() // Tự động điều chỉnh kích thước ảnh để khớp với ImageView
                .centerCrop() // Cắt và điều chỉnh trung tâm ảnh
                .into(holder.pic); // ImageView của bạn

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FoodListActivity.class);
            intent.putExtra("categoryId",items.get(position).getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        private ImageView pic;
        private TextView mTextViewName;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            pic = itemView.findViewById(R.id.image_category);
            mTextViewName= itemView.findViewById(R.id.textView_nameCategory);
        }
    }
}
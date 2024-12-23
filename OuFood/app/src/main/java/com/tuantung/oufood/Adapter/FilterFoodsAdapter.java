package com.tuantung.oufood.Adapter;

import android.annotation.SuppressLint;
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
import com.tuantung.oufood.Activity.FoodDetailActivity;
import com.tuantung.oufood.Class.Food;
import com.tuantung.oufood.R;

import java.util.ArrayList;
import java.util.List;

public class FilterFoodsAdapter extends RecyclerView.Adapter<FilterFoodsAdapter.ViewHolder> {
    List<Food> items;
    Context context;

    public FilterFoodsAdapter(ArrayList<Food> items){
        this.items = items;
    }

    public List<Food> getItems() {
        return items;
    }

    public void setItems(ArrayList<Food> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public FilterFoodsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_filter_foods,parent,false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterFoodsAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Picasso.get().load(items.get(position).getUrl()).fit().centerCrop().into(holder.pic);
        holder.name.setText(items.get(position).getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetailActivity.class);
                intent.putExtra("FoodId", items.get(position).getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pic;
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            pic= itemView.findViewById(R.id.pic);
            name = itemView.findViewById(R.id.name);
        }
    }
}

package com.tuantung.oufood.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tuantung.oufood.Class.AnAddress;
import com.tuantung.oufood.R;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {
    private List<AnAddress> items;
    private Context context;

    AddressAdapter(List<AnAddress> items) {
        this.items = items;
    }

    public void setItems(List<AnAddress> items) {
        this.items = items;
    }

    public List<AnAddress> getItems() {
        return items;
    }

    @NonNull
    @Override
    public AddressAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_address, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull AddressAdapter.ViewHolder holder, int position) {
        holder.textView_typeAddress.setText(items.get(position).getTypeAddress().toString());
        holder.textView_address.setText(items.get(position).getAddress());
        holder.textView_name.setText(items.get(position).getName());
        holder.textView_phoneNumber.setText(items.get(position).getPhone());
        holder.textView_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_typeAddress, textView_update, textView_address, textView_name, textView_phoneNumber;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_typeAddress = itemView.findViewById(R.id.textView_typeAddress);
            textView_update = itemView.findViewById(R.id.textView_update);
            textView_address = itemView.findViewById(R.id.textView_address);
            textView_name = itemView.findViewById(R.id.textView_name);
            textView_phoneNumber = itemView.findViewById(R.id.textView_phoneNumber);
        }
    }
}

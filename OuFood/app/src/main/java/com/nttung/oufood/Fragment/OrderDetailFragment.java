package com.nttung.oufood.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.nttung.oufood.Adapter.OrderDetailAdapter;
import com.nttung.oufood.Class.Request;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;


public class OrderDetailFragment extends BottomSheetDialogFragment {

    TextView username, phone, address, total, idRequest, paymentMethod, orderTime, status;
    AppCompatButton buttonDone;
    RecyclerView recyclerOrderDetail;

    OrderDetailAdapter adapter;

    private String id;

    public OrderDetailFragment(String idRequest) {
        id = idRequest;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_detail, container, false);

        username = view.findViewById(R.id.username);
        phone = view.findViewById(R.id.phone);
        address = view.findViewById(R.id.address);
        total = view.findViewById(R.id.total);
        idRequest = view.findViewById(R.id.idRequest);
        paymentMethod = view.findViewById(R.id.paymentMethod);
        orderTime = view.findViewById(R.id.orderTime);
        buttonDone = view.findViewById(R.id.buttonDone);
        recyclerOrderDetail = view.findViewById(R.id.recyclerOrderDetail);
        status = view.findViewById(R.id.status);

        idRequest.setText(id);

        Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Request request = snapshot.getValue(Request.class);
                    username.setText(request.getAnAddress().getName());
                    phone.setText(request.getAnAddress().getPhone());
                    address.setText(request.getAnAddress().getAddress());
                    total.setText(request.getTotal());

                    status.setText(getStringStatus(request.getStatus()));

                    adapter = new OrderDetailAdapter(request.getOrders());
                    recyclerOrderDetail.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL, false));
                    recyclerOrderDetail.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonDone.setOnClickListener(v -> {
            dismiss();
        });

        return view;
    }

    private String getStringStatus(String sta) {

        if (sta.equals("1")) {
            return "Đã giao";
        }

        if (sta.equals("-1")) {
            return "Đã hủy";
        }

        return "Đang giao";
    }
}
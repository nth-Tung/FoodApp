package com.tuantung.oufood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.tuantung.oufood.Activity.ChangePasswordActivity;
import com.tuantung.oufood.Activity.EditAvatarActivity;
import com.tuantung.oufood.Activity.EditNameUserActivity;
import com.tuantung.oufood.Activity.EditPhoneUserActivity;
import com.tuantung.oufood.Activity.ForgotPasswordActivity;
import com.tuantung.oufood.Activity.SignInActivity;
import com.tuantung.oufood.Activity.SignUpActivity;
import com.tuantung.oufood.Class.User;
import com.tuantung.oufood.R;
import com.tuantung.oufood.common.Common;

import io.paperdb.Paper;


public class AccountFragment extends Fragment {

    private TextView name, phone, email, logout;

    private ImageView pic;

    private ImageView ic_next_name, ic_next_phone, ic_next_password;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);


        pic = view.findViewById(R.id.pic);
        name = view.findViewById(R.id.name);
        phone = view.findViewById(R.id.phone);
        email = view.findViewById(R.id.email);
        logout = view.findViewById(R.id.button_logout);

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.currentUser = null;
                FirebaseAuth.getInstance().signOut();
                Paper.clear(requireActivity());
                startActivity(new Intent(requireActivity(), SignInActivity.class));
                requireActivity().finish();
            }
        });


        if (!Common.currentUser.getUrl().equals(" "))
            Picasso.get().load(Common.currentUser.getUrl()).into(pic);

        pic.setOnClickListener(v -> {
           startActivity(new Intent(requireActivity(), EditAvatarActivity.class));
        });

        name.setText(Common.currentUser.getName());
        String sPhone = Common.currentUser.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);

        String s = Common.currentUser.getEmail();
        int index = s.indexOf("@");
        String sEmail = s.substring(0, 1) + "*****" + s.substring(index - 1);

        email.setText(sEmail);

        ic_next_name = view.findViewById(R.id.ic_next_name);
        ic_next_name.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditNameUserActivity.class));
        });

        ic_next_phone = view.findViewById(R.id.ic_next_phone);
        ic_next_phone.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), EditPhoneUserActivity.class));
        });

        ic_next_password = view.findViewById(R.id.ic_next_password);
        ic_next_password.setOnClickListener(v -> {
            startActivity(new Intent(requireActivity(), ChangePasswordActivity.class));
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        phone.setText(Common.currentUser.getPhone());
        name.setText(Common.currentUser.getName());

    }
}
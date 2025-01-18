package com.nttung.oufood.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;
import com.nttung.oufood.Activity.ChangePasswordActivity;
import com.nttung.oufood.Activity.EditAvatarActivity;
import com.nttung.oufood.Activity.EditNameUserActivity;
import com.nttung.oufood.Activity.EditPhoneUserActivity;
import com.nttung.oufood.Activity.SignInActivity;
import com.nttung.oufood.R;
import com.nttung.oufood.common.Common;

import io.paperdb.Paper;


public class AccountFragment extends Fragment {

    private TextView name, phone, email, logout;

    private ImageView pic;

    private ImageView ic_next_name, ic_next_phone, ic_next_password;

    private ImageView buttonChangeImage;


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
        buttonChangeImage=view.findViewById(R.id.button_changeImage);

        //logout
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CURRENTUSER = null;
                FirebaseAuth.getInstance().signOut();
                Paper.clear(requireActivity());
                startActivity(new Intent(requireActivity(), SignInActivity.class));
                requireActivity().finish();
            }
        });


        if (!Common.CURRENTUSER.getUrl().equals(" ")) {
            Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
        }
        buttonChangeImage.setOnClickListener(v -> {
           startActivity(new Intent(requireActivity(), EditAvatarActivity.class));
        });

        name.setText(Common.CURRENTUSER.getName());
        String sPhone = Common.CURRENTUSER.getPhone();
        sPhone = "*******" + sPhone.substring(sPhone.length() - 3);
        phone.setText(sPhone);

        String s = Common.CURRENTUSER.getEmail();
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
        phone.setText(Common.CURRENTUSER.getPhone());
        name.setText(Common.CURRENTUSER.getName());
        Picasso.get().load(Common.CURRENTUSER.getUrl()).into(pic);
    }
}
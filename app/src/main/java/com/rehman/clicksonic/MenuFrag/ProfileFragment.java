package com.rehman.clicksonic.MenuFrag;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.SavedNotification;
import com.rehman.clicksonic.Utils.SharedPref;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    TextView name_text,coins_text,points_text,email_text,loginWith_text;
    ImageView profile_image;


    FirebaseAuth mAuth;
    FirebaseUser user;

    String name,email,coins,points,userUID,loginWith,profileImageLink;

    SavedNotification savedNotification;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();
        savedNotification = new SavedNotification(getActivity());

        initViews(view);

        SharedPreferences preferences = getActivity().getSharedPreferences("USER",Context.MODE_PRIVATE);
        name = preferences.getString("name","");
        email = preferences.getString("email","");
        loginWith = preferences.getString("loginWith","");
        profileImageLink = preferences.getString("profileImageLink","");
        coins = preferences.getString("coins","");
        points = preferences.getString("points","");

        Glide.with(getActivity()).load(profileImageLink).into(profile_image);

        name_text.setText(name);
        coins_text.setText(coins);
        points_text.setText(points);
        email_text.setText(email);
        loginWith_text.setText(loginWith);

        return view;
    }

    private void initViews(View view)
    {
        name_text = view.findViewById(R.id.name_text);
        coins_text = view.findViewById(R.id.coins_text);
        points_text = view.findViewById(R.id.points_text);
        email_text = view.findViewById(R.id.email_text);
        loginWith_text = view.findViewById(R.id.loginWith_text);
        profile_image = view.findViewById(R.id.profile_image);
    }
}
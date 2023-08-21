package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.rehman.clicksonic.R;

public class AdminControlActivity extends AppCompatActivity {
    CardView card_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);
        initView();

        card_user.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, UserListActivity.class));});

    }

    private void initView() {

        card_user =findViewById(R.id.card_user);

    }
}
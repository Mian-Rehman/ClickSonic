package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.rehman.clicksonic.Lists.FacebookListActivity;
import com.rehman.clicksonic.Lists.InstagramListActivity;
import com.rehman.clicksonic.Lists.TikTokActivity;
import com.rehman.clicksonic.Lists.UserListActivity;
import com.rehman.clicksonic.Lists.YouTubeListActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.SubActivity.MakeScratchActivity;

public class AdminControlActivity extends AppCompatActivity {
    CardView card_user,card_youTube,card_instagram,card_facebook,card_tiktok,card_scratch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_control);
        initView();



        card_user.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, UserListActivity.class));});
        card_youTube.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, YouTubeListActivity.class));});
        card_instagram.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, InstagramListActivity.class));});
        card_facebook.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, FacebookListActivity.class));});
        card_tiktok.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, TikTokActivity.class));});
        card_scratch.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, MakeScratchActivity.class));});

    }

    public void onBackPressed() {
        moveTaskToBack(true);// This will move the app to the background
        finish(); // This will close the current activity
        System.exit(0); // This will forcefully close the app's process
    }

    private void initView() {

        card_user =findViewById(R.id.card_user);
        card_youTube =findViewById(R.id.card_youTube);
        card_instagram =findViewById(R.id.card_instagram);
        card_facebook =findViewById(R.id.card_facebook);
        card_tiktok =findViewById(R.id.card_tiktok);
        card_scratch =findViewById(R.id.card_scratch);

    }
}
package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.rehman.clicksonic.Lists.AdminPaymentViewlistActivity;
import com.rehman.clicksonic.Lists.BoughtListActivity;
import com.rehman.clicksonic.Lists.CoinBoughtListActivity;
import com.rehman.clicksonic.Lists.FacebookListActivity;
import com.rehman.clicksonic.Lists.InstagramListActivity;
import com.rehman.clicksonic.Lists.TikTokActivity;
import com.rehman.clicksonic.Lists.UserListActivity;
import com.rehman.clicksonic.Lists.YouTubeListActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.SubActivity.MakeBonusActivity;
import com.rehman.clicksonic.SubActivity.MakeScratchActivity;

public class AdminControlActivity extends AppCompatActivity {
    CardView card_user,card_youTube,card_instagram,card_facebook,card_tiktok,card_bonus,
            card_scratch,money_card,card_bought,card_coin;

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
        money_card.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, AdminPaymentViewlistActivity.class));});
        card_bonus.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, MakeBonusActivity.class));});
        card_bought.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, BoughtListActivity.class));});
        card_coin.setOnClickListener(v -> {startActivity(new Intent(AdminControlActivity.this, CoinBoughtListActivity.class));});

    }

    public void onBackPressed() {
        finish(); // This will close the current activity
    }

    private void initView() {

        card_user =findViewById(R.id.card_user);
        card_youTube =findViewById(R.id.card_youTube);
        card_instagram =findViewById(R.id.card_instagram);
        card_facebook =findViewById(R.id.card_facebook);
        card_tiktok =findViewById(R.id.card_tiktok);
        card_scratch =findViewById(R.id.card_scratch);
        card_bonus =findViewById(R.id.card_bonus);
        money_card =findViewById(R.id.money_card);
        card_bought =findViewById(R.id.card_bought);
        card_coin =findViewById(R.id.card_coin);

    }
}
package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rehman.clicksonic.Adapter.PurchaseTabsAdapter;
import com.rehman.clicksonic.R;

public class BuyCoinActivity extends AppCompatActivity {

//    ,buy_points_layout
    LinearLayout buy_coins_layout;
    TextView buy_coins_text,buy_points_text;
    View buy_coins_view,buy_points_view;
    ImageView back_image;

    PurchaseTabsAdapter adapter;
    ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_coin);

        intiViews();
        fragmentSlider();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });
    }

    private void fragmentSlider()
    {

        adapter = new PurchaseTabsAdapter(this);
        viewPager.setUserInputEnabled(true);
        viewPager.setAdapter(adapter);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
//                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        buy_coins_text.setTextColor(ContextCompat.getColor(this,R.color.main_color));
        buy_coins_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.main_color,this.getTheme()));

        buy_points_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
        buy_points_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,this.getTheme()));

        buy_coins_layout.setOnClickListener(v -> {
            viewPager.setCurrentItem(0,true);
        });



    }

    private void onChangeTab(int position)
    {
    if (position == 0)
        {
            buy_coins_text.setTextColor(ContextCompat.getColor(this,R.color.main_color));
            buy_coins_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.main_color,this.getTheme()));

            buy_points_text.setTextColor(ContextCompat.getColor(this,R.color.gray));
            buy_points_view.setBackgroundColor(ResourcesCompat.getColor(getResources(),R.color.gray,this.getTheme()));
        }
    }

    private void intiViews()
    {
        viewPager = findViewById(R.id.viewPager);

        buy_coins_layout = findViewById(R.id.buy_coins_layout);
        buy_coins_text = findViewById(R.id.buy_coins_text);
        buy_coins_view = findViewById(R.id.buy_coins_view);

//        buy_points_layout = findViewById(R.id.buy_points_layout);
        buy_points_text = findViewById(R.id.buy_points_text);
        buy_points_view = findViewById(R.id.buy_points_view);
        back_image = findViewById(R.id.back_image);
    }
}
package com.rehman.clicksonic.SubActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.rehman.clicksonic.Lists.YouTubeListActivity;
import com.rehman.clicksonic.R;

public class CategorySelectActivity extends AppCompatActivity {

    Button btn_follow,btn_like,btn_view;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catigory);

        initView();
        getIntentValue();

        btn_follow.setOnClickListener(v -> {

            if (name.equals("YouTube")){
                Intent intent = new Intent(CategorySelectActivity.this, YouTubeListActivity.class);
                intent.putExtra("category","follows");
                startActivity(intent);
            }

        });
    }

    private void getIntentValue() {

        name = getIntent().getStringExtra("name");

    }

    private void initView() {

        //Button
        btn_follow=findViewById(R.id.btn_follow);
        btn_like=findViewById(R.id.btn_like);
        btn_view=findViewById(R.id.btn_view);
    }
}
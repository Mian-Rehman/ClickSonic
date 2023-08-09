package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rehman.clicksonic.R;

public class InformationActivity extends AppCompatActivity {

    ImageView top_image,back_image;
    TextView tv_title;
    EditText ed_link,ed_follower,ed_name;
    Button btn_submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        initView();
        instagram();
        back_image.setOnClickListener(v -> { onBackPressed();});

    }

    private void instagram() {

        String name = getIntent().getStringExtra("name");
        int image = getIntent().getIntExtra("image",0);

        tv_title.setText(name);
        top_image.setImageResource(image);
    }

    private void initView() {

        //ImageView
        top_image=findViewById(R.id.top_image);
        back_image=findViewById(R.id.back_image);

        //TextView
        tv_title=findViewById(R.id.tv_title);

        //EditText
        ed_link=findViewById(R.id.ed_link);
        ed_follower=findViewById(R.id.ed_follower);
        ed_name=findViewById(R.id.ed_name);

        //Button
        btn_submit=findViewById(R.id.btn_submit);
    }
}
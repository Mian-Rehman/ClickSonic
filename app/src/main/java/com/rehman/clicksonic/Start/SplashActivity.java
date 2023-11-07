package com.rehman.clicksonic.Start;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.rehman.clicksonic.Activity.AdsTestActivity;
import com.rehman.clicksonic.MainActivity;
import com.rehman.clicksonic.R;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    Animation image_anim;
    ImageView image;
    CardView card;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();

        initViews();

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        image_anim = AnimationUtils.loadAnimation(this,R.anim.zoom_in);
        card.startAnimation(image_anim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                finish();
//                mAuth.signOut();
//                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
//                finish();
//                Toast.makeText(SplashActivity.this, "Sign out", Toast.LENGTH_SHORT).show();
            }
        },3000);

    }

    private void initViews()
    {
        image = findViewById(R.id.image);
        card = findViewById(R.id.card);
    }
}
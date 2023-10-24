package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rehman.clicksonic.Payment.PaymentActivity;
import com.rehman.clicksonic.R;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ScratchActivity extends AppCompatActivity {

    TextView tv_offerName,tv_offerPrice,tv_expire;
    ImageView img_offer,back_image;
    String imageUrl,name,price,expire;
    CardView card_scratch;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        InitView();

        retrieveLatestImage();

        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        card_scratch.setOnClickListener(v -> {
            Intent intent = new Intent(ScratchActivity.this, PaymentActivity.class);
            intent.putExtra("price",price);
            startActivity(intent);
        });

    }

    private void retrieveLatestImage() {
        db.collection("scratchOffers")
                .orderBy("date", Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            imageUrl = document.getString("scratchUrl");
                            name = document.getString("name");
                            expire = document.getString("expire");
                            price = document.getString("price");

                            Glide.with(ScratchActivity.this).load(imageUrl).into(img_offer);
                            tv_offerName.setText(name);
                            tv_expire.setText(expire);
                            tv_offerPrice.setText(price);

                            // Use the imageUrl as needed (e.g., display the image)
                            Log.d("LatestImage", "Latest image URL: " + imageUrl);
                        } else {
                            Log.e("LatestImage", "Error getting latest image", task.getException());
                        }
                    }
                });
    }


    private void InitView() {

        //textView
        tv_offerName = findViewById(R.id.tv_offerName);
        tv_offerPrice = findViewById(R.id.tv_offerPrice);
        tv_expire = findViewById(R.id.tv_expire);
        //ImageView
        img_offer = findViewById(R.id.img_offer);
        back_image = findViewById(R.id.back_image);
        //CardView
        card_scratch = findViewById(R.id.card_scratch);

        db = FirebaseFirestore.getInstance();

    }


}
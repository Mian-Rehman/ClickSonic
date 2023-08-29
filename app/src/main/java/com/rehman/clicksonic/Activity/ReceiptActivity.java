package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;
import com.rehman.clicksonic.Utils.PaymentDialog;

import java.util.HashMap;
import java.util.Map;

public class ReceiptActivity extends AppCompatActivity {

    TextView tv_link,tv_category,tv_amount,tv_fulName,tv_cost,tv_remaining,tv_title;
    Button btn_upload;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String link,fullName,title,type,likes,followers,views,cost,remaining;
    double total,coinsLeft;
    String userUID;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    LoadingBar loadingBar = new LoadingBar(this);
    PaymentDialog paymentDialog =new PaymentDialog(this);

    @Override
    protected void onStart() {
        super.onStart();
        mUser = mAuth.getCurrentUser();
        if (mUser!=null){
            userUID = mUser.getUid();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        mAuth = FirebaseAuth.getInstance();

        initViews();
        getIntentValues();
        setText();
        tv_title.setPaintFlags(tv_title.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);

        btn_upload.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please Wait");
            saveFirebaseData();
        });

    }
    private void saveFirebaseData() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection(title).document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("link", link);
        if (!(followers == null) && (likes == null) && (views == null)){
            map.put(type, followers);
        } else if ((followers == null) && !(likes == null) && (views == null)) {
            map.put("likes", likes);
        }else {
            map.put("views", views);
        }
        total = Double.parseDouble(cost);
        map.put("cost", total);
        coinsLeft = Double.parseDouble(remaining);
        map.put("updatedCoins", coinsLeft);
        map.put("OrderID", id);
        map.put("userUID", userUID);
        map.put("Status", "pending");
        map.put("OrderDate", dateTime.getCurrentDate());
        map.put("OrderTime", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection(title).document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingBar.HideDialog();
                        paymentDialog.ShowDialog();
                        Toast.makeText(ReceiptActivity.this, "Your request has been sent for " + title, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    Toast.makeText(ReceiptActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                });



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userUID);
        userRef.update("coin", coinsLeft)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Coins have been updated", Toast.LENGTH_SHORT).show();
                        loadingBar.HideDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    loadingBar.HideDialog();
                });

    }

    private void setText() {

        tv_title.setText(title);
        tv_link.setText(link);
        if (!(followers == null) && (likes == null) && (views == null)){
            tv_amount.setText(followers);
            tv_category.setText(type+":");
        } else if ((followers == null) && !(likes == null) && (views == null)) {
            tv_amount.setText(likes);
            tv_category.setText("Likes:");
        }else {
            tv_amount.setText(views);
            tv_category.setText("Views:");
        }
        tv_fulName.setText(fullName);
        tv_cost.setText(cost);

    }

    private void getIntentValues() {

        link = getIntent().getExtras().getString("link");
        fullName = getIntent().getExtras().getString("fullName");
        title = getIntent().getExtras().getString("title");
        type = getIntent().getExtras().getString("type");
        followers = getIntent().getExtras().getString("followers");
        likes = getIntent().getExtras().getString("likes");
        views = getIntent().getExtras().getString("views");
        cost = getIntent().getExtras().getString("cost");
        remaining = getIntent().getExtras().getString("updatedCoins");

    }

    private void initViews() {
        //TextView
        tv_link=findViewById(R.id.tv_link);
        tv_category=findViewById(R.id.tv_category);
        tv_amount=findViewById(R.id.tv_amount);
        tv_fulName=findViewById(R.id.tv_fulName);
        tv_cost=findViewById(R.id.tv_cost);
        tv_remaining=findViewById(R.id.tv_remaining);
        tv_title=findViewById(R.id.tv_title);

        //Button
        btn_upload=findViewById(R.id.btn_upload);

    }
}
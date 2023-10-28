package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rehman.clicksonic.Payment.PaymentActivity;
import com.rehman.clicksonic.R;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rehman.clicksonic.Utils.LoadingBar;

public class ScratchActivity extends AppCompatActivity {

    TextView tv_offerName,tv_offerPrice,tv_expire;
    ImageView img_offer,back_image;
    String imageUrl,name,expire,price;
    CardView card_scratch;
    private FirebaseFirestore db;
    LoadingBar loadingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;
    int wallet,amount;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scratch);

        loadingBar = new LoadingBar(this);
        loadingBar.ShowDialog("fetch data");

        InitView();

        retrieveLatestImage();

        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        card_scratch.setOnClickListener(v -> {
            showConfirmationDialog();
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

                            assert price != null;
                            amount= Integer.parseInt(price);


                            Glide.with(ScratchActivity.this).load(imageUrl).into(img_offer);
                            tv_offerName.setText(name);
                            tv_expire.setText(expire);
                            tv_offerPrice.setText(price);
                            loadingBar.HideDialog();

                            // Use the imageUrl as needed (e.g., display the image)
                            Log.d("LatestImage", "Latest image URL: " + imageUrl);
                        } else {
                            Log.e("LatestImage", "Error getting latest image", task.getException());
                        }
                    }
                });
    }
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Action");
        builder.setMessage("Are you sure you want to perform this action?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the action when the user clicks "Yes"
                dialog.dismiss();
                userData();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing when the user clicks "No"
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Add this method to perform the action
    private void performAction() {
        // You can implement the action you want to perform here.
        // For example, start the PaymentActivity.
        Intent intent = new Intent(ScratchActivity.this, PaymentActivity.class);
        startActivity(intent);
    }
    private void userData()
    {
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                wallet = snapshot.getLong("wallet").intValue();

                                if ( amount <= wallet){
                                    double newAmount = wallet - amount;
                                    updateValur(newAmount);
                                }else {
                                    Toast.makeText(ScratchActivity.this, "insufficient Balance", Toast.LENGTH_SHORT).show();
                                }
                                loadingBar.HideDialog();
                            }

                        }else{
                            loadingBar.HideDialog();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                    }
                });
    }

    private void updateValur(double newAmount) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID)
                .update("wallet", newAmount)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> updateTask) {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(ScratchActivity.this, "payment was successfully done", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle update failure
                            Log.e("WalletUpdate", "Failed to update wallet amount", updateTask.getException());
                            // You may want to show an error message to the user
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
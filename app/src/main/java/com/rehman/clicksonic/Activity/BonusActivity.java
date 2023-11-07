package com.rehman.clicksonic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rehman.clicksonic.Payment.PaymentActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.util.HashMap;
import java.util.Map;

public class BonusActivity extends AppCompatActivity {
    TextView tv_offerName,tv_offerPrice,tv_expire,tv_details;
    ImageView img_offer,back_image;
    String imageUrl,name,expire,price,detail,date;
    ErrorTost errorTost = new ErrorTost(this);
    CardView card_scratch;
    private FirebaseFirestore db;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    LoadingBar loadingBar;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;
    int wallet,amount;
    AdView adView;
    private InterstitialAd mInterstitialAd;

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
        setContentView(R.layout.activity_bonus);
        loadingBar = new LoadingBar(this);
        loadingBar.ShowDialog("fetching data");


        ShowAds();
        InitView();
        retrieveLatestImage();

//        check = tv_offerName.getText().toString();
//        if (check.equals("Null")){
//            loadingBar.HideDialog();
//            card_scratch.setVisibility(View.GONE);
//        }

        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });
        card_scratch.setOnClickListener(v -> {
            showConfirmationDialog();
            if (mInterstitialAd != null) {
                mInterstitialAd.show(BonusActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }
        });
    }


    private void ShowAds() {
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(this,"ca-app-pub-8763323260658694/8508249288", adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("TAG", "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d("TAG", loadAdError.toString());
                        mInterstitialAd = null;
                    }
                });
    }

    private void retrieveLatestImage() {
        db.collection("bonusOffers")
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
                            detail = document.getString("detail");
                            price = document.getString("price");


                            if (expire != null && expire.equals(dateTime.getCurrentDate())){
                                card_scratch.setVisibility(View.GONE);
                            }

                            assert price != null;
                            amount= Integer.parseInt(price);


                            Glide.with(BonusActivity.this).load(imageUrl).into(img_offer);
                            tv_offerName.setText(name);
                            tv_expire.setText(expire);
                            tv_details.setText(detail);
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
        builder.setMessage("Are you sure you want to buy this Offer?");

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
        Intent intent = new Intent(BonusActivity.this, PaymentActivity.class);
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
                                    Toast.makeText(BonusActivity.this, "insufficient Balance", Toast.LENGTH_SHORT).show();
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
                            saveData();
                            Toast.makeText(BonusActivity.this, "payment was successfully done", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle update failure
                            Log.e("WalletUpdate", "Failed to update wallet amount", updateTask.getException());
                            // You may want to show an error message to the user
                        }
                    }
                });
    }
    private void saveData() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection("boughtItems").document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("moneySpent", price);
        map.put("offerBought", name);
        map.put("userUID", userUID);
        map.put("documentID", id);
        map.put("dateOfPurchase", dateTime.getCurrentDate());
        map.put("timeOfPurchase", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection("boughtItems").document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        loadingBar.HideDialog();
                        Toast.makeText(BonusActivity.this, "Your Order has been placed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    errorTost.showErrorMessage("Something went wrong");
                });

    }

    private void InitView() {

        //textView
        tv_offerName = findViewById(R.id.tv_offerName);
        tv_offerPrice = findViewById(R.id.tv_offerPrice);
        tv_expire = findViewById(R.id.tv_expire);
        tv_details = findViewById(R.id.tv_details);
        //ImageView
        img_offer = findViewById(R.id.img_offer);
        back_image = findViewById(R.id.back_image);
        //CardView
        card_scratch = findViewById(R.id.card_scratch);

        db = FirebaseFirestore.getInstance();

    }
}
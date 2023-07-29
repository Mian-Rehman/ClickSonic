package com.rehman.clicksonic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.MainActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.CustomNotification;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;
import com.rehman.clicksonic.Utils.SavedNotification;

import java.util.HashMap;
import java.util.Map;

public class ConvertPointActivity extends AppCompatActivity {

    ImageView back_image;
    TextView points_text;
    Button btn_convert;
    EditText ed_coins;
    Dialog dialog;

    ErrorTost errorTost = new ErrorTost(this);
    CurrentDateTime currentDateTime = new CurrentDateTime(this);
    LoadingBar loadingBar = new LoadingBar(this);
    SavedNotification notification = new SavedNotification(this);
    CustomNotification showNotification = new CustomNotification(this);

    int coins = 0,points, cuttingPoints = 0,purchaseCoin = 0;
    String userUID;

    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_point);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userUID  = user.getUid();

        initViews();
        getIntentValues();

        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });


        btn_convert.setOnClickListener(v -> {
            purchaseCoin = Integer.parseInt(ed_coins.getText().toString());
            if (isValid(purchaseCoin)){

                   cuttingPoints = (purchaseCoin * 100);

                   if (cuttingPoints <= points){
                       points = points - cuttingPoints;
                       conversionBillDialog(purchaseCoin,points,cuttingPoints);
                   }
                   else{
                      errorTost.showErrorMessage("Something went wrong");
                   }
            }
        });


    }

    private void conversionBillDialog(int purchaseCoin,int points,int cuttingPoints)
    {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.conversion_bill_layout);


        Button btn_confirm = dialog.findViewById(R.id.btn_confirm);
        TextView buyCoins_text = dialog.findViewById(R.id.buyCoins_text);
        TextView cuttingPoints_text = dialog.findViewById(R.id.cuttingPoints_text);
        TextView remainingPoints_text = dialog.findViewById(R.id.remainingPoints_text);

        buyCoins_text.setText(String.valueOf(purchaseCoin));
        cuttingPoints_text.setText(String.valueOf(cuttingPoints));
        remainingPoints_text.setText(String.valueOf(points));

        btn_confirm.setOnClickListener(v -> {
            loadingBar.ShowDialog("purchasing...");
            saveCoinConversion(purchaseCoin, points, cuttingPoints);
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);


    }

    private void saveCoinConversion(int purchaseCoin,int points,int cuttingPoints)
    {

        Map<String, Object> map = new HashMap<>();
        map.put("coin",purchaseCoin + coins);
        map.put("points",points);

        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            map.put("customerUID",userUID);
                            map.put("purchaseTime",currentDateTime.getTimeWithAmPm());
                            map.put("purchaseDate",currentDateTime.getCurrentDate());
                            String key =  FirebaseDatabase.getInstance().getReference().push().getKey();
                            assert key != null;
                            FirebaseFirestore.getInstance().collection("CoinPurchase")
                                    .document(key)
                                    .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                loadingBar.HideDialog();
                                                showNotification.ShowNotification(
                                                        R.drawable.ic_coin,
                                                        "coin purchasing successfully"
                                                );
                                                Toast.makeText(ConvertPointActivity.this,
                                                        "Thanks for Purchasing", Toast.LENGTH_SHORT).show();
                                                notification.savedNotificationData(userUID,
                                                        "Coin Purchasing",
                                                        "Purchasing " + purchaseCoin + " coins successfully",
                                                        "Coins",
                                                        true);
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            loadingBar.HideDialog();
                                            errorTost.showErrorMessage("something went wrong");
                                        }
                                    });
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                        errorTost.showErrorMessage("something went wrong");
                    }
                });
    }


    private boolean isValid(int coins)
    {
        if (coins == 0){
            errorTost.showErrorMessage("please enter coins");
            return false;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private void getIntentValues()
    {
        Intent intent = getIntent();
        coins = Integer.parseInt(intent.getStringExtra("coins"));
        points = Integer.parseInt(intent.getStringExtra("points"));

        points_text.setText(points + " points available");

        if (points < 100){
            ed_coins.setEnabled(false);
            btn_convert.setEnabled(false);
        }

    }

    private void initViews()
    {
        back_image = findViewById(R.id.back_image);
        btn_convert = findViewById(R.id.btn_convert);
        points_text = findViewById(R.id.points_text);
        ed_coins = findViewById(R.id.ed_coins);
    }
}
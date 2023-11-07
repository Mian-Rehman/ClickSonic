package com.rehman.clicksonic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bluehomestudio.luckywheel.LuckyWheel;
import com.bluehomestudio.luckywheel.OnLuckyWheelReachTheTarget;
import com.bluehomestudio.luckywheel.WheelItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rehman.clicksonic.MainActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DailyBonusActivity extends AppCompatActivity {

    ImageView spin_Image;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    LoadingBar loadingBar;
    LuckyWheel lucky_wheel;
    List<WheelItem> wheelItemList = new ArrayList<>();
    String points,date,documentID,added="CoinsAdded";
    String point_amount;
    int coin,wallet;
    int clickCount = 0,SpinCount = 0,CoinsAdded;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;
    Boolean dailyOffer = false,isDataExist =false;
    Random random = new Random();
//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        user = mAuth.getCurrentUser();
//        assert user != null;
//        userUID = user.getUid();
//    }

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
        setContentView(R.layout.activity_daily_bonus);

        spin_Image = findViewById(R.id.spin_Image);


        luckyWheel();
        loadingBar = new LoadingBar(this);

        // creating an object of Random class
        // to generate random numbers for the spin


        // on click listener for btnSpin
        spin_Image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // disabling the button so that user
                // should not click on the button
                // while the wheel is spinning
//                        btnSpin.setEnabled(false);

                // reading random value between 10 to 30
//                        int spin = random.nextInt(20)+10;

                // since the wheel has 10 divisions, the
                // rotation should be a multiple of
                // 360/10 = 36 degrees
//                        spin = spin * 36;

                // timer for each degree movement
//                timer = new CountDownTimer(spin*20,1) {
//                    @Override
//                    public void onTick(long l) {
//                        // rotate the wheel
//                        float rotation = ivWheel.getRotation() + 2;
//                        ivWheel.setRotation(rotation);
//                    }
//
//                    @Override
//                    public void onFinish() {
//                        // enabling the button again
//                        btnSpin.setEnabled(true);
//                    }
//                }.start();
                loadingBar.ShowDialog("Please Wait");

                points = String.valueOf(random.nextInt(10));
                if (points.equals("0")) {
                    points = String.valueOf(1);
                }
                lucky_wheel.rotateWheelTo(Integer.parseInt(points));

            }
        });
    }

    private void luckyWheel() {
        lucky_wheel = findViewById(R.id.lucky_wheel);

        WheelItem wheelItem1 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.light_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "2");
        wheelItemList.add(wheelItem1);

        WheelItem wheelItem2 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.normal_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "20");

        wheelItemList.add(wheelItem2);

        WheelItem wheelItem3 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.light_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "5");

        wheelItemList.add(wheelItem3);

        WheelItem wheelItem4 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.normal_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "10");

        wheelItemList.add(wheelItem4);

        WheelItem wheelItem5 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.light_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "25");

        wheelItemList.add(wheelItem5);

        WheelItem wheelItem6 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.normal_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "15");

        wheelItemList.add(wheelItem6);

        WheelItem wheelItem7 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.light_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "10");

        wheelItemList.add(wheelItem7);

        WheelItem wheelItem8 = new WheelItem(ResourcesCompat.getColor(getResources(), R.color.normal_golden, null),
                BitmapFactory.decodeResource(getResources(), R.drawable.dollar), "25");

        wheelItemList.add(wheelItem8);


        lucky_wheel.addWheelItems(wheelItemList);

        lucky_wheel.setLuckyWheelReachTheTarget(new OnLuckyWheelReachTheTarget() {
            @Override
            public void onReachTarget() {
//                WheelItem selectedItems =  wheelItemList.get(Integer.parseInt(points)-1);
//                String point_amount = selectedItems.text;

                int index = Integer.parseInt(points) - 1;
                if (index >= 0 && index < wheelItemList.size()) {
                    WheelItem selectedItems = wheelItemList.get(index);
                    point_amount = selectedItems.text;
                    Toast.makeText(DailyBonusActivity.this, "" + point_amount, Toast.LENGTH_SHORT).show();

                    checkUserExits();


                } else {
                    Toast.makeText(DailyBonusActivity.this, "Error: Please try again", Toast.LENGTH_SHORT).show();
                }

//                updateValur(point_amount);

            }
        });

    }
    private void updateCoin(Double newCoinAmount) {


        FirebaseFirestore.getInstance().collection("users")
                .document(userUID)
                .update("coin", newCoinAmount)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> updateTask) {
                        if (updateTask.isSuccessful()) {
                            Toast.makeText(DailyBonusActivity.this, "Coins Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(DailyBonusActivity.this, "not done", Toast.LENGTH_SHORT).show();
                            // Handle update failure
                            Log.e("CoinUpdate", "Failed to update Coins", updateTask.getException());
                            // You may want to show an error message to the user
                        }
                    }
                });
    }

    private void saveData() {
        int coinUpdate = Integer.parseInt(point_amount);

        DocumentReference ref = FirebaseFirestore.getInstance().collection("dailyOffer").document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("CoinsAdded", coinUpdate);
        map.put("SpinCount", 1);
        map.put("documentID", id);
        map.put("date", dateTime.getCurrentDate());
        map.put("userUID", userUID);
        map.put("time", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection("dailyOffer").document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        CoinData(coinUpdate);
                        loadingBar.HideDialog();
                        Toast.makeText(DailyBonusActivity.this, "coins added", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    Toast.makeText(DailyBonusActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                });
    }
    private void update() {

        int coinUpdate = Integer.parseInt(point_amount);

        Map<String, Object> map = new HashMap<>();
        map.put("CoinsAdded"+SpinCount, coinUpdate);
        map.put("SpinCount", SpinCount+1);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("dailyOffer").document(documentID);

        docRef
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        docRef
                                .update(map)
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        CoinData(coinUpdate);
                                        Toast.makeText(this, "updated", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
                                    }
                                    loadingBar.HideDialog();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                    loadingBar.HideDialog();
                                });
                    } else {
                        // If the document doesn't exist, you may want to handle it accordingly.
                        Toast.makeText(this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        loadingBar.HideDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error retrieving document", Toast.LENGTH_SHORT).show();
                    loadingBar.HideDialog();
                });

    }

    private boolean checkUserExits() {
        FirebaseFirestore.getInstance().collection("dailyOffer")
                .whereEqualTo("userUID",userUID)
                .whereEqualTo("date",dateTime.getCurrentDate())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            DocumentSnapshot snapshot = queryDocumentSnapshots.getDocuments().get(0); // Assuming you want the first matching document

                            isDataExist = true;

                            CoinsAdded = snapshot.getLong("CoinsAdded").intValue();
                            date = snapshot.getString("date");
                            documentID = snapshot.getString("documentID");
                            SpinCount = snapshot.getLong("SpinCount").intValue();
//                            SpinCount = Integer.parseInt(Objects.requireNonNull(snapshot.getString("SpinCount")));
                            loadingBar.HideDialog();
                            Toast.makeText(DailyBonusActivity.this, "Data Found", Toast.LENGTH_SHORT).show();

                            if (SpinCount >= 1 && SpinCount <= 4){
                                moneyTransferDialog();
                            }else if (SpinCount >= 5) {
                                Toast.makeText(DailyBonusActivity.this, "Come Again Tomorrow", Toast.LENGTH_SHORT).show();
                                DailyLimitDialog();
                            }else {
                                update();
                            }

                        } else {
                            Toast.makeText(DailyBonusActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                            isDataExist = false;
                            saveData();
                        }
                    }
                });
        return false;
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

                                if ( 2 <= wallet){
                                    double newAmount = wallet - 2;

                                    updateValur(newAmount);
                                    update();
                                }else {
                                    Toast.makeText(DailyBonusActivity.this, "insufficient Balance", Toast.LENGTH_SHORT).show();
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
    private void CoinData(int coinAdd)
    {
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                coin = snapshot.getLong("coin").intValue();


                                double newCoinAmount = coin + coinAdd;

                                updateCoin(newCoinAmount);
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
                            Toast.makeText(DailyBonusActivity.this, "payment was successfully done", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle update failure
                            Log.e("WalletUpdate", "Failed to update wallet amount", updateTask.getException());
                            // You may want to show an error message to the user
                        }
                    }
                });
    }
    private void moneyTransferDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.money_transfer_dialog, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_Yes = layout_dialog.findViewById(R.id.btn_Yes);
        Button btn_No = layout_dialog.findViewById(R.id.btn_No);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_Yes.setOnClickListener(v -> {
            userData();

            dialog.dismiss();
        });
        btn_No.setOnClickListener(v -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            dialog.dismiss();
        });

    }

    private void DailyLimitDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.daily_limit_reached_dialog, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_ok = layout_dialog.findViewById(R.id.btn_ok);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_ok.setOnClickListener(v -> {
            Intent intent = new Intent(DailyBonusActivity.this,MainActivity.class);
            startActivity(intent);
            finish();

            dialog.dismiss();
        });

    }
}
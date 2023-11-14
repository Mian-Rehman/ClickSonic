package com.rehman.clicksonic.PurchaseFrag;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Activity.ScratchActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.util.HashMap;
import java.util.Map;


public class CoinsPurhaseFragment extends Fragment {

    CardView card_100,card_150,card_500,card_800;
    String offerName;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;
    int wallet,amount,coin;
    CurrentDateTime dateTime = new CurrentDateTime(getActivity());
    ErrorTost errorTost = new ErrorTost(getActivity());
    LoadingBar loadingBar;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_coins_purhase, container, false);

        initView(view);
        loadingBar = new LoadingBar(getActivity());

        card_100.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please Wait...");
            offerName = "500Coin";
            int cost = 100;
            int getCoin = 500;
            showConfirmationDialog(cost,getCoin);
        });

        card_150.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please Wait...");
            offerName = "18000Coin";
            int cost = 300;
            int getCoin = 1800;
            showConfirmationDialog(cost,getCoin);
        });

        card_500.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please Wait...");
            offerName = "3000Coin";
            int cost = 500;
            int getCoin = 3000;
            showConfirmationDialog(cost,getCoin);
        });

        card_800.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please Wait...");
            offerName = "6500Coin";
            int cost = 1000;
            int getCoin = 6500;
            showConfirmationDialog(cost,getCoin);
        });


        return view;
    }

    private void showConfirmationDialog(int cost, int getCoin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm Action");
        builder.setMessage("Are you sure you want to buy this Offer?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform the action when the user clicks "Yes"
                dialog.dismiss();
                userData(cost,getCoin);
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

    private void userData(int cost, int getCoin) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()) {
                                wallet = snapshot.getLong("wallet").intValue();
                                coin = snapshot.getLong("coin").intValue();


                                if (cost <= wallet) {
                                    double newAmount = wallet - cost;
                                    double coinAmount = coin + getCoin;
                                    updateValur(newAmount,coinAmount,cost);
                                } else {
                                    Toast.makeText(getActivity(), "insufficient Balance", Toast.LENGTH_SHORT).show();
                                }
                                loadingBar.HideDialog();
                            }

                        } else {
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
    private void updateValur(double newAmount, double coinAmount, int cost) {
        Map<String, Object> map = new HashMap<>();
        map.put("wallet", newAmount);
        map.put("coin", coinAmount);

        FirebaseFirestore.getInstance().collection("users")
                .document(userUID)
                .update(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> updateTask) {
                        if (updateTask.isSuccessful()) {
                            saveData(cost);
                            Toast.makeText(getActivity(), "payment was successfully done", Toast.LENGTH_SHORT).show();
                        } else {
                            // Handle update failure
                            Log.e("WalletUpdate", "Failed to update wallet amount", updateTask.getException());
                            // You may want to show an error message to the user
                        }
                    }
                });
    }
    private void saveData(int cost) {

        Map<String, Object> map = new HashMap<>();
        map.put("moneySpent", String.valueOf(cost));
        map.put("offerBought", offerName);
        map.put("offer", "coinPurchase");
        map.put("dateOfPurchase", dateTime.getCurrentDate());
        map.put("userUID", userUID);
        map.put("timeOfPurchase", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection("coinBought")
                .add(map)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            loadingBar.HideDialog();
                            Toast.makeText(getActivity(), "Your Order has been placed", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                        errorTost.showErrorMessage("Something went wrong");
                    }
                });

    }

    private void initView(View view) {
        card_100 = view.findViewById(R.id.card_100);
        card_150 = view.findViewById(R.id.card_150);
        card_500 = view.findViewById(R.id.card_500);
        card_800 = view.findViewById(R.id.card_800);
    }
}
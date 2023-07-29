package com.rehman.clicksonic.MenuFrag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Activity.BuyCoinActivity;
import com.rehman.clicksonic.Activity.ConvertPointActivity;
import com.rehman.clicksonic.Activity.DailyBonusActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.LoadingBar;


public class HomeFragment extends Fragment implements View.OnClickListener{

    TextView coins_text,points_text;
    LinearLayout ll_dailyOffer,ll_bonus,ll_scratch,ll_refresh;
    CardView rate_card,wallet_card,invite_card,watchVideo_card,earnCoin_card,buySub_card
                ,buyWatchTime_card,convertCoin_card,addLink_card;

    Button btn_convertCoins,btn_buyCoin;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    FirebaseAuth mAuth;
    FirebaseUser user;

    String userUID,fullName,email,password,loginWith;
    int coins,points,bonus;

    LoadingBar loadingBar;

    @Override
    public void onStart() {
        super.onStart();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();
        userData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        loadingBar = new LoadingBar(getActivity());
        loadingBar.ShowDialog("fetch data");
        preferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = preferences.edit();


        initViews(view);
        clickLisnters(view);

        return view;
    }

    private void clickLisnters(View view)
    {
        ll_refresh.setOnClickListener(this::onClick);
        btn_convertCoins.setOnClickListener(this::onClick);
        btn_buyCoin.setOnClickListener(this::onClick);
        ll_dailyOffer.setOnClickListener(this::onClick);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id){

            case R.id.ll_refresh:
                loadingBar.ShowDialog("refreshing");
                userData();
                break;

            case R.id.btn_buyCoin:
                startActivity(new Intent(getActivity(), BuyCoinActivity.class));
                break;

            case R.id.btn_convertCoins:
                Intent covertIntent = new Intent(getActivity(),ConvertPointActivity.class);
                covertIntent.putExtra("coins",String.valueOf(coins));
                covertIntent.putExtra("points",String.valueOf(points));
                startActivity(covertIntent);
                break;

                //Middle Menu
            case R.id.ll_dailyOffer:
                startActivity(new Intent(getActivity(), DailyBonusActivity.class));
                break;
        }

    }

    private void initViews(View view)
    {
        coins_text = view.findViewById(R.id.coins_text);
        points_text = view.findViewById(R.id.points_text);
        ll_dailyOffer = view.findViewById(R.id.ll_dailyOffer);
        ll_bonus = view.findViewById(R.id.ll_bonus);
        ll_scratch = view.findViewById(R.id.ll_scratch);
        ll_refresh = view.findViewById(R.id.ll_refresh);
        rate_card = view.findViewById(R.id.rate_card);
        wallet_card = view.findViewById(R.id.wallet_card);
        invite_card = view.findViewById(R.id.invite_card);
        watchVideo_card = view.findViewById(R.id.watchVideo_card);
        earnCoin_card = view.findViewById(R.id.earnCoin_card);
        buySub_card = view.findViewById(R.id.buySub_card);
        buyWatchTime_card = view.findViewById(R.id.buyWatchTime_card);
        convertCoin_card = view.findViewById(R.id.convertCoin_card);
        addLink_card = view.findViewById(R.id.addLink_card);

        btn_convertCoins = view.findViewById(R.id.btn_convertCoins);
        btn_buyCoin = view.findViewById(R.id.btn_buyCoin);

        ll_dailyOffer = view.findViewById(R.id.ll_dailyOffer);
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
                                coins = snapshot.getLong("coin").intValue();
                                points = snapshot.getLong("points").intValue();

                                editor.putString("coins",String.valueOf(coins));
                                editor.putString("points",String.valueOf(points));
                                editor.apply();
                                editor.commit();

                                coins_text.setText(String.valueOf(coins));
                                points_text.setText(String.valueOf(points));
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


}
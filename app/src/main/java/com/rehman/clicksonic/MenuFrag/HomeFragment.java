package com.rehman.clicksonic.MenuFrag;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Activity.BuyCoinActivity;
import com.rehman.clicksonic.Activity.ConvertPointActivity;
import com.rehman.clicksonic.Activity.DailyBonusActivity;
import com.rehman.clicksonic.Activity.InformationActivity;
import com.rehman.clicksonic.Activity.ScratchActivity;
import com.rehman.clicksonic.Payment.PaymentActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.LoadingBar;



public class HomeFragment extends Fragment implements View.OnClickListener{

    private static final int MY_REQUEST_CODE = 100;
    TextView coins_text,points_text;
    LinearLayout ll_dailyOffer,ll_bonus,ll_scratch,ll_refresh;
    CardView rate_card,wallet_card,invite_card,youtube_card,earnCoin_card,facebook_card
                ,tiktok_card,convertCoin_card,instagram_card,card_addFunds;

    Button btn_convertCoins,btn_buyCoin;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID,fullName,email,password,loginWith;
    int coins,points,bonus,wallet;
    LoadingBar loadingBar;
    private AppUpdateManager appUpdateManager;
    AdView adView;
    private InterstitialAd mInterstitialAd;
    View view;

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
         view = inflater.inflate(R.layout.fragment_home, container, false);


        loadingBar = new LoadingBar(getActivity());
        loadingBar.ShowDialog("fetch data");
        preferences = getActivity().getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = preferences.edit();


        initViews(view);
        clickLisnters(view);
        checkAppUpdate(view);
        ShowAds(view);

        ll_scratch.setOnClickListener(v -> {

            startActivity(new Intent(getActivity(), ScratchActivity.class));

        });



        instagram_card.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), InformationActivity.class);
            intent.putExtra("name","Instagram");
            intent.putExtra("type","Followers");
            intent.putExtra("hint","Number of followers...");

            intent.putExtra("followers","3");
            intent.putExtra("likes","0.5");
            intent.putExtra("views","0.25");

            intent.putExtra("maxFollow","3000");
            intent.putExtra("minFollow","50");

            intent.putExtra("maxLike","20000");
            intent.putExtra("minLike","10");

            intent.putExtra("maxView","100000");
            intent.putExtra("minView","100");
            intent.putExtra("image",R.drawable.instagram);
            startActivity(intent);

            if (mInterstitialAd != null) {
                mInterstitialAd.show(getActivity());
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        });
        facebook_card.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), InformationActivity.class);
            intent.putExtra("name","Facebook");
            intent.putExtra("type","Followers");
            intent.putExtra("hint","Number of followers...");

            intent.putExtra("followers","3");
            intent.putExtra("likes","10");
            intent.putExtra("views","1");

            intent.putExtra("maxFollow","3000");
            intent.putExtra("minFollow","100");

            intent.putExtra("maxLike","1000");
            intent.putExtra("minLike","10");

            intent.putExtra("maxView","10000");
            intent.putExtra("minView","500");

            intent.putExtra("image",R.drawable.facebook);
            startActivity(intent);
            if (mInterstitialAd != null) {
                mInterstitialAd.show(getActivity());
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        });
        tiktok_card.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), InformationActivity.class);
            intent.putExtra("name","TikTok");
            intent.putExtra("type","Followers");
            intent.putExtra("hint","Number of followers...");

            intent.putExtra("followers","10");
            intent.putExtra("likes","1");
            intent.putExtra("views","0.005");

            intent.putExtra("maxFollow","10000");
            intent.putExtra("minFollow","500");

            intent.putExtra("maxLike","10000");
            intent.putExtra("minLike","100");

            intent.putExtra("maxView","1000000");
            intent.putExtra("minView","100");

            intent.putExtra("image",R.drawable.tiktok);
            startActivity(intent);
            if (mInterstitialAd != null) {
                mInterstitialAd.show(getActivity());
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        });
        youtube_card.setOnClickListener(v -> {

            Intent intent = new Intent(getActivity(), InformationActivity.class);
            intent.putExtra("name","YouTube");
            intent.putExtra("type","Subscriber");
            intent.putExtra("hint","Number of subscriber...");

            intent.putExtra("followers","10");
            intent.putExtra("likes","3");
            intent.putExtra("views","3");

            intent.putExtra("maxFollow","1000");
            intent.putExtra("minFollow","50");

            intent.putExtra("maxLike","3000");
            intent.putExtra("minLike","20");

            intent.putExtra("maxView","5000");
            intent.putExtra("minView","100");

            intent.putExtra("image",R.drawable.ic_youtube);
            startActivity(intent);
            if (mInterstitialAd != null) {
                mInterstitialAd.show(getActivity());
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        });

        card_addFunds.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), PaymentActivity.class);
            startActivity(intent);
        });


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
        //CardView
        rate_card = view.findViewById(R.id.rate_card);
        wallet_card = view.findViewById(R.id.wallet_card);
        invite_card = view.findViewById(R.id.invite_card);
        youtube_card = view.findViewById(R.id.youtube_card);
        earnCoin_card = view.findViewById(R.id.earnCoin_card);
        facebook_card = view.findViewById(R.id.facebook_card);
        tiktok_card = view.findViewById(R.id.tiktok_card);
        card_addFunds = view.findViewById(R.id.card_addFunds);
//        convertCoin_card = view.findViewById(R.id.convertCoin_card);
        instagram_card = view.findViewById(R.id.instagram_card);

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
                                wallet = snapshot.getLong("wallet").intValue();

                                editor.putString("coins",String.valueOf(coins));
                                editor.putString("points",String.valueOf(points));
                                editor.putString("wallet",String.valueOf(wallet));
                                editor.apply();
                                editor.commit();

                                coins_text.setText(String.valueOf(coins));
//                                points_text.setText(String.valueOf(points));
                                points_text.setText(String.valueOf(wallet+"Rs"));
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

    private void checkAppUpdate(View view)
    {
        appUpdateManager = AppUpdateManagerFactory.create(requireActivity());

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            // Pass the intent that is returned by 'getAppUpdateInfo()'.
                            appUpdateInfo,
                            // an activity result launcher registered via registerForActivityResult
                            AppUpdateType.FLEXIBLE,
                            // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                            // flexible updates.
                            requireActivity(),
                            MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Before starting an update, register a listener for updates.
        appUpdateManager.registerListener(listener);

    }
    private void ShowAds(View view) {
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        InterstitialAd.load(getActivity(),"ca-app-pub-8763323260658694/8508249288", adRequest,
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == MY_REQUEST_CODE)
        {
            if (resultCode != RESULT_OK)
            {
                Log.d("TAG", "onActivityResult: ");
            }
        }

    }

    InstallStateUpdatedListener listener = state -> {
        if (state.installStatus() == InstallStatus.DOWNLOADED) {
            // After the update is downloaded, show a notification
            // and request user confirmation to restart the app.
            popupSnackbarForCompleteUpdate();
        }
    };
    // Displays the snackbar notification and call to action.
    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        view.findViewById(android.R.id.content),
                        "An update has just been downloaded.",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("RESTART", view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(
                getResources().getColor(android.R.color.holo_blue_bright));
        snackbar.show();
    }
}


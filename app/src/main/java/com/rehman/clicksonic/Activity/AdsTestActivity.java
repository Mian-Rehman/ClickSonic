package com.rehman.clicksonic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.rehman.clicksonic.MainActivity;
import com.rehman.clicksonic.R;

public class AdsTestActivity extends AppCompatActivity {

    AdView adView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads_test);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

//        adView = findViewById(R.id.adView);
//        AdView adView = new AdView(this);



//        adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");
//
        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);
//
//        adView.setAdListener(new AdListener() {
//            @Override
//            public void onAdClicked() {
//                // Code to be executed when the user clicks on an ad.
//                Toast.makeText(AdsTestActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdClosed() {
//                // Code to be executed when the user is about to return
//                // to the app after tapping on an ad.
//                Toast.makeText(AdsTestActivity.this, "Closed", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdFailedToLoad(@NonNull LoadAdError adError) {
//                // Code to be executed when an ad request fails.
//                adView.loadAd(adRequest);
//            }
//
//            @Override
//            public void onAdImpression() {
//                // Code to be executed when an impression is recorded
//                // for an ad.
//            }
//
//            @Override
//            public void onAdLoaded() {
//                // Code to be executed when an ad finishes loading.
//                Toast.makeText(AdsTestActivity.this, "ads loaded", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAdOpened() {
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//        });

//        AdRequest adRequest = new AdRequest.Builder().build();

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

//        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
//            @Override
//            public void onAdClicked() {
//                // Called when a click is recorded for an ad.
//                Log.d("TAG", "Ad was clicked.");
//            }
//
//            @Override
//            public void onAdDismissedFullScreenContent() {
//                // Called when ad is dismissed.
//                // Set the ad reference to null so you don't show the ad a second time.
//                Log.d("TAG", "Ad dismissed fullscreen content.");
//                mInterstitialAd = null;
//            }
//
//            @Override
//            public void onAdFailedToShowFullScreenContent(AdError adError) {
//                // Called when ad fails to show.
//                Log.e("TAG", "Ad failed to show fullscreen content.");
//                mInterstitialAd = null;
//            }
//
//            @Override
//            public void onAdImpression() {
//                // Called when an impression is recorded for an ad.
//                Log.d("TAG", "Ad recorded an impression.");
//            }
//
//            @Override
//            public void onAdShowedFullScreenContent() {
//                // Called when ad is shown.
//                Log.d("TAG", "Ad showed fullscreen content.");
//            }
//        });




        Button button = findViewById(R.id.button);

        button.setOnClickListener(v -> {
            if (mInterstitialAd != null) {
                mInterstitialAd.show(AdsTestActivity.this);
            } else {
                Log.d("TAG", "The interstitial ad wasn't ready yet.");
            }

        });


    }
}
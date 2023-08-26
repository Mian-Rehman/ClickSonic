package com.rehman.clicksonic.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {

    ImageView top_image, back_image;
    TextView tv_title, tv_total, coins_text,tv_maxmin;
    EditText ed_link, ed_followers, ed_likes, ed_views, ed_comments, ed_fullName;
    RadioButton radio_followers, radio_likes, radio_views, radio_comments;
    String link, followers, likes, views, comments, fullName, name, type, hint,cost,
            coinLike, coinFollower, coinViews,maxFollow,minFollow,maxLike,minLike,maxView,minView;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    Button btn_submit;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userUID;
    double coins,newCoins,usedCoins;
    double multipliedValue;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    LoadingBar loadingBar = new LoadingBar(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;
        userUID = mUser.getUid();

        initView();
        instagram();

        loadingBar = new LoadingBar(this);
        loadingBar.ShowDialog("fetch data");
        preferences = this.getSharedPreferences("USER", Context.MODE_PRIVATE);
        editor = preferences.edit();

        userData();

        instagram();
        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        radio_followers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_followers.setVisibility(View.VISIBLE);
                    if (radio_followers.getText().equals("Subscriber")) {
                        tv_maxmin.setText("Please Select number of subscribers b/w "+minFollow+" and "+maxFollow);
                    } else {
                        tv_maxmin.setText("Please Select number of followers b/w "+minFollow+" and "+maxFollow);
                    }
                    ed_followers.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String enteredText = s.toString();
                            calculateAndUpdateTotalCoins(enteredText);
                            cost = String.valueOf(multipliedValue);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });

                } else {
                    ed_followers.setVisibility(View.GONE);
                }

            }
        });
        radio_likes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_likes.setVisibility(View.VISIBLE);
                    tv_maxmin.setText("Please Select number of likes b/w "+minLike+" and "+maxLike);
                    ed_likes.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String enteredText = s.toString();
                            calculateAndUpdateTotalCoins(enteredText);
                            cost = String.valueOf(multipliedValue);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    ed_likes.setVisibility(View.GONE);
                }

            }
        });
        radio_comments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_comments.setVisibility(View.VISIBLE);

                } else {
                    ed_comments.setVisibility(View.GONE);
                }

            }
        });
        radio_views.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_views.setVisibility(View.VISIBLE);
                    tv_maxmin.setText("Please Select number of views b/w "+minView+" and "+maxView);
                    ed_views.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        }
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            String enteredText = s.toString();
                            calculateAndUpdateTotalCoins(enteredText);
                            cost = String.valueOf(multipliedValue);
                        }
                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });
                } else {
                    ed_views.setVisibility(View.GONE);
                }

            }
        });

        btn_submit.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please wait");
            fullName = ed_fullName.getText().toString();
            link = ed_link.getText().toString();
            if (radio_followers.isChecked()) {
                followers = ed_followers.getText().toString();
            }
            if (radio_likes.isChecked()) {
                likes = ed_likes.getText().toString();
            }
            if (radio_comments.isChecked()) {
                comments = ed_comments.getText().toString();
            }
            if (radio_views.isChecked()) {
                views = ed_views.getText().toString();
            }
            if (isValid(link, followers, likes, views, comments, fullName , multipliedValue, coins)) {
                //saveFirebaseData();
                IntentData();
                userData();
            }
        });
    }

    private void IntentData() {
        Intent intent =new Intent(InformationActivity.this, ReceiptActivity.class);
        intent.putExtra("link",link);
        intent.putExtra("fullName",fullName);
        intent.putExtra("title",name);
        intent.putExtra("type",type);
        if (radio_followers.isChecked()) {
            intent.putExtra("followers", followers);
        }
        if (radio_likes.isChecked()) {
            intent.putExtra("likes",likes);
        }
        if (radio_views.isChecked()) {
            intent.putExtra("views",views);
        }
        intent.putExtra("cost",cost);
        intent.putExtra("updatedCoins",String.valueOf(newCoins));

        startActivity(intent);

    }

    private void calculateAndUpdateTotalCoins(String enteredText) {
        if (!enteredText.isEmpty()) {
            try {
                double enteredValue = Double.parseDouble(enteredText);
                multipliedValue = 0.0;

                if (radio_followers.isChecked()) {
                    multipliedValue = enteredValue * Double.parseDouble(coinFollower);
                    tv_total.setText(String.valueOf(multipliedValue));
                }
                if (radio_likes.isChecked()) {
                    multipliedValue = enteredValue * Double.parseDouble(coinLike);
                    tv_total.setText(String.valueOf(multipliedValue));
                }
                if (radio_views.isChecked()) {
                    multipliedValue = enteredValue * Double.parseDouble(coinViews);
                    tv_total.setText(String.valueOf(multipliedValue));
                }
            } catch (NumberFormatException e) {
                tv_total.setText("Invalid input");
            }
        } else {
        }
    }

    private void userData() {


        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()){
                            DocumentSnapshot snapshot = task.getResult();
                            if (snapshot.exists()){
                                coins = snapshot.getLong("coin").intValue();

                                editor.putString("coins",String.valueOf(coins));
                                editor.apply();
                                editor.commit();

                                coins_text.setText(String.valueOf(coins));
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

    private void saveFirebaseData() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection(name).document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("link", link);
        if (radio_followers.isChecked()) {
            if (radio_followers.getText().equals("Subscriber")) {
                map.put("subscriber", followers);
            } else {
                map.put("followers", followers);
            }
        }
        if (radio_likes.isChecked()) {
            map.put("likes", likes);
        }
        if (radio_comments.isChecked()) {
            map.put("comments", comments);
        }
        if (radio_views.isChecked()) {
            map.put("views", views);
        }
        map.put("cost", multipliedValue);
        map.put("updatedCoins", newCoins);
        map.put("OrderID", id);
        map.put("userUID", userUID);
        map.put("OrderDate", dateTime.getCurrentDate());
        map.put("OrderTime", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection(name).document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingBar.HideDialog();
                        Toast.makeText(InformationActivity.this, "Your request has been sent for " + name, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    Toast.makeText(InformationActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                });


        /*usedCoins = multipliedValue;
        newCoins = coins - usedCoins;

        if (newCoins < 0) {
            // User doesn't have enough coins to complete the transaction
            loadingBar.HideDialog();
            Toast.makeText(InformationActivity.this, "Insufficient coins. You don't have enough coins to complete this transaction.", Toast.LENGTH_SHORT).show();
            return;
        }*/

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("users").document(userUID);
        userRef.update("coin", newCoins)
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

    private boolean isValid(String link, String followers, String likes, String views, String comments, String fullName, double multiplied, double coins1) {

        if (link.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Link is required");
            return false;
        }
        if (fullName.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Name is required");
            return false;
        }
        if (radio_likes.isChecked()) {

            if (likes.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of likes required");
                return false;
            }

            int likesAmount = Integer.parseInt(likes);
            int min = Integer.parseInt(minLike);
            int max = Integer.parseInt(maxLike);

            if (likesAmount < min || likesAmount > max) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Likes should be between " + min + " and " + max);
                return false;
            }
        }
        if (radio_followers.isChecked()) {


            if (followers.isEmpty()) {
                loadingBar.HideDialog();
                if (radio_followers.getText().equals("Subscriber")) {
                    errorTost.showErrorMessage("Amount of subscriber required");
                } else {
                    errorTost.showErrorMessage("Amount of followers required");
                }
                return false;
            }

            int followAmount = Integer.parseInt(followers);
            int min = Integer.parseInt(minFollow);
            int max = Integer.parseInt(maxFollow);

            if (followAmount < min || followAmount > max) {
                loadingBar.HideDialog();
                if (radio_followers.getText().equals("Subscriber")){
                    errorTost.showErrorMessage("Subscriber should be between " + min + " and " + max);
                }else{
                    errorTost.showErrorMessage("Followers should be between " + min + " and " + max);
                }

                return false;
            }
        }

        if (radio_views.isChecked()) {

            if (views.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of Views required");
                return false;
            }

            int viewAmount = Integer.parseInt(views);
            int min = Integer.parseInt(minView);
            int max = Integer.parseInt(maxView);

            if (viewAmount < min || viewAmount > max) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Views should be between " + min + " and " + max);
                return false;
            }
        }

        if (!radio_views.isChecked() && !radio_likes.isChecked() && !radio_followers.isChecked()){
            loadingBar.HideDialog();
            errorTost.showErrorMessage("please select a category");
            return false;
        }


        usedCoins = multiplied;
        newCoins = coins1 - usedCoins;

        if (newCoins < 0) {
            // User doesn't have enough coins to complete the transaction
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Insufficient coins");
            return false;
        }
        /*if (radio_comments.isChecked()) {

            if (comments.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of comments required");
                return false;
            }
        }*/

        return true;
    }

    private void instagram() {

        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        hint = getIntent().getStringExtra("hint");

        coinFollower = getIntent().getStringExtra("followers");
        coinLike = getIntent().getStringExtra("likes");
        coinViews = getIntent().getStringExtra("views");

        minLike = getIntent().getStringExtra("minLike");
        maxLike = getIntent().getStringExtra("maxLike");

        minFollow = getIntent().getStringExtra("minFollow");
        maxFollow = getIntent().getStringExtra("maxFollow");

        minView = getIntent().getStringExtra("minView");
        maxView = getIntent().getStringExtra("maxView");
        int image = getIntent().getIntExtra("image", 0);


        tv_title.setText(name);
        radio_followers.setText(type);
        ed_followers.setHint(hint);
        top_image.setImageResource(image);

    }

    private void initView() {

        //ImageView
        top_image = findViewById(R.id.top_image);
        back_image = findViewById(R.id.back_image);

        //TextView
        tv_title = findViewById(R.id.tv_title);
        coins_text = findViewById(R.id.coins_text);
        tv_total = findViewById(R.id.tv_total);
        tv_maxmin = findViewById(R.id.tv_maxmin);

        //EditText
        ed_link = findViewById(R.id.ed_link);
        ed_followers = findViewById(R.id.ed_followers);
        ed_likes = findViewById(R.id.ed_likes);
        ed_views = findViewById(R.id.ed_views);
        ed_comments = findViewById(R.id.ed_comments);
        ed_fullName = findViewById(R.id.ed_fullName);

        //CheckBoxes
        radio_followers = findViewById(R.id.radio_followers);
        radio_likes = findViewById(R.id.radio_likes);
        radio_views = findViewById(R.id.radio_views);
        radio_comments = findViewById(R.id.radio_comments);

        //Button
        btn_submit = findViewById(R.id.btn_submit);
    }
}
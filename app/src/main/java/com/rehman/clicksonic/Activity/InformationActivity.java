package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class InformationActivity extends AppCompatActivity {

    ImageView top_image, back_image;
    TextView tv_title;
    EditText ed_link, ed_followers, ed_likes, ed_views, ed_comments, ed_username;
    CheckBox check_followers, check_likes, check_views, check_comments;
    String link, followers, likes, views, comments, username, name, type, hint;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    Button btn_submit;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userUID;
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
        back_image.setOnClickListener(v -> {
            onBackPressed();
        });

        check_followers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_followers.setVisibility(View.VISIBLE);
                } else {
                    ed_followers.setVisibility(View.GONE);
                }

            }
        });
        check_likes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_likes.setVisibility(View.VISIBLE);
                } else {
                    ed_likes.setVisibility(View.GONE);
                }

            }
        });
        check_comments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_comments.setVisibility(View.VISIBLE);
                } else {
                    ed_comments.setVisibility(View.GONE);
                }

            }
        });
        check_views.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ed_views.setVisibility(View.VISIBLE);
                } else {
                    ed_views.setVisibility(View.GONE);
                }

            }
        });

        btn_submit.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please wait");
            username = ed_username.getText().toString();
            link = ed_link.getText().toString();
            if (check_followers.isChecked()) {
                followers = ed_followers.getText().toString();
            }
            if (check_followers.isChecked()) {
                likes = ed_likes.getText().toString();
            }
            if (check_followers.isChecked()) {
                comments = check_comments.getText().toString();
            }
            if (check_followers.isChecked()) {
                views = ed_views.getText().toString();
            }
            if (isValid(link, followers, likes, views, comments, username)) {
                saveFirebaseData();
            }
        });
    }

    private void saveFirebaseData() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection(name).document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("username", username);
        map.put("link", link);
        if (check_followers.isChecked()) {
            if (check_followers.getText().equals("Subscriber")) {
                map.put("Subscriber", followers);
            } else {
                map.put("followers", followers);
            }
        }
        if (check_likes.isChecked()) {
            map.put("likes", likes);
        }
        if (check_comments.isChecked()) {
            map.put("comments", comments);
        }
        if (check_views.isChecked()) {
            map.put("views", views);
        }
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

    }

    private boolean isValid(String link, String followers, String likes, String views, String comments, String username) {

        if (link.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Link is required");
            return false;
        }
        if (username.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Name is required");
            return false;
        }
        if (check_likes.isChecked()) {

            if (likes.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of likes required");
                return false;
            }
        }
        if (check_followers.isChecked()) {

            if (followers.isEmpty()) {
                loadingBar.HideDialog();
                if (check_followers.getText().equals("Subscriber")) {
                    errorTost.showErrorMessage("Amount of subscriber required");
                } else {
                    errorTost.showErrorMessage("Amount of followers required");
                }
                return false;
            }
        }
        if (check_views.isChecked()) {

            if (views.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of Views required");
                return false;
            }
        }
        if (check_comments.isChecked()) {

            if (comments.isEmpty()) {
                loadingBar.HideDialog();
                errorTost.showErrorMessage("Amount of comments required");
                return false;
            }
        }

        return true;
    }

    private void instagram() {

        name = getIntent().getStringExtra("name");
        type = getIntent().getStringExtra("type");
        hint = getIntent().getStringExtra("hint");
        int image = getIntent().getIntExtra("image", 0);


        tv_title.setText(name);
        check_followers.setText(type);
        ed_followers.setHint(hint);
        top_image.setImageResource(image);

    }

    private void initView() {

        //ImageView
        top_image = findViewById(R.id.top_image);
        back_image = findViewById(R.id.back_image);

        //TextView
        tv_title = findViewById(R.id.tv_title);

        //EditText
        ed_link = findViewById(R.id.ed_link);
        ed_followers = findViewById(R.id.ed_followers);
        ed_likes = findViewById(R.id.ed_likes);
        ed_views = findViewById(R.id.ed_views);
        ed_comments = findViewById(R.id.ed_comments);
        ed_username = findViewById(R.id.ed_username);

        //CheckBoxes
        check_followers = findViewById(R.id.check_followers);
        check_likes = findViewById(R.id.check_likes);
        check_views = findViewById(R.id.check_views);
        check_comments = findViewById(R.id.check_comments);

        //Button
        btn_submit = findViewById(R.id.btn_submit);
    }
}
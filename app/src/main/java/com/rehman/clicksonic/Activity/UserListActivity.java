package com.rehman.clicksonic.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.R;


public class UserListActivity extends AppCompatActivity {
    EditText ed_username;
    ImageButton img_search;
    ImageView back_image;
    RecyclerView recycler_view;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        intiView();

        ed_username.requestFocus();
        back_image.setOnClickListener(v -> { onBackPressed(); });

        img_search.setOnClickListener(v -> {
            username = ed_username.getText().toString();
            if(username.isEmpty()){
                ed_username.setError("Invalid Username");
                return;
            }
            SearchRecyclerView(username);
        });

    }

    private void SearchRecyclerView(String username) {


    }

    private void intiView() {
        ed_username=findViewById(R.id.ed_username);

        img_search=findViewById(R.id.img_search);
        back_image=findViewById(R.id.back_image);

        recycler_view=findViewById(R.id.search_user_recycler_view);
    }
}
package com.rehman.clicksonic.Lists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;
import com.rehman.clicksonic.Adapter.AdapterUsers;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;
import java.util.List;


public class UserListActivity extends AppCompatActivity {
    ImageView back_image;
    TextView totalCount;
    RecyclerView recyclerView;
    AdapterUsers adapter;
    ArrayList<UserModel> mDataList = new ArrayList<>();
    String fullName,email;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        mAuth = FirebaseAuth.getInstance();

        intiView();
        getUserList();
        back_image.setOnClickListener(v -> { onBackPressed(); });

    }

//    private void getIntentValues() {
//
//        Intent intent = getIntent();
//        fullName = intent.getStringExtra("name");
//        email = intent.getStringExtra("email");
//
//        getUserList();
//    }

    private void getUserList() {

        adapter = new AdapterUsers(this,mDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("users")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error !=null) {
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }
                        assert value != null;
                        for (DocumentChange documentChange : value.getDocumentChanges())
                        {
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                mDataList.add(documentChange.getDocument().toObject(UserModel.class));
                            }

                        }
                        adapter.notifyDataSetChanged();
                        totalCount.setText(String.valueOf(mDataList.size()));

                    }
                });

    }

    private void intiView() {
        back_image=findViewById(R.id.back_image);

        totalCount=findViewById(R.id.totalCount);

        recyclerView=findViewById(R.id.search_user_recycler_view);
    }
}
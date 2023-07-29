package com.rehman.clicksonic.Notification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rehman.clicksonic.Adapter.NotificationAdapter;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    NotificationAdapter adapter;
    ArrayList<NotificationData> mDataList;
    RecyclerView recycleView;
    FirebaseAuth mAuth;
    String userUID;
    RelativeLayout rr_notification;
    ImageView back_image;

    LoadingBar loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getCurrentUser().getUid();
        loadingBar = new LoadingBar(this);

        initViews();
        getNotifications();

        back_image.setOnClickListener(v -> {
            onBackPressed();
        });


    }

    private void getNotifications()
    {
        mDataList = new ArrayList<>();
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(this,mDataList);
        recycleView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).collection("Notifications")
                .orderBy("notificationTime", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error !=null){
                            rr_notification.setVisibility(View.VISIBLE);
                            Log.e("Firestore error", error.getMessage());
                            return;
                        }else{
                            rr_notification.setVisibility(View.GONE);
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                if (documentChange.getNewIndex() < 8)
                                {
                                    mDataList.add(documentChange.getDocument().toObject(NotificationData.class));
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    private void initViews()
    {
        recycleView = findViewById(R.id.recycleView);
        rr_notification = findViewById(R.id.rr_notification);
        back_image = findViewById(R.id.back_image);
    }
}
package com.rehman.clicksonic.Lists;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.rehman.clicksonic.Adapter.AdapterUsers;
import com.rehman.clicksonic.Adapter.YouTubeAdapter;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.Model.YouTubeModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;

public class YouTubeListActivity extends AppCompatActivity {
    ImageView back_image;
    TextView totalCount;
    RecyclerView recyclerView;
    YouTubeAdapter adapter;
    ArrayList<YouTubeModel> mDataList = new ArrayList<>();
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_list);
        mAuth = FirebaseAuth.getInstance();

        intiView();
        getYouTubeList();
        back_image.setOnClickListener(v -> { onBackPressed(); });

    }

    private void getYouTubeList() {

        adapter = new YouTubeAdapter(this,mDataList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        FirebaseFirestore.getInstance().collection("YouTube")
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
                                mDataList.add(documentChange.getDocument().toObject(YouTubeModel.class));
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

        recyclerView=findViewById(R.id.youTube_recycler_view);
    }
}
package com.rehman.clicksonic.SubActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MakeScratchActivity extends AppCompatActivity {

    EditText ed_name,ed_price,ed_expire;
    ImageView back_image,img_scratch;
    Button btn_submit;
    CardView card_pickImage;
    String name,price,expire,scratchUrl;
    int priceCheck;
    Uri scratchUri;
    Bitmap scratchBitmap;
    ErrorTost errorTost = new ErrorTost(this);
    LoadingBar loadingBar = new LoadingBar(this);
    CurrentDateTime dateTime = new CurrentDateTime(this);
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_scratch);
        mAuth = FirebaseAuth.getInstance();

        initView();

        imgPic();

        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        btn_submit.setOnClickListener(v -> {
            loadingBar.ShowDialog("Please wait");
            name = ed_name.getText().toString();
            price = ed_price.getText().toString();
            expire = ed_expire.getText().toString();
            if (isValid(name,price,expire)){

                saveData();

            }

        });

    }

    private void saveData() {

        StorageReference reference = FirebaseStorage.getInstance().getReference("scratchImages")
                .child(System.currentTimeMillis() + "." + getFileExtenstion(scratchUri));
        reference.putFile(scratchUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        scratchUrl = uri.toString();
                        saveToFirebase();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.HideDialog();
            }
        });

    }

    private void saveToFirebase() {
        DocumentReference ref = FirebaseFirestore.getInstance().collection("scratchOffers").document();
        String id = ref.getId();

        Map<String,Object> map = new HashMap<>();
        map.put("name",name);
        map.put("price",price);
        map.put("expire",expire);
        map.put("scratchUrl",scratchUrl);
        map.put("registrationFormId",id);
        map.put("date",dateTime.getCurrentDate());
        map.put("time",dateTime.getTimeWithAmPm());


        FirebaseFirestore.getInstance().collection("scratchOffers").document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful())
                    {
                        loadingBar.HideDialog();
                        Toast.makeText(MakeScratchActivity.this, "Offer has been uploaded", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    Toast.makeText(MakeScratchActivity.this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                });
    }

    private void imgPic() {

        card_pickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 10);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10 && resultCode == RESULT_OK && data != null) {
            scratchUri = data.getData();
            try {
                scratchBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), scratchUri);
                img_scratch.setImageBitmap(scratchBitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }
    private String getFileExtenstion(Uri scratchUri)
    {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap typeMap = MimeTypeMap.getSingleton();
        return typeMap.getExtensionFromMimeType(contentResolver.getType(scratchUri));
    }

    private boolean isValid(String name, String price, String expire) {

        if (name.isEmpty()){
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Ticket Name is required");
            return false;
        }

        if (price.isEmpty()){
            loadingBar.HideDialog();
            errorTost.showErrorMessage("price is required");
            return false;
        }
        if (expire.isEmpty()){
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Expiry Date is required");
            return false;
        }
        if (scratchUri == null) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Scratch offer picture required");
            return false;
        }


        return true;
    }

    private void initView() {
        //EditText
        ed_name = findViewById(R.id.ed_name);
        ed_price = findViewById(R.id.ed_price);
        ed_expire = findViewById(R.id.ed_expire);
        //Button
        btn_submit = findViewById(R.id.btn_submit);
        //ImageView
        back_image = findViewById(R.id.back_image);
        img_scratch = findViewById(R.id.img_scratch);
        //CardView
        card_pickImage=findViewById(R.id.card_pickImage);

    }
}
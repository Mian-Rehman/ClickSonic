package com.rehman.clicksonic.Start;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rehman.clicksonic.Notification.NotificationData;
import com.rehman.clicksonic.Notification.SendNotification;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;

    ImageView back_image;
    EditText ed_name, ed_email, ed_password, ed_conPassword;
    Button btn_create;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    LoadingBar loadingBar = new LoadingBar(this);
    String fullName, email, password, conPassword, loginWith = "random", token, userUID, profileImageLink = "", notificationTime, notificationDate;
    int coin = 0, points = 0, bonus = 0, wallet = 0;
    boolean isGetNewAccountBonus;
    FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        initViews();


        back_image.setOnClickListener(v -> {
            onBackPressed();
            finish();
        });

        btn_create.setOnClickListener(v -> {

            loadingBar.ShowDialog("wait");

            fullName = ed_name.getText().toString().trim();
            email = ed_email.getText().toString().trim();
            password = ed_password.getText().toString().trim();
            conPassword = ed_conPassword.getText().toString().trim();

            if (isValid(fullName, email, password, conPassword)) {
                if (password.equals(conPassword)) {
                    createAccountWithEmailPassword(email, password);
                } else {
                    loadingBar.HideDialog();
                    errorTost.showErrorMessage("Password Not Match");
                }
            }
        });


    }

    private void createAccountWithEmailPassword(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            saveData(email, password);
                            // Sign in success, update UI with the signed-in user's information
//                            emailVerfication(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            loadingBar.HideDialog();
                            errorTost.showErrorMessage("failed to create account");
                        }
                    }
                });
    }

    private void saveData(String email, String password) {


        FirebaseUser user = mAuth.getCurrentUser();
        assert user != null;
        userUID = user.getUid();

        Map<String, Object> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("email", email);
        map.put("password", password);
        map.put("coin", coin);
        map.put("bonus", bonus);
        map.put("points", points);
        map.put("wallet", wallet);
        map.put("userUID", userUID);
        map.put("loginWith", loginWith);
        map.put("date", dateTime.getCurrentDate());
        map.put("time", dateTime.getTimeWithAmPm());
        map.put("isGetNewAccountBonus", isGetNewAccountBonus);
        map.put("profileImageLink", profileImageLink);

        FirebaseFirestore.getInstance().collection("users").document(userUID)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            loadingBar.HideDialog();
                            accountCreationDialog();
                            pushNotification();
                        }
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                        errorTost.showErrorMessage("something went wrong");
                    }
                });

    }

    private boolean isValid(String fullName, String email, String password, String conPassword) {
        if (fullName.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Name Required");
            return false;
        }

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Email Required");
            ed_email.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Enter Valid Password");
            return false;
        }

        if (conPassword.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Repeat Password");
            return false;
        }

        return true;
    }

    private void initViews() {
        back_image = findViewById(R.id.back_image);
        ed_name = findViewById(R.id.ed_name);
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        ed_conPassword = findViewById(R.id.ed_conPassword);
        btn_create = findViewById(R.id.btn_create);
    }

    private void accountCreationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.account_creation_dialog, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_signIn = layout_dialog.findViewById(R.id.btn_signIn);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_signIn.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            dialog.dismiss();
        });

    }

    private void pushNotification() {
        CurrentDateTime currentDateTime = new CurrentDateTime(this);
        notificationDate = currentDateTime.getCurrentDate();
        notificationTime = currentDateTime.getTimeWithAmPm();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(String s) {
                token = s;
                userUID = mAuth.getCurrentUser().getUid();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("token", token);

                FirebaseDatabase.getInstance().getReference("users").child(userUID)
                        .updateChildren(hashMap);
                String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                String notificationMessage = "Your account has been successfully registered";
                NotificationData notificationData = new NotificationData(notificationId, userUID, "signUp",
                        "Registration Successfull", notificationMessage, notificationDate, notificationTime, true);

                Map<String, Object> map = new HashMap<>();
                map.put("token", token);

                assert notificationId != null;
                FirebaseFirestore.getInstance().collection("users")
                        .document(userUID).collection("Notifications")
                        .document(notificationId).set(notificationData);


                SendNotification sendNotification = new SendNotification("Registration Successfull", notificationMessage,
                        token, userUID, SignUpActivity.this);
                sendNotification.sendNotification();
                notification();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notification() {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.transparent_logo, null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        assert bitmapDrawable != null;
        Bitmap bitmapIcon = bitmapDrawable.getBitmap();

        NotificationManager manager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(this)
                    .setLargeIcon(bitmapIcon)
                    .setSmallIcon(R.drawable.transparent_logo)
                    .setContentText("Account created successfully")
                    .setChannelId(CHANNEL_ID)
                    .build();

            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID, "NEW CHANNEL"
                    , NotificationManager.IMPORTANCE_HIGH));
        } else {
            notification = new Notification.Builder(this)
                    .setLargeIcon(bitmapIcon)
                    .setSmallIcon(R.drawable.transparent_logo)
                    .setContentText("Account created successfully")
                    .build();


        }

        manager.notify(NOTIFICATION_ID, notification);

    }

    private String getTimeWithAmPm() {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate() {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }
}
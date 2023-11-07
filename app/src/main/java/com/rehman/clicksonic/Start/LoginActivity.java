package com.rehman.clicksonic.Start;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.rehman.clicksonic.MainActivity;
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

public class LoginActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;
    public static final int PERMISSION_REQUEST_ENABLE_GPS = 102;
    public static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 103;

    private GoogleSignInClient mGoogleSignInClient;
    private final static int RC_SIGN_IN = 123;
    private boolean mLocationPermissionGranted = false;


    EditText ed_email,ed_password;
    TextView tv_forgot,account_text,tv_admin;
    Button btn_login;
    CardView google_card;
    RelativeLayout rr_error;
    LoadingBar loadingBar;
    ErrorTost errorTost;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userUID;
    String email,password,loginWith = "Google",token;
    boolean passwordVisible;

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user!=null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else{
            createRequest();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        loadingBar = new LoadingBar(this);
        errorTost = new ErrorTost(this);


        initViews();
        showPasswordVisibilityONOFF();

        account_text.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
        });

        btn_login.setOnClickListener(v -> {

        loadingBar.ShowDialog("Logging");

        email = ed_email.getText().toString().trim();
        password = ed_password.getText().toString().trim();

        if (isValid(email,password)){
            signWithEmailPassword(email,password);
            }
        });

        google_card.setOnClickListener(v -> {
            loadingBar.ShowDialog("please wait");
            signInGoogle();
        });

        tv_admin.setOnClickListener(v -> { startActivity(new Intent(LoginActivity.this,AdminActivity.class));});

    }

    private void signWithEmailPassword(String email, String password)
    {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                           loadingBar.HideDialog();
                        }else {
                            loadingBar.HideDialog();
                            errorTost.showErrorMessage("Login failed! please register account");
                        }

                    }
                });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void showPasswordVisibilityONOFF()
    {
        ed_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int right =2;

                if (event.getAction()==MotionEvent.ACTION_UP)
                {
                    if (event.getRawX() >= ed_password.getRight()-ed_password.getCompoundDrawables()[right].getBounds()
                            .width()){
                        int selectionPassword =ed_password.getSelectionEnd();
                        if (passwordVisible)
                        {
                            ed_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock,0,
                                    R.drawable.ic_visibility_off,0);
                            ed_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passwordVisible = false;
                        }else
                        {
                            ed_password.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_lock,0,
                                    R.drawable.ic_visibility,0);
                            ed_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passwordVisible = true;
                        }
                        ed_password.setSelection(selectionPassword);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private boolean isValid(String email, String password)
    {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!email.matches(emailPattern) || email.isEmpty())
        {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Email Required");
            ed_email.requestFocus();
            return false;
        }

        if (password.isEmpty()){
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Enter Valid Password");
            return false;
        }

        return true;
    }


    private void initViews()
    {
        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        tv_forgot = findViewById(R.id.tv_forgot);
        btn_login = findViewById(R.id.btn_login);
        account_text = findViewById(R.id.account_text);
        google_card = findViewById(R.id.google_card);
        tv_admin = findViewById(R.id.tv_admin);

    }

    private void createRequest()
    {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInGoogle()
    {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                // ...
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            switch (requestCode)
            {
                case PERMISSION_REQUEST_ENABLE_GPS:
                {
                    if(mLocationPermissionGranted)
                    {
                        Log.d("TAG", "onActivityResult: " + "permission Granted");
                    }
                    else
                    {
                        getPermission();
                    }
                }

            }

        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            progressDialog = ProgressDialog.show(LoginActivity.this, "Please wait", "Processing", true);
                            saveUserProfileData();



                        } else {
                            Toast.makeText(LoginActivity.this, "Sorry auth failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }

    private void saveUserProfileData()
    {
        userUID = mAuth.getCurrentUser().getUid();

        pushNotification();

        DocumentReference reference = FirebaseFirestore.getInstance().collection("users")
                .document(userUID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
            if (task.isSuccessful()){
                DocumentSnapshot snapshot = task.getResult();
                if (snapshot.exists()){
                    loadingBar.HideDialog();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                    }else{
                    saveData();
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void saveData()
    {
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        int coin = 0,bonus = 0,points = 0,wallet = 0;
        boolean isGetNewAccountBonus = false;
        String profileImageLink = "";


        Map<String,Object> map = new HashMap<>();
        assert acct != null;
        map.put("fullName",acct.getDisplayName());
        map.put("email",acct.getEmail());
        map.put("coin",coin);
        map.put("bonus",bonus);
        map.put("points",points);
        map.put("wallet",wallet);
        map.put("userUID",userUID);
        map.put("loginWith",loginWith);
        map.put("isGetNewAccountBonus",isGetNewAccountBonus);
        map.put("profileImageLink",acct.getPhotoUrl());

        FirebaseFirestore.getInstance().collection("users").document(userUID)
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){
                            loadingBar.HideDialog();
                            startActivity(new Intent(LoginActivity.this,MainActivity.class));
                            finish();
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

    private void pushNotification()
    {
       CurrentDateTime currentDateTime = new CurrentDateTime(this);
       String notificationDate = currentDateTime.getCurrentDate();
       String notificationTime = currentDateTime.getTimeWithAmPm();

        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onSuccess(String s) {
                token = s;
                userUID = mAuth.getCurrentUser().getUid();

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("token",token);

                FirebaseDatabase.getInstance().getReference("users").child(userUID)
                        .updateChildren(hashMap);
                String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
                String notificationMessage = "Your account has been successfully registered";
                NotificationData notificationData = new NotificationData(notificationId, userUID, "login",
                        "Registration Successfull", notificationMessage,notificationDate,notificationTime, true);

                Map<String,Object> map = new HashMap<>();
                map.put("token",token);

                assert notificationId != null;
                FirebaseFirestore.getInstance().collection("users")
                        .document(userUID).collection("Notifications")
                        .document(notificationId).set(notificationData);


                SendNotification sendNotification = new SendNotification("Registration Successfull", notificationMessage,
                        token, userUID, LoginActivity.this);
                sendNotification.sendNotification();
                notification();
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void notification()
    {
        Drawable drawable = ResourcesCompat.getDrawable(getResources(),R.drawable.transparent_logo,null);
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

            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"NEW CHANNEL"
                    , NotificationManager.IMPORTANCE_HIGH));
        }else{
            notification = new Notification.Builder(this)
                    .setLargeIcon(bitmapIcon)
                    .setSmallIcon(R.drawable.transparent_logo)
                    .setContentText("Account created successfully")
                    .build();


        }

        manager.notify(NOTIFICATION_ID,notification);

    }


    private void getPermission()
    {
        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
        )  == PackageManager.PERMISSION_GRANTED)
        {
            mLocationPermissionGranted = true;
        }
        else
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.CAMERA
                    },PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private String getTimeWithAmPm()
    {
        return new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
    }

    private String getCurrentdate()
    {
        return new SimpleDateFormat("dd/LLL/yyyy", Locale.getDefault()).format(new Date());
    }



}


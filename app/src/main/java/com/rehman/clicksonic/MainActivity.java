package com.rehman.clicksonic;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.MenuFrag.AboutFragment;
import com.rehman.clicksonic.MenuFrag.HomeFragment;
import com.rehman.clicksonic.MenuFrag.PolicyFragment;
import com.rehman.clicksonic.MenuFrag.ProfileFragment;
import com.rehman.clicksonic.MenuFrag.SettingFragment;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.Notification.NotificationActivity;
import com.rehman.clicksonic.Notification.NotificationData;
import com.rehman.clicksonic.Start.LoginActivity;
import com.rehman.clicksonic.Utils.CongratulationDialog;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.CustomNotification;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;

    NavigationView navMenu;
    ActionBarDrawerToggle toggle;
    DrawerLayout drayerLayout;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    Fragment temp=null;

    FirebaseAuth mAuth;
    FirebaseUser user;

    CustomNotification notification = new CustomNotification(this);

    String userUID,fullName,email,password,profileImageLink,loginWith,notificationDate,notificationTime;


    boolean isGetNewAccountBonus;
    int bonus,reward = 20;
    int coin,points;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getCurrentUser().getUid();
        preferences = getSharedPreferences("USER",MODE_PRIVATE);
         editor = preferences.edit();
        initViews();
        checkBonus();
        drawer();


    }

    private void initViews()
    {
        navMenu=findViewById(R.id.navMenu);
        drayerLayout=findViewById(R.id.drawerlayout);
    }

    private void drawer()
    {
        Toolbar toolbar=findViewById(R.id.Toolbar);
        setSupportActionBar(toolbar);

        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,new HomeFragment()).commit();

        toggle=new ActionBarDrawerToggle(this,drayerLayout,toolbar,R.string.app_name,R.string.app_name);
        drayerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navMenu.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId())
                {
                    case R.id.mm_dashboard:
                        toolbar.setTitle("Click Sonic");
                        temp=new HomeFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,temp).commit();
                        drayerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mm_profile:
                        toolbar.setTitle("Profile");
                        temp=new ProfileFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,temp).commit();
                        drayerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mm_about:
                        toolbar.setTitle("About Us");
                        temp=new AboutFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,temp).commit();
                        drayerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mm_policy:
                        toolbar.setTitle("Privacy Policy");
                        temp=new PolicyFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,temp).commit();
                        drayerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.mm_setting:
                        toolbar.setTitle("Setting");
                        temp=new SettingFragment();
                        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,temp).commit();
                        drayerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.mm_logout:
                        drayerLayout.closeDrawer(GravityCompat.START);
                        signOutDialog();
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu,menu);
        return super.onCreateOptionsMenu(menu);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.ic_notification:
                startActivity(new Intent(MainActivity.this, NotificationActivity.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkBonus()
    {
        DocumentReference reference = FirebaseFirestore.getInstance().collection("users")
                .document(userUID);
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    if (snapshot.exists()){
                        isGetNewAccountBonus = (boolean) snapshot.get("isGetNewAccountBonus");
                        coin = snapshot.getLong("coin").intValue();
                        points = snapshot.getLong("points").intValue();
                        bonus = snapshot.getLong("bonus").intValue();
                        fullName = snapshot.getString("fullName");
                        email = snapshot.getString("email");
                        password = snapshot.getString("password");
                        profileImageLink = snapshot.getString("profileImageLink");
                        loginWith = snapshot.getString("loginWith");

                        editor.putString("name",fullName);
                        editor.putString("email",email);
                        editor.putString("password",password);
                        editor.putString("profileImageLink",profileImageLink);
                        editor.putString("loginWith",loginWith);
                        editor.putString("coin",String.valueOf(coin));
                        editor.putString("points",String.valueOf(points));
                        editor.putString("bonus",String.valueOf(bonus));
                        editor.apply();
                        editor.commit();

                        if (isGetNewAccountBonus){
                            Log.d("TAG", "onComplete: ");
                        }else{
                            newAccountBonusPopup();
                        }
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    private void newAccountBonusPopup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.new_account_bonus_popup_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        Button btn_later = layout_dialog.findViewById(R.id.btn_later);
        Button btn_claim = layout_dialog.findViewById(R.id.btn_claim);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        btn_later.setOnClickListener(v -> {
            dialog.dismiss();
        });

        btn_claim.setOnClickListener(v -> {
            bonusClaimed(reward,dialog);
        });

    }

    private void bonusClaimed(int reward, AlertDialog dialog)
    {
        coin = coin + reward;
        isGetNewAccountBonus = true;
        Map<String,Object> map = new HashMap<>();
        map.put("coin",coin);
        map.put("isGetNewAccountBonus",isGetNewAccountBonus);

        FirebaseFirestore.getInstance().collection("users").document(userUID)
                .update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            dialog.dismiss();
                            saveNotifications();
                            notification.ShowNotification(R.drawable.ic_coin,"Coins Added to your wallet");
                            Toast.makeText(MainActivity.this, "coins added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    private void saveNotifications()
    {
        CurrentDateTime currentDateTime = new CurrentDateTime(this);
        notificationDate = currentDateTime.getCurrentDate();
        notificationTime = currentDateTime.getTimeWithAmPm();

        String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
        String notificationMessage = "Congratulation you received 20 coins";
        NotificationData notificationData = new NotificationData(notificationId, userUID, "bonus",
                "New Account Bonus", notificationMessage,notificationDate,notificationTime, true);

        assert notificationId != null;
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).collection("Notifications")
                .document(notificationId).set(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                   if (task.isSuccessful()){
                       Log.d("TAG", "onComplete: ");
                   }
                    }
                });

    }


    private void signOutDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        LinearLayout ll_yes = layout_dialog.findViewById(R.id.ll_yes);
        LinearLayout no_layout = layout_dialog.findViewById(R.id.no_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        ll_yes.setOnClickListener(v -> {
            mAuth.signOut();
            dialog.dismiss();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            Toast.makeText(this, "Sign out", Toast.LENGTH_SHORT).show();
        });

        no_layout.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void showAccountDeleteDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout_dialog = LayoutInflater.from(this).inflate(R.layout.alert_dialog_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        LinearLayout ll_yes = layout_dialog.findViewById(R.id.ll_yes);
        LinearLayout no_layout = layout_dialog.findViewById(R.id.no_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        ll_yes.setOnClickListener(v -> {
//            loadingDialogBar.ShowDialog("Deleting");
            deleteUserAccount();
            dialog.dismiss();
        });

        no_layout.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void deleteUserAccount()
    {
        if (user!=null){
            user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful())
                    {
                        Toast.makeText(MainActivity.this, "deleting", Toast.LENGTH_SHORT).show();
//                    removeFirebaseData();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
//                loadingDialogBar.HideDialog();
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(this, "no user found", Toast.LENGTH_SHORT).show();
        }

    }

}
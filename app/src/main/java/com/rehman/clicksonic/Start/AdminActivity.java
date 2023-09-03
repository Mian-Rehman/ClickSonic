package com.rehman.clicksonic.Start;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.rehman.clicksonic.Activity.AdminControlActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;

public class AdminActivity extends AppCompatActivity {

    ImageView back_image;
    EditText ed_email, ed_password;
    String email, password, adminEmail, adminPassword;
    Button btn_login;
    LoadingBar loadingBar;
    ErrorTost errorTost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        initViews();

        loadingBar = new LoadingBar(this);
        errorTost = new ErrorTost(this);

        back_image.setOnClickListener(v -> { onBackPressed(); });

        adminEmail = "admin";
        adminPassword = "admin";

        btn_login.setOnClickListener(v -> {
            loadingBar.ShowDialog("please wait");

            email = ed_email.getText().toString();
            password = ed_password.getText().toString();
            if (isValid(email,password)) {
                checkPassword();
            }
        });
    }

    private boolean isValid(String email, String password) {

        if (email.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Please enter email");
            return false;
        }
        if (password.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Please enter password");
            return false;
        }

        return true;
    }

    private void checkPassword() {

        if (ed_email.getText().toString().equals(adminEmail) && ed_password.getText().toString().equals(adminPassword)){
            startActivity(new Intent(AdminActivity.this, AdminControlActivity.class));
        }else {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("please enter correct information");
        }

    }

    private void initViews() {

        ed_email = findViewById(R.id.ed_email);
        ed_password = findViewById(R.id.ed_password);
        btn_login = findViewById(R.id.btn_login);

        back_image = findViewById(R.id.back_image);

    }
}
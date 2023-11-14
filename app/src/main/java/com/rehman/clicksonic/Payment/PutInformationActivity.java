package com.rehman.clicksonic.Payment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Activity.ReceiptActivity;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Utils.CurrentDateTime;
import com.rehman.clicksonic.Utils.ErrorTost;
import com.rehman.clicksonic.Utils.LoadingBar;
import com.rehman.clicksonic.Utils.PaymentDialog;

import java.util.HashMap;
import java.util.Map;

public class PutInformationActivity extends AppCompatActivity {
    String price,number,title,step1,step2,step3,step4,step5,step6,step7,verify,senderNumber,amount;
    TextView tv_title,tv_number,tv_name,tv_step1,tv_step2,tv_step3,tv_step4,tv_step5,tv_step6,tv_step7,tv_step8;
    EditText ed_verify,ed_number,ed_amount;
    ImageView img_copyLink,top_image;
    Button btn_submit;
    CurrentDateTime dateTime = new CurrentDateTime(this);
    ErrorTost errorTost = new ErrorTost(this);
    LoadingBar loadingBar = new LoadingBar(this);
    PaymentDialog paymentDialog =new PaymentDialog(this);
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_put_information);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        assert mUser != null;
        userUID = mUser.getUid();

        initValues();
        getIntentValues();

        img_copyLink.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String youtubeLink = number;
            ClipData clip = ClipData.newPlainText("YouTube Link", youtubeLink);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(this, "ClipBoard: "+youtubeLink, Toast.LENGTH_SHORT).show();
        });

        btn_submit.setOnClickListener(v -> {

            verify = ed_verify.getText().toString().trim();
            senderNumber = ed_number.getText().toString().trim();
            amount = ed_amount.getText().toString().trim();
            if (isValid(verify,senderNumber,amount)){
                loadingBar.ShowDialog("Please Wait");
                saveFirebaseData();
            }
        });
    }

    private void saveFirebaseData() {

        DocumentReference ref = FirebaseFirestore.getInstance().collection("OnlinePayment").document();
        String id = ref.getId();

        Map<String, Object> map = new HashMap<>();
        map.put("verifyTID", verify);
        map.put("senderNumber", senderNumber);
        map.put("amountTransferred", amount);
        map.put("paymentMethod", title);
        map.put("order", "Uncompleted");
        map.put("OrderID", id);
        map.put("userUID", userUID);
        map.put("Status", "pending");
        map.put("OrderDate", dateTime.getCurrentDate());
        map.put("OrderTime", dateTime.getTimeWithAmPm());

        FirebaseFirestore.getInstance().collection("OnlinePayment").document(id)
                .set(map).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadingBar.HideDialog();
                        paymentDialog.ShowDialog();
                    }
                }).addOnFailureListener(e -> {
                    loadingBar.HideDialog();
                    Toast.makeText(this, "Something went wrong! try again", Toast.LENGTH_SHORT).show();
                });

    }

    private boolean isValid(String verify, String senderNumber, String amount) {
        if (verify.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("TID is required");
            return false;
        }

        if (senderNumber.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Your number is required");
            return false;
        }

        if (amount.isEmpty()) {
            loadingBar.HideDialog();
            errorTost.showErrorMessage("Amount is required");
            return false;
        }
        return true;
    }

    private void initValues() {
        //TextView
        tv_title=findViewById(R.id.tv_title);
        tv_number=findViewById(R.id.tv_number);
        tv_name=findViewById(R.id.tv_name);
        tv_step1=findViewById(R.id.tv_step1);
        tv_step2=findViewById(R.id.tv_step2);
        tv_step3=findViewById(R.id.tv_step3);
        tv_step4=findViewById(R.id.tv_step4);
        tv_step5=findViewById(R.id.tv_step5);
        tv_step6=findViewById(R.id.tv_step6);
        tv_step7=findViewById(R.id.tv_step7);

        //editText
        ed_verify=findViewById(R.id.ed_verify);
        ed_number=findViewById(R.id.ed_number);
        ed_amount=findViewById(R.id.ed_amount);

        //ImageView
        top_image=findViewById(R.id.top_image);
        img_copyLink=findViewById(R.id.img_copyLink);

        //Button
        btn_submit=findViewById(R.id.btn_submit);

    }

    private void getIntentValues() {
//        price = getIntent().getExtras().getString("price");
        number = getIntent().getExtras().getString("number");
        title = getIntent().getExtras().getString("title");
        step1 = getIntent().getExtras().getString("step1");
        step2 = getIntent().getExtras().getString("step2");
        step3 = getIntent().getExtras().getString("step3");
        step4 = getIntent().getExtras().getString("step4");
        step5 = getIntent().getExtras().getString("step5");
        step6 = getIntent().getExtras().getString("step6");
        step7 = getIntent().getExtras().getString("step7");
        int image = getIntent().getIntExtra("image", 0);

//        ed_amount.setText(price);
        tv_number.setText(number);
        tv_title.setText(title);
        tv_step1.setText(step1);
        tv_step2.setText(step2);
        tv_step3.setText(step3);
        tv_step4.setText(step4);
        tv_step5.setText(step5);
        tv_step6.setText(step6);
        tv_step7.setText(step7);
        top_image.setImageResource(image);
    }
}
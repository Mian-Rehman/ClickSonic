package com.rehman.clicksonic.Payment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.rehman.clicksonic.R;

public class PaymentActivity extends AppCompatActivity {
    String price;
    CardView easyPaisa_card,jazzCash_card;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        initValues();
        getIntentValues();

        easyPaisa_card.setOnClickListener(v -> {
            Intent intent = new Intent(this,PutInformationActivity.class);
            intent.putExtra("price",price);
            intent.putExtra("title","EasyPaisa");
            intent.putExtra("number","03491437754");
            intent.putExtra("step1","Step 1: Copy Easypaisa Number and read Easypaisa account name");
            intent.putExtra("step2","Step 2 : Go phone a nd dial *786#");
            intent.putExtra("step3","Step 3 : press \"1 (Money Transfer)\"");
            intent.putExtra("step4","Step 4 : press \"1 (Mobile Account)\"\nNext: press \"1 (Easypaisa)\"");
            intent.putExtra("step5","Step 5 : Enter Number\nNext \"Enter Amount\" \nNext \"Enter 5 digit pin\"");
            intent.putExtra("step6","Step 6: wait 1 or 2 minute receive massage 3737 .Copy Trx id");
            intent.putExtra("step7","Step 7 : back to app (add fund ) past TID and write sender number and write amount");
            intent.putExtra("image",R.drawable.easypaisa1);
            startActivity(intent);
        });
        jazzCash_card.setOnClickListener(v -> {
            Intent intent = new Intent(this,PutInformationActivity.class);
            intent.putExtra("price",price);
            intent.putExtra("title","JazzCash");
            intent.putExtra("number","03286619121");
            intent.putExtra("step1","Step 1 : copy Jazzcash number and read Jazzcash account name");
            intent.putExtra("step2","Step 2 : Go phone and dial *786#");
            intent.putExtra("step3","Step 3 : press \"1 (Send Money)\"");
            intent.putExtra("step4","Step 4 : press \"1 (To Mobile Account)\"\nNext: press \"1 (To JazzCash)\"");
            intent.putExtra("step5","Step 5 : Enter Number\nNext \"Enter Amount\" \nNext \"Enter pin\"");
            intent.putExtra("step6","Step 6: wait 1 or 2 minute receive massage 3737 .Copy Trx id");
            intent.putExtra("step7","Step 7 :  Back to app (add fund ) select Jazzcash , past TID ,write sender number , write Amount");
            intent.putExtra("image",R.drawable.jazz_cash);
            startActivity(intent);
        });

    }

    private void initValues() {
        easyPaisa_card =findViewById(R.id.easyPaisa_card);
        jazzCash_card =findViewById(R.id.jazzCash_card);
    }

    private void getIntentValues() {
        price = getIntent().getExtras().getString("price");
    }
}
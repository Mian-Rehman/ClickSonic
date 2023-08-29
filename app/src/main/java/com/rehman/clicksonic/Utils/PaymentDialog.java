package com.rehman.clicksonic.Utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;

import com.rehman.clicksonic.MainActivity;
import com.rehman.clicksonic.R;

public class PaymentDialog {
    Context context;
    Dialog dialog;

    public PaymentDialog(Context context) {
        this.context = context;
    }

    public void ShowDialog(){
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.payment_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_claim = dialog.findViewById(R.id.btn_claim);
        btn_claim.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
        dialog.create();
        dialog.show();
    }

    public void HideDialog()
    {
        dialog.dismiss();
    }

}

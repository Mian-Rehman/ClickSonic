package com.rehman.clicksonic.Utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.Button;
import android.widget.TextView;

import com.rehman.clicksonic.R;

public class CongratulationDialog {
    Context context;
    Dialog dialog;

    public CongratulationDialog(Context context) {
        this.context = context;
    }

    public void ShowDialog(){
        dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.congratulation_dialog_layout);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_claim = dialog.findViewById(R.id.btn_claim);
        btn_claim.setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.create();
        dialog.show();
    }

    public void HideDialog()
    {
        dialog.dismiss();
    }

}

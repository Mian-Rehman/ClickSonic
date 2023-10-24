package com.rehman.clicksonic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehman.clicksonic.Model.PaymentModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;

public class AdapterPayment extends RecyclerView.Adapter<AdapterPayment.viewHolder> {
    Context context;
    ArrayList<PaymentModel> mPaymentList;

    public AdapterPayment(Context context, ArrayList<PaymentModel> mPaymentList) {
        this.context = context;
        this.mPaymentList = mPaymentList;
    }

    @NonNull
    @Override
    public AdapterPayment.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.payment_recycle_row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPayment.viewHolder holder, int position) {

        PaymentModel model = mPaymentList.get(position);
        holder.tv_amount.setText(model.getAmountTransferred());
        holder.tv_payment.setText(model.getPaymentMethod());
        holder.tv_tid.setText(model.getVerifyTID());
        holder.tv_orderID.setText(model.getOrderID());
    }

    @Override
    public int getItemCount() {
        return mPaymentList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView tv_amount, tv_payment, tv_tid, tv_orderID;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            tv_tid = itemView.findViewById(R.id.tv_tid);
            tv_orderID = itemView.findViewById(R.id.tv_orderID);

        }
    }
}

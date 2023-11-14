package com.rehman.clicksonic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehman.clicksonic.Model.BoughtModel;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;

public class AdapterBought extends RecyclerView.Adapter<AdapterBought.viewHolder> {
    Context context;
    ArrayList<BoughtModel> mBoughtList;

    public AdapterBought(Context context, ArrayList<BoughtModel> mBoughtList) {
        this.context = context;
        this.mBoughtList = mBoughtList;
    }

    @NonNull
    @Override
    public AdapterBought.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.bought_recycle_row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterBought.viewHolder holder, int position) {

        BoughtModel model = mBoughtList.get(position);

        holder.tv_offerBought.setText(model.getOfferBought());
        holder.tv_moneySpent.setText(model.getMoneySpent());
        holder.tv_userUID.setText(model.getUserUID());
        holder.tv_dateOfPurchase.setText(model.getDateOfPurchase());
    }

    @Override
    public int getItemCount() {
        return mBoughtList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView tv_offerBought, tv_moneySpent, tv_userUID, tv_dateOfPurchase;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_offerBought = itemView.findViewById(R.id.tv_offerBought);
            tv_moneySpent = itemView.findViewById(R.id.tv_moneySpent);
            tv_userUID = itemView.findViewById(R.id.tv_userUID);
            tv_dateOfPurchase = itemView.findViewById(R.id.tv_dateOfPurchase);

        }
    }
}

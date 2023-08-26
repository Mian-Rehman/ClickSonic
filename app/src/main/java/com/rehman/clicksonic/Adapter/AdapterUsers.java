package com.rehman.clicksonic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.viewHolder>{
    Context context;
    ArrayList<UserModel> mUserList;

    public AdapterUsers(Context context, ArrayList<UserModel> mUserList) {
        this.context = context;
        this.mUserList = mUserList;
    }

    @NonNull
    @Override
    public AdapterUsers.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.user_recycle_row , parent,false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterUsers.viewHolder holder, int position) {

        UserModel model = mUserList.get(position);
        holder.tv_name.setText(model.getFullName());
        holder.tv_email.setText(model.getEmail());
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder{

        TextView tv_name,tv_email;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.tv_name);
            tv_email=itemView.findViewById(R.id.tv_email);

        }
    }
}

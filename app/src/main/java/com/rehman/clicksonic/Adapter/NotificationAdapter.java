package com.rehman.clicksonic.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rehman.clicksonic.Notification.NotificationData;
import com.rehman.clicksonic.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    Context context;
    ArrayList<NotificationData> mDataList;

    public NotificationAdapter(Context context, ArrayList<NotificationData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.notification_list_layout,parent,false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        NotificationData data  = mDataList.get(position);
        holder.title_text.setText(data.getNotificationTitle());
        holder.message_text.setText(data.getNotificationMessage());
        holder.date_text.setText(data.getNotificationDate());
        holder.time_text.setText(data.getNotificationTime());

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title_text,message_text,date_text,time_text;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            title_text = itemView.findViewById(R.id.title_text);
            message_text = itemView.findViewById(R.id.message_text);
            date_text = itemView.findViewById(R.id.date_text);
            time_text = itemView.findViewById(R.id.time_text);
        }
    }

}

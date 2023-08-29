package com.rehman.clicksonic.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rehman.clicksonic.Model.YouTubeModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;

public class FacebookAdapter extends RecyclerView.Adapter<FacebookAdapter.viewHolder> {
    Context context;
    ArrayList<YouTubeModel> mYouTubeList;

    public FacebookAdapter(Context context, ArrayList<YouTubeModel> mYouTubeList) {
        this.context = context;
        this.mYouTubeList = mYouTubeList;
    }

    @NonNull
    @Override
    public FacebookAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.instagram_recycle_row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FacebookAdapter.viewHolder holder, int position) {

        YouTubeModel model = mYouTubeList.get(position);
        holder.tv_name.setText(model.getFullName());
        holder.tv_link.setText(model.getLink());
        holder.tv_subscriber.setText(model.getFollowers());
        if (model.getFollowers() == null) {
            holder.ll_sub.setVisibility(View.GONE);
        } else {
            holder.ll_sub.setVisibility(View.VISIBLE);
        }
        holder.img_copyLink.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String youtubeLink = model.getLink();
            ClipData clip = ClipData.newPlainText("YouTube Link", youtubeLink);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "ClipBoard: "+youtubeLink, Toast.LENGTH_SHORT).show();
        });
        holder.tv_views.setText(model.getViews());
        if (model.getViews() == null) {
            holder.ll_view.setVisibility(View.GONE);
        } else {
            holder.ll_view.setVisibility(View.VISIBLE);
        }
        holder.tv_likes.setText(model.getLikes());
        if (model.getLikes() == null) {
            holder.ll_like.setVisibility(View.GONE);
        } else {
            holder.ll_like.setVisibility(View.VISIBLE);
        }
        holder.tv_cost.setText(String.valueOf(model.getCost()));
    }

    @Override
    public int getItemCount() {
        return mYouTubeList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView tv_name,tv_link,tv_cost,tv_subscriber,tv_likes,tv_views;
        ImageView img_copyLink;
        LinearLayout ll_view,ll_like,ll_sub;

        public viewHolder(@NonNull View itemView) {
            super(itemView);

            tv_name = itemView.findViewById(R.id.tv_name);
            tv_link = itemView.findViewById(R.id.tv_link);
            tv_cost = itemView.findViewById(R.id.tv_cost);
            tv_subscriber = itemView.findViewById(R.id.tv_subscriber);
            tv_likes = itemView.findViewById(R.id.tv_likes);
            tv_views = itemView.findViewById(R.id.tv_views);

            img_copyLink = itemView.findViewById(R.id.img_copyLink);

            ll_view = itemView.findViewById(R.id.ll_view);
            ll_like = itemView.findViewById(R.id.ll_like);
            ll_sub = itemView.findViewById(R.id.ll_sub);

        }
    }
}

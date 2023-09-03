package com.rehman.clicksonic.Adapter;

import static androidx.core.content.ContextCompat.getSystemService;

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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Model.UserModel;
import com.rehman.clicksonic.Model.YouTubeModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.viewHolder> {
    Context context;
    ArrayList<YouTubeModel> mYouTubeList;

    public YouTubeAdapter(Context context, ArrayList<YouTubeModel> mYouTubeList) {
        this.context = context;
        this.mYouTubeList = mYouTubeList;
    }

    @NonNull
    @Override
    public YouTubeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.youtube_recycle_row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeAdapter.viewHolder holder, int position) {

        YouTubeModel model = mYouTubeList.get(position);
        holder.tv_name.setText(model.getFullName());
        holder.tv_link.setText(model.getLink());
        holder.tv_subscriber.setText(model.getSubscriber());
        if (model.getSubscriber() == null) {
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
        holder.card_pendingAdp.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();

            Map<String, Object> map = new HashMap<>();
            map.put("Status","pending");
            FirebaseFirestore.getInstance().collection("YouTube")
                    .document(model.getOrderID())
                    .update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            model.setStatus("pending");
                            notifyItemChanged(clickedPosition);
                            Toast.makeText(context, "Status updated to pending", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.card_approvedAdp.setOnClickListener(v -> {

            int clickedPosition = holder.getAdapterPosition();

            Map<String, Object> map = new HashMap<>();
            map.put("Status","approved");
            FirebaseFirestore.getInstance().collection("YouTube")
                    .document(model.getOrderID())
                    .update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Data updated successfully in Firestore
                            // You can also update the local data if needed
                            model.setStatus("approved");
                            notifyItemChanged(clickedPosition); // Notify the adapter of the data change
                            Toast.makeText(context, "Status updated to approved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle any errors that occurred during the update
                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        holder.card_rejectedAdp.setOnClickListener(v -> {

            int clickedPosition = holder.getAdapterPosition();

            Map<String, Object> map = new HashMap<>();
            map.put("Status","rejected");
            FirebaseFirestore.getInstance().collection("YouTube")
                    .document(model.getOrderID())
                    .update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            model.setStatus("rejected");
                            notifyItemChanged(clickedPosition);
                            Toast.makeText(context, "Status updated to rejected", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    });
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
        CardView card_pendingAdp,card_approvedAdp,card_rejectedAdp;

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
            //CardView
            card_pendingAdp = itemView.findViewById(R.id.card_pendingAdp);
            card_approvedAdp = itemView.findViewById(R.id.card_approvedAdp);
            card_rejectedAdp = itemView.findViewById(R.id.card_rejectedAdp);

        }
    }
}

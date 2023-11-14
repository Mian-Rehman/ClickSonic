package com.rehman.clicksonic.Adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Model.PaymentModel;
import com.rehman.clicksonic.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AdminPaymentAdapter extends RecyclerView.Adapter<AdminPaymentAdapter.viewHolder> {
    Context context;
    ArrayList<PaymentModel> mPaymentList;

    public AdminPaymentAdapter(Context context, ArrayList<PaymentModel> mPaymentList) {
        this.context = context;
        this.mPaymentList = mPaymentList;
    }

    @NonNull
    @Override
    public AdminPaymentAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.admin_payment_recycle_row, parent, false);

        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminPaymentAdapter.viewHolder holder, int position) {

        PaymentModel model = mPaymentList.get(position);
        holder.tv_amount.setText(model.getAmountTransferred());
        holder.tv_payment.setText(model.getPaymentMethod());
        holder.tv_tid.setText(model.getVerifyTID());
        holder.tv_orderID.setText(model.getOrderID());

        holder.img_copyLink.setOnClickListener(v -> {

            ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            String TID = model.getVerifyTID();
            ClipData clip = ClipData.newPlainText("TID Link", TID);
            assert clipboard != null;
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "ClipBoard: "+TID, Toast.LENGTH_SHORT).show();
        });

        holder.card_pendingAdp.setOnClickListener(v -> {
            int clickedPosition = holder.getAdapterPosition();

            Map<String, Object> map = new HashMap<>();
            map.put("Status","pending");
            FirebaseFirestore.getInstance().collection("OnlinePayment")
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

                isValid(model.getOrderID(), new OnPaymentStatusFetchedListener() {
                    @Override
                    public void onPaymentStatusFetched(boolean isApproved) {
                        if (!isApproved) {
                            fetchUserWalletAmount(model.getUserUID(), new OnWalletAmountFetchedListener() {
                                @Override
                                public void onWalletAmountFetched(int walletAmount) {

                                    int newWalletAmount = walletAmount + Integer.parseInt(model.getAmountTransferred());

                                    updateWalletAmount(model.getUserUID(), newWalletAmount);
                                    Toast.makeText(context, "Wallet has been updated", Toast.LENGTH_SHORT).show();

                                    Map<String, Object> map = new HashMap<>();
                                    map.put("Status","approved");
                                    map.put("order","completed");
                                    FirebaseFirestore.getInstance().collection("OnlinePayment")
                                            .document(model.getOrderID())
                                            .update(map)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    model.setStatus("approved");
                                                    model.setOrder("completed");

                                                    notifyItemChanged(clickedPosition); // Notify the adapter of the data change
                                                    Toast.makeText(context, "Status updated to approved", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }

                                @Override
                                public void onWalletAmountFetchFailed() {
                                    Toast.makeText(context, "Failed to fetch user's wallet amount", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(context, "Payment was already Approved", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPaymentStatusFetchFailed() {
                        Toast.makeText(context, "Failed to fetch payment status", Toast.LENGTH_SHORT).show();
                    }
                });


            int money;
            Map<String, Object> map = new HashMap<>();
            map.put("Status","approved");
            FirebaseFirestore.getInstance().collection("OnlinePayment")
                    .document(model.getOrderID())
                    .update(map)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            model.setStatus("approved");

                            notifyItemChanged(clickedPosition); // Notify the adapter of the data change
                            Toast.makeText(context, "Status updated to approved", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, "Failed to update status", Toast.LENGTH_SHORT).show();
                        }
                    });



        });

        holder.card_rejectedAdp.setOnClickListener(v -> {

            int clickedPosition = holder.getAdapterPosition();

            Map<String, Object> map = new HashMap<>();
            map.put("Status","rejected");
            FirebaseFirestore.getInstance().collection("OnlinePayment")
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
    }

    private void fetchUserWalletAmount(String userId, OnWalletAmountFetchedListener listener) {
        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int walletAmount = documentSnapshot.getLong("wallet").intValue();
                        listener.onWalletAmountFetched(walletAmount);
                    } else {
                        listener.onWalletAmountFetchFailed();
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onWalletAmountFetchFailed();
                });
    }

    private void isValid(String orderId, OnPaymentStatusFetchedListener listener) {
        FirebaseFirestore.getInstance().collection("OnlinePayment")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String status = documentSnapshot.getString("order");
                        if (status != null && status.equals("completed")) {
                            listener.onPaymentStatusFetched(true);
                        } else {
                            listener.onPaymentStatusFetched(false);
                        }
                    } else {
                        listener.onPaymentStatusFetchFailed();
                        Toast.makeText(context, "Document not fetched", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    listener.onPaymentStatusFetchFailed();
                    Toast.makeText(context, "Error: Try again", Toast.LENGTH_SHORT).show();
                });
    }
    interface OnPaymentStatusFetchedListener {
        void onPaymentStatusFetched(boolean isApproved);

        void onPaymentStatusFetchFailed();
    }
    private void updateWalletAmount(String userId, int newWalletAmount) {
        Map<String, Object> walletUpdate = new HashMap<>();
        walletUpdate.put("wallet", newWalletAmount);

        FirebaseFirestore.getInstance().collection("users")
                .document(userId)
                .update(walletUpdate)
                .addOnFailureListener(e -> {
                    Toast.makeText(context, "Wallet wasn't Updated", Toast.LENGTH_SHORT).show();
                });
    }
    // Listener interface for wallet amount fetching
    interface OnWalletAmountFetchedListener {
        void onWalletAmountFetched(int walletAmount);

        void onWalletAmountFetchFailed();
    }
    @Override
    public int getItemCount() {
        return mPaymentList.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView tv_amount,tv_payment,tv_tid,tv_orderID;
        ImageView img_copyLink;
        CardView card_pendingAdp,card_approvedAdp,card_rejectedAdp;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            //TextView
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_payment = itemView.findViewById(R.id.tv_payment);
            tv_tid = itemView.findViewById(R.id.tv_tid);
            tv_orderID = itemView.findViewById(R.id.tv_orderID);
            //ImageView
            img_copyLink = itemView.findViewById(R.id.img_copyLink);
            //CardView
            card_pendingAdp = itemView.findViewById(R.id.card_pendingAdp);
            card_approvedAdp = itemView.findViewById(R.id.card_approvedAdp);
            card_rejectedAdp = itemView.findViewById(R.id.card_rejectedAdp);
        }
    }
}

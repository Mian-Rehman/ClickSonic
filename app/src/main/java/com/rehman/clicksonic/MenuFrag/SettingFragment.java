package com.rehman.clicksonic.MenuFrag;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.R;
import com.rehman.clicksonic.Start.LoginActivity;
import com.rehman.clicksonic.Utils.LoadingBar;


public class SettingFragment extends Fragment {

    CardView delete_card;

    LoadingBar loadingBar;

    FirebaseAuth mAuth;
    FirebaseUser user;
    String userUID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        loadingBar = new LoadingBar(getActivity());
        initViews(view);

        delete_card.setOnClickListener(v -> {
            showAccountDeleteDialog();
        });

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void showAccountDeleteDialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View layout_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.alert_dialog_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        LinearLayout ll_yes = layout_dialog.findViewById(R.id.ll_yes);
        LinearLayout no_layout = layout_dialog.findViewById(R.id.no_layout);
        TextView des_text = layout_dialog.findViewById(R.id.des_text);

        des_text.setText("Are you sure to Delete Account?");

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        ll_yes.setOnClickListener(v -> {
            loadingBar.ShowDialog("Deleting");
            deleteUserAccount();
            dialog.dismiss();
        });

        no_layout.setOnClickListener(v -> {
            dialog.dismiss();
        });
    }

    private void deleteUserAccount()
    {
        mAuth.signOut();
        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful())
                {
                    removeFirebaseData();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingBar.HideDialog();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void removeFirebaseData()
    {

        FirebaseFirestore.getInstance().collection("users")
                        .document(userUID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            AccountDeleteDialog();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loadingBar.HideDialog();
                        Toast.makeText(getActivity(), "went wrong!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void AccountDeleteDialog()
    {

        loadingBar.HideDialog();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View layout_dialog = LayoutInflater.from(getActivity()).inflate(R.layout.delete_complete_layout, null);
        builder.setView(layout_dialog);

        //Show Dialog
        LinearLayout ok_layout = layout_dialog.findViewById(R.id.ok_layout);

        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.getWindow().setWindowAnimations(R.style.alertDialogAnimation);

        ok_layout.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
            dialog.dismiss();
        });
    }


    private void initViews(View view)
    {
        delete_card = view.findViewById(R.id.delete_card);
    }
}
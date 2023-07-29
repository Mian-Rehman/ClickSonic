package com.rehman.clicksonic.Utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.rehman.clicksonic.Notification.NotificationData;

public class SavedNotification {
    Context context;
    CurrentDateTime currentDateTime;

    public SavedNotification(Context context) {
        this.context = context;
    }

    public void savedNotificationData(String userUID,String notificationTitle,String notificationMessage
                                      ,String notificationType,boolean seen)
    {
        currentDateTime = new CurrentDateTime(context);
        String notificationDate = currentDateTime.getCurrentDate();
        String notificationTime = currentDateTime.getTimeWithAmPm();
        String notificationId = FirebaseDatabase.getInstance().getReference().push().getKey();
        NotificationData notificationData = new NotificationData(notificationId, userUID, notificationType,
                notificationTitle, notificationMessage,notificationDate,notificationTime, seen);

        assert notificationId != null;
        FirebaseFirestore.getInstance().collection("users")
                .document(userUID).collection("Notifications")
                .document(notificationId).set(notificationData).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.d("TAG", "onComplete: ");
                        }
                    }
                });
    }

}

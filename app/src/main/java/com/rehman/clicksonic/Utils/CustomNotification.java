package com.rehman.clicksonic.Utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.res.ResourcesCompat;

import com.rehman.clicksonic.R;

public class CustomNotification {
    private static final String CHANNEL_ID = "My Channel";
    private static final int NOTIFICATION_ID = 100;

    Context context;

    public CustomNotification(Context context) {
        this.context = context;
    }

    public void ShowNotification(int rightNotificationIcon,String text){
        Drawable drawable = ResourcesCompat.getDrawable(context.getResources(),rightNotificationIcon,null);
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        assert bitmapDrawable != null;
        Bitmap bitmapIcon = bitmapDrawable.getBitmap();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(context)
                    .setLargeIcon(bitmapIcon)
                    .setSmallIcon(R.drawable.transparent_logo)
                    .setContentText(text)
                    .setChannelId(CHANNEL_ID)
                    .build();

            manager.createNotificationChannel(new NotificationChannel(CHANNEL_ID,"NEW CHANNEL"
                    , NotificationManager.IMPORTANCE_HIGH));
        }else{
            notification = new Notification.Builder(context)
                    .setLargeIcon(bitmapIcon)
                    .setSmallIcon(R.drawable.transparent_logo)
                    .setContentText(text)
                    .build();

        }

        manager.notify(NOTIFICATION_ID,notification);
    }
}

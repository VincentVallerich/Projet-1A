package ensisa.group5.confined.controller;


import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.content.Context;
import android.content.ContextWrapper;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.RequiresApi;

import ensisa.group5.confined.R;

public class NotificationHelper extends ContextWrapper {

    private static final String CHANNEL_DEFAULT_ID = "com.infinisoftware.testnotifs.DEFAULT_CHANNEL";
    private static final int NOTIF_ID = 123;
    private Context context;


    public NotificationHelper(Context base ) {
        super( base );
        this.context = base;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    public void notify(int id, String title, String message, int largeIcon ) {
        Resources res = context.getResources();
        //  l'activité à ouvrir : ici MainActivity
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context, 456, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        Notification notification = null;     // avant l'API 16


        notification = new Notification.Builder(context, CHANNEL_DEFAULT_ID)
                .setSmallIcon(R.drawable.taskicon_task_chef_icon)     // drawable for API 26
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.taskicon_maid))
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentTitle("A notification title")
                .setContentText( "Full message" )
                .setContentIntent( contentIntent )      // On injecte le contentIntent
                .setVibrate(new long[] { 0, 500, 110, 500, 110, 450, 110, 200, 110,
                        170, 40, 450, 110, 200, 110, 170, 40, 500 } )
                .setLights(Color.RED, 3000, 3000)
                .build();             // à partir de l'API 16

        NotificationManager notifManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifManager.notify( NOTIF_ID, notification );
    }
}
package com.ka8eem.market24.Notification;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ka8eem.market24.ui.activities.CattingActivity;

import static com.ka8eem.market24.ui.activities.App.CHANNEL_ID;

public class MyFirebaseMessageing   extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        //Toast.makeText(this, "My notifications", Toast.LENGTH_SHORT).show();
        String sented = remoteMessage.getData().get("sented");
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        sendNotification(remoteMessage);
        if(firebaseUser != null && sented.equals(firebaseUser.getUid()))
        {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                sendOreoNotification(remoteMessage);
            }else {
                sendNotification(remoteMessage);
            }

        }
    }
private  void sendOreoNotification(RemoteMessage remoteMessage){
    String user = remoteMessage.getData().get("user");
    String icon = remoteMessage.getData().get("icon");
    String title = remoteMessage.getData().get("title");
    String body = remoteMessage.getData().get("body");
    String id_ads = remoteMessage.getData().get("id_ADS");
    String name_ads = remoteMessage.getData().get("name_ADS");
    String img_ads = remoteMessage.getData().get("img_ADS");


    RemoteMessage.Notification notification = remoteMessage.getNotification();
    int j = Integer.parseInt(user.replaceAll("[\\D]" , ""));
    Intent intent = new Intent(this , CattingActivity.class);
    Bundle bundle = new Bundle();

    bundle.putString("ID_ADS", id_ads);
    bundle.putString("IMG_ADS", img_ads);
    bundle.putString("name_ADS",name_ads );
    bundle.putString("id_user" , user);
    intent.putExtras(bundle);

    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this , j , intent , PendingIntent.FLAG_ONE_SHOT);
    Uri defultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

    OreoNotification oreoNotification = new OreoNotification(this);
    Notification.Builder builder = oreoNotification.getOreoNotification(title , body , pendingIntent, defultSound , icon);
    int i = 0 ;
    if(j > 0 ){
        i = j ;
    }
    oreoNotification.getManager().notify(i , builder.build());

    }
    private void sendNotification(RemoteMessage remoteMessage) {

        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");
        String id_ads = remoteMessage.getData().get("id_ADS");
        String name_ads = remoteMessage.getData().get("name_ADS");
        String img_ads = remoteMessage.getData().get("img_ADS");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]" , ""));
        Intent intent = new Intent(this , CattingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ID_ADS", id_ads);
        bundle.putString("IMG_ADS", img_ads);
        bundle.putString("name_ADS",name_ads );
        bundle.putString("id_user" , user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this , j , intent , PendingIntent.FLAG_ONE_SHOT);
        Uri defultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this ,CHANNEL_ID )
                .setSmallIcon(Integer.parseInt(icon))
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defultSound)
                .setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        int i = 0 ;
        if(j > 0 ){
            i = j ;
        }
        noti.notify(i , builder.build());

    }
}

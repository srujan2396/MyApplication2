package com.example.srujansai.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by srujan sai on 10-07-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            if(remoteMessage.getData().get("notification").equals("true")){


            String title = remoteMessage.getData().get("title");
            String icon= remoteMessage.getData().get("icon");
            String body=remoteMessage.getData().get("body");
            String click_action=remoteMessage.getData().get("click_action");
            String phno=remoteMessage.getData().get("phno");
            String name=remoteMessage.getData().get("name");
            String fromname=remoteMessage.getData().get("fromname");
            String fromphno= remoteMessage.getData().get("fromphno");

            sendNotification(title,icon,body,click_action,phno,name,fromname,fromphno);

            if (/* Check if data needs to be processed by long running job */ true) {
           //     // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
             //   scheduleJob();
            } else {
                // Handle message within 10 seconds
              //  handleNow();
            }//
        }
            if(remoteMessage.getData().get("notification").equals("false")){
                String fname=remoteMessage.getData().get("fname");
                String fphno=remoteMessage.getData().get("fphno");
                String selfname=remoteMessage.getData().get("selfname");
                String selfphne=remoteMessage.getData().get("selfphno");
                String status=remoteMessage.getData().get("status");
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    DatabaseReference nr = FirebaseDatabase.getInstance().getReference().child("USERS").child(user.getUid()).child("friendslist").child(selfphne);
                    HashMap<String, String> friendslist = new HashMap<String, String>();
                    friendslist.put("fname", selfname);
                    friendslist.put("phno", selfphne);
                    friendslist.put("status", status);
                    nr.setValue(friendslist);
                    System.out.println("updated in Firebase list");
                }else{
                    System.out.println("user is not sign in");
                }
            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            String title=remoteMessage.getNotification().getTitle();
            String message=remoteMessage.getNotification().getBody();
            String click_action=remoteMessage.getNotification().getClickAction();
            Intent intent = new Intent(click_action);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent= PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this);
            notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText(message);
            notificationBuilder.setSmallIcon(R.drawable.ic_action_locatio);
            notificationBuilder.setSound(defaultSoundUri);
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0,notificationBuilder.build());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // m
    }

    private void sendNotification(String title, String icon, String body, String click_action, String phno, String name,String fromname,String fromphno) {
        Intent intent = new Intent(this, RequestActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("title", title);
        intent.putExtra("icon", icon);
        intent.putExtra("body", body);
        intent.putExtra("phno", phno);
        intent.putExtra("name",name);
        intent.putExtra("fromname",fromname);
        intent.putExtra("fromphno",fromphno);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder= new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("TRACKO Request from "+fromname);
        notificationBuilder.setContentText(fromname+" Phno: "+fromphno);
        notificationBuilder.setSmallIcon(R.drawable.ic_action_locatio);
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notificationBuilder.build());
    }
}

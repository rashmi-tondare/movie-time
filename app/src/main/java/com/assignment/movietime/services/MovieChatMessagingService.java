
package com.assignment.movietime.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.assignment.movietime.R;
import com.assignment.movietime.view.activities.BaseActivity;
import com.assignment.movietime.view.activities.ChatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Rashmi on 20/08/17.
 */

public class MovieChatMessagingService extends FirebaseMessagingService {

    private static final String TAG = MovieChatMessagingService.class.getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Handle data payload of FCM messages.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();
        if (currentUser != null && currentUser.getDisplayName().equals(title)) {
            return;
        }

        displayNotification(title, body);
    }

    @SuppressWarnings("deprecation")
    private void displayNotification(String title, String body) {
        if (BaseActivity.currentActivity.getClass().getSimpleName().equals(ChatActivity.class.getSimpleName())) {
            return;
        }

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int defaults = 0;
        defaults = defaults | Notification.DEFAULT_LIGHTS
                   | Notification.DEFAULT_SOUND
                   | Notification.FLAG_AUTO_CANCEL;

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_movie)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                .setAutoCancel(true)
                .setDefaults(defaults)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        notificationManager.notify(1, builder.build());
    }
}

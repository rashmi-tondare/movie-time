
package com.assignment.movietime.services;

import android.util.Log;

import com.assignment.movietime.constants.AppConstants;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by Rashmi on 20/08/17.
 */

public class MovieTimeInstanceIdService extends FirebaseInstanceIdService {
    private static final String TAG = MovieTimeInstanceIdService.class.getSimpleName();

    /**
     * The Application's current Instance ID token is no longer valid
     * and thus a new one must be requested.
     */
    @Override
    public void onTokenRefresh() {
        // If you need to handle the generation of a token, initially or
        // after a refresh this is where you should do that.
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "FCM Token: " + token);

        // Once a token is generated, we subscribe to topic.
        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.MESSAGE_NOTIFICATIONS_TOPIC);
    }
}

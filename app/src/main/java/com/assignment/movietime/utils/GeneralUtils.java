
package com.assignment.movietime.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Rashmi on 18/08/17.
 */

public class GeneralUtils {

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm =
                               (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
               activeNetwork.isConnectedOrConnecting();
    }

    public static String formatToOneDecimal(float value) {
        return String.format(Locale.getDefault(), "%.1f", value);
    }

    public static String formatDate(String dateString) {
        if (TextUtils.isEmpty(dateString))
            return "";

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = simpleDateFormat.parse(dateString);
            simpleDateFormat = new SimpleDateFormat("dd MMM, yyyy", Locale.getDefault());
            return simpleDateFormat.format(date);
        }
        catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String formatTime(int durationInMinutes) {
        StringBuilder sb = new StringBuilder();
        int hrs = durationInMinutes / 60;

        if (hrs > 0) {
            sb.append((durationInMinutes / 60)).append(hrs == 1 ? " hr" : " hrs");
        }
        int mins = durationInMinutes % 60;
        if (mins > 0) {
            if (hrs > 0) {
                sb.append(", ");
            }
            sb.append(mins).append(mins == 1 ? " min" : " mins");
        }
        return sb.toString();
    }
}

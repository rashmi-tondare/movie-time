
package com.assignment.movietime.utils;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.assignment.movietime.view.activities.BaseActivity;

/**
 * Created by Rashmi on 18/08/17.
 */

public class SnackbarUtils {

    public static void displaySnackbar(String message) {
        if (BaseActivity.currentActivity != null) {
            AppCompatActivity activity = BaseActivity.currentActivity;
            Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView().findViewById(android.R.id.content),
                    message,
                    Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    public static void displaySnackbar(int resId) {
        if (BaseActivity.currentActivity != null) {
            displaySnackbar(BaseActivity.currentActivity.getString(resId));
        }
    }

    public static void displaySnackbar(Throwable throwable) {
        if (throwable != null && !TextUtils.isEmpty(throwable.getMessage())) {
            displaySnackbar(throwable.getMessage());
        }
    }
}

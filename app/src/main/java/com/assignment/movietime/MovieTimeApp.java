package com.assignment.movietime;

import android.app.Application;

import com.assignment.movietime.network.MovieTimeApiHandler;
import com.facebook.stetho.Stetho;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieTimeApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        MovieTimeApiHandler.init(this);

        // Helpful for debugging network requests, viewing local db & shared prefs
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}

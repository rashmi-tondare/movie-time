
package com.assignment.movietime.network;

import android.util.Log;

import com.assignment.movietime.BuildConfig;
import com.assignment.movietime.MovieTimeApp;
import com.assignment.movietime.error.SingletonAlreadyInitializedException;
import com.assignment.movietime.error.SingletonNotInitializedException;
import com.assignment.movietime.utils.GeneralUtils;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieTimeApiClient {
    private static final String TAG = MovieTimeApiClient.class.getSimpleName();
    private static final String CACHE_FILE_NAME = "response_cache";
    private static final int CACHE_SIZE = 10 * 1024 * 1024; //10 Mb

    private static MovieTimeApiClient movieTimeApiClient;

    private MovieTimeApiInterface apiInterface;
    private MovieTimeApp appContext;

    private MovieTimeApiClient(MovieTimeApp appContext) {
        this.appContext = appContext;
    }

    public static void init(MovieTimeApp appContext) {
        if (movieTimeApiClient != null) {
            throw new SingletonAlreadyInitializedException(TAG);
        }
        movieTimeApiClient = new MovieTimeApiClient(appContext);
    }

    public static MovieTimeApiClient getInstance() {
        if (movieTimeApiClient == null) {
            throw new SingletonNotInitializedException(TAG);
        }
        return movieTimeApiClient;
    }

    public MovieTimeApiInterface getApiInterface() {
        if (apiInterface == null) {
            createApiClient();
        }
        return apiInterface;
    }

    private void createApiClient() {
        File httpCacheDirectory = new File(appContext.getCacheDir(), CACHE_FILE_NAME);
        Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);

        Interceptor cacheControlInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                CacheControl.Builder cacheBuilder = new CacheControl.Builder();
                cacheBuilder.maxAge(0, TimeUnit.SECONDS);
                cacheBuilder.maxStale(365, TimeUnit.DAYS);
                CacheControl cacheControl = cacheBuilder.build();

                Request request = chain.request();
                if (GeneralUtils.isNetworkAvailable(appContext)) {
                    request = request.newBuilder()
                            .cacheControl(cacheControl)
                            .build();
                }
                Response originalResponse = chain.proceed(request);
                if (GeneralUtils.isNetworkAvailable(appContext)) {
                    int maxAge = 60 * 60; // read from cache
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, max-age=" + maxAge)
                            .build();
                }
                else {
                    int maxStale = 60 * 60 * 24 * 14; // tolerate 2 weeks stale
                    return originalResponse.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                            .build();
                }
            }
        };

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.connectTimeout(60, TimeUnit.SECONDS);

        builder.addNetworkInterceptor(new StethoInterceptor())
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(cache);
        OkHttpClient okHttpClient = builder.build();

        apiInterface = new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MovieTimeApiInterface.class);
    }
}


package com.assignment.movietime.network;

import android.util.Log;

import com.assignment.movietime.MovieTimeApp;
import com.assignment.movietime.R;
import com.assignment.movietime.error.SingletonAlreadyInitializedException;
import com.assignment.movietime.error.SingletonNotInitializedException;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.model.MovieList;
import com.assignment.movietime.utils.SnackbarUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieTimeApiHandler {

    private static final String TAG = MovieTimeApiHandler.class.getSimpleName();

    private static MovieTimeApiHandler apiHandler;

    private MovieTimeApiInterface apiInterface;
    private MovieTimeApp appContext;

    private MovieTimeApiHandler(MovieTimeApp appContext) {
        this.appContext = appContext;
        this.apiInterface = MovieTimeApiClient.getInstance().getApiInterface();
    }

    public static void init(MovieTimeApp appContext) {
        if (apiHandler != null) {
            throw new SingletonAlreadyInitializedException(TAG);
        }
        MovieTimeApiClient.init(appContext);
        apiHandler = new MovieTimeApiHandler(appContext);
    }

    public static MovieTimeApiHandler getInstance() {
        if (apiHandler == null) {
            throw new SingletonNotInitializedException(TAG);
        }
        return apiHandler;
    }

    public void getMoviesPlayingNow(String apiKey, final NetworkCallback<MovieList> movieListNetworkCallback) {
        apiInterface.getMoviesPlayingNow(apiKey).enqueue(new Callback<MovieList>() {
            @Override
            public void onResponse(Call<MovieList> call, Response<MovieList> response) {
                if (response.isSuccessful()) {
                    movieListNetworkCallback.onSuccess(response.body());
                }
                else {
                    movieListNetworkCallback.onFailure(null, response.code());
                    SnackbarUtils.displaySnackbar(appContext.getString(R.string.snackbar_please_try_again));
                }
            }

            @Override
            public void onFailure(Call<MovieList> call, Throwable t) {
                movieListNetworkCallback.onFailure(t, -1);
                SnackbarUtils.displaySnackbar(appContext.getString(R.string.snackbar_please_try_again));
            }
        });
    }

    public void getMovieDetails(String apiKey, long movieId, final NetworkCallback<Movie> movieNetworkCallback) {
        apiInterface.getMovieDetails(movieId, apiKey).enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(Call<Movie> call, Response<Movie> response) {
                if (response.isSuccessful()) {
                    movieNetworkCallback.onSuccess(response.body());
                }
                else {
                    movieNetworkCallback.onFailure(null, response.code());
                    SnackbarUtils.displaySnackbar(appContext.getString(R.string.snackbar_please_try_again));
                }
            }

            @Override
            public void onFailure(Call<Movie> call, Throwable t) {
                movieNetworkCallback.onFailure(t, -1);
                SnackbarUtils.displaySnackbar(appContext.getString(R.string.snackbar_please_try_again));
            }
        });
    }
}

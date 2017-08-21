
package com.assignment.movietime.viewmodel;

import android.databinding.BaseObservable;
import android.support.annotation.Nullable;

import com.assignment.movietime.BuildConfig;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.model.MovieList;
import com.assignment.movietime.network.MovieTimeApiHandler;
import com.assignment.movietime.network.NetworkCallback;
import com.assignment.movietime.view.listeners.NotifyUIListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MoviesListViewModel extends BaseObservable {

    private MovieTimeApiHandler apiHandler;

    public MoviesListViewModel() {
        apiHandler = MovieTimeApiHandler.getInstance();
    }

    public void getMoviesList(NetworkCallback<MovieList> networkCallback) {
        apiHandler.getMoviesPlayingNow(BuildConfig.MOVIE_DB_API_KEY, networkCallback);
    }
}

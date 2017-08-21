
package com.assignment.movietime.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.view.View;
import android.widget.ImageView;

import com.assignment.movietime.GlideApp;
import com.assignment.movietime.constants.AppConstants;
import com.assignment.movietime.constants.ServerConstants;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.utils.GeneralUtils;
import com.assignment.movietime.view.activities.MovieDetailsActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.Locale;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieListItemViewModel extends BaseObservable {
    private Movie movie;

    public MovieListItemViewModel(Movie movie) {
        this.movie = movie;
    }

    public String getTitle() {
        return movie.getTitle();
    }

    public String getVoteAverage() {
        return GeneralUtils.formatToOneDecimal(movie.getVoteAverage());
    }

    public String getReleaseDate() {
        return GeneralUtils.formatDate(movie.getReleaseDate());
    }

    public String getImageUrl() {
        return ServerConstants.GET_IMAGE + ServerConstants.IMAGE_SIZE_342
               + (movie.getBackdropPath() == null ? movie.getPosterPath() : movie.getBackdropPath());
    }

    @BindingAdapter({ "image" })
    public static void loadImage(ImageView imgView, String url) {
        GlideApp.with(imgView.getContext())
                .load(url)
                .centerCrop()
                .into(imgView);
    }

    public void onMovieClicked(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, MovieDetailsActivity.class);
        intent.putExtra(AppConstants.INTENT_EXTRA_MOVIE_ID, movie.getId());
        context.startActivity(intent);
    }
}

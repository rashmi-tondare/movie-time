
package com.assignment.movietime.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.assignment.movietime.BR;
import com.assignment.movietime.BuildConfig;
import com.assignment.movietime.GlideApp;
import com.assignment.movietime.constants.ServerConstants;
import com.assignment.movietime.model.Genre;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.network.MovieTimeApiHandler;
import com.assignment.movietime.network.NetworkCallback;
import com.assignment.movietime.utils.GeneralUtils;
import com.assignment.movietime.view.activities.BaseActivity;
import com.assignment.movietime.view.activities.ChatActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieDetailsViewModel extends BaseObservable {

    private static final String TAG = MovieDetailsViewModel.class.getSimpleName();
    private Movie movie;
    private MovieTimeApiHandler apiHandler;
    public static ObservableInt toolbarColor;

    public MovieDetailsViewModel(int toolbarColor) {
        this.movie = new Movie();
        this.toolbarColor = new ObservableInt(toolbarColor);
        apiHandler = MovieTimeApiHandler.getInstance();
    }

    public void getMovieDetails(long movieId, final NetworkCallback<Movie> movieNetworkCallback) {
        apiHandler.getMovieDetails(BuildConfig.MOVIE_DB_API_KEY, movieId, new NetworkCallback<Movie>() {
            @Override
            public void onSuccess(Movie response) {
                movie = response;
                notifyPropertyChanged(BR.imageUrl);
                notifyPropertyChanged(BR.voteAverage);
                notifyPropertyChanged(BR.releaseDate);
                notifyPropertyChanged(BR.duration);
                notifyPropertyChanged(BR.genres);
                notifyPropertyChanged(BR.overview);
                movieNetworkCallback.onSuccess(response);
            }

            @Override
            public void onFailure(Throwable throwable, int failureCode) {
                movieNetworkCallback.onFailure(throwable, failureCode);
            }
        });
    }

    @Bindable
    public String getImageUrl() {
        return ServerConstants.GET_IMAGE + ServerConstants.IMAGE_SIZE_342
               + (movie.getBackdropPath() == null ? movie.getPosterPath() : movie.getBackdropPath());
    }

    @BindingAdapter({ "posterImage" })
    public static void loadImage(ImageView imgView, String url) {
        if (!TextUtils.isEmpty(url)) {
            GlideApp.with(imgView.getContext())
                    .load(url)
                    .centerCrop()
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    int lightColor = palette.getSwatches().get(1).getRgb();

                                    float[] hsv = new float[3];
                                    Color.colorToHSV(lightColor, hsv);
                                    hsv[2] *= 0.8f; // value component
                                    int darkColor = Color.HSVToColor(hsv);

                                    toolbarColor.set(lightColor);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                                        BaseActivity.currentActivity != null) {
                                        BaseActivity.currentActivity.getWindow().setStatusBarColor(darkColor);
                                    }
                                    toolbarColor.notifyChange();
                                }
                            });
                            return false;
                        }
                    })
                    .into(imgView);
        }
    }

    @Bindable
    public String getVoteAverage() {
        return GeneralUtils.formatToOneDecimal(movie.getVoteAverage());
    }

    @Bindable
    public String getReleaseDate() {
        return GeneralUtils.formatDate(movie.getReleaseDate());
    }

    @Bindable
    public String getDuration() {
        return GeneralUtils.formatTime(movie.getDurationInMinutes());
    }

    @Bindable
    public String getGenres() {
        StringBuilder sb = new StringBuilder();
        if (movie.getGenres() != null) {
            for (Genre genre : movie.getGenres()) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(genre);
            }
        }
        return sb.length() > 0 ? sb.toString() : "- - -";
    }

    @Bindable
    public String getOverview() {
        return movie.getOverview();
    }

    public void onChatClicked(View view) {
        Context context = view.getContext();
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }
}

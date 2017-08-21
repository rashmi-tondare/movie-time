
package com.assignment.movietime.view.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.assignment.movietime.R;
import com.assignment.movietime.constants.AppConstants;
import com.assignment.movietime.databinding.ActivityMovieDetailsBinding;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.network.NetworkCallback;
import com.assignment.movietime.view.listeners.NotifyUIListener;
import com.assignment.movietime.viewmodel.MovieDetailsViewModel;

public class MovieDetailsActivity extends BaseActivity implements NetworkCallback<Movie> {

    private MovieDetailsViewModel movieDetailsViewModel;
    private CollapsingToolbarLayout toolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMovieDetailsBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_details);
        movieDetailsViewModel = new MovieDetailsViewModel(ContextCompat.getColor(this, R.color.colorPrimary));
        binding.setMovieDetailsViewModel(movieDetailsViewModel);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        toolbarLayout.setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        showLoader();
        long movieId = getIntent().getLongExtra(AppConstants.INTENT_EXTRA_MOVIE_ID, 0L);
        movieDetailsViewModel.getMovieDetails(movieId, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSuccess(Movie response) {
        toolbarLayout.setTitle(response.getTitle());
        dismissLoader();
    }

    @Override
    public void onFailure(Throwable throwable, int failureCode) {
        dismissLoader();
    }
}


package com.assignment.movietime.view.activities;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.assignment.movietime.R;
import com.assignment.movietime.databinding.ActivityMoviesListBinding;
import com.assignment.movietime.model.MovieList;
import com.assignment.movietime.network.NetworkCallback;
import com.assignment.movietime.view.adapters.MoviesAdapter;
import com.assignment.movietime.viewmodel.MoviesListViewModel;

import java.util.Arrays;

public class MoviesListActivity extends BaseActivity implements NetworkCallback<MovieList> {

    private ActivityMoviesListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movies_list);
        binding.recyclerBills.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        showLoader();
        MoviesListViewModel moviesListViewModel = new MoviesListViewModel();
        moviesListViewModel.getMoviesList(this);
    }

    @Override
    public void onSuccess(MovieList response) {
        binding.recyclerBills.setAdapter(new MoviesAdapter(Arrays.asList(response.getMovies()), this));
        dismissLoader();
    }

    @Override
    public void onFailure(Throwable throwable, int failureCode) {
        dismissLoader();
    }
}

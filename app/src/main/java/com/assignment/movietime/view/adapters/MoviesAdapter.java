
package com.assignment.movietime.view.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.assignment.movietime.R;
import com.assignment.movietime.databinding.ListItemMovieBinding;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.viewmodel.MovieListItemViewModel;

import java.util.List;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;

    public MoviesAdapter(List<Movie> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemMovieBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.list_item_movie,
                parent,
                false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.binding.setMovieViewModel(new MovieListItemViewModel(movies.get(position)));
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private ListItemMovieBinding binding;

        public MovieViewHolder(ListItemMovieBinding binding) {
            super(binding.cardMovie);
            this.binding = binding;
        }
    }
}

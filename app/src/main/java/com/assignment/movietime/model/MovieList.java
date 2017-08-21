
package com.assignment.movietime.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rashmi on 18/08/17.
 */

public class MovieList {

    @SerializedName("results")
    private Movie[] movies;

    public Movie[] getMovies() {
        return movies;
    }

    public void setMovies(Movie[] movies) {
        this.movies = movies;
    }
}

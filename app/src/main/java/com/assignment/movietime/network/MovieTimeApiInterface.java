package com.assignment.movietime.network;

import com.assignment.movietime.constants.ServerConstants;
import com.assignment.movietime.model.Movie;
import com.assignment.movietime.model.MovieList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Rashmi on 18/08/17.
 */

public interface MovieTimeApiInterface {

    @GET(ServerConstants.GET_MOVIES_PLAYING_NOW)
    Call<MovieList> getMoviesPlayingNow(@Query(ServerConstants.QUERY_PARAM_API_KEY) String apiKey);

    @GET(ServerConstants.GET_MOVIE_DETAILS)
    Call<Movie> getMovieDetails(@Path("movie_id") long movieId, @Query(ServerConstants.QUERY_PARAM_API_KEY) String apiKey);
}

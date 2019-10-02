package com.jumadi.moviecatalogue.data.api;

import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Results;
import com.jumadi.moviecatalogue.data.entitiy.Videos;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiService {

    String API_KEY_YOUTUBE = "AIzaSyDMRm0djt-6eLuQobJP-pDhNfpN_Wscexo";
    
    String SERVER = "http://api.themoviedb.org/3/";
    String API_KEY = "73b8d3445c97a9beb20a8dd11ce9f0be";
    String LANG_DEFAULT = "en-US";
    String LANG_DEFAULT_IMG = "en";

    String IMG_URL = "https://image.tmdb.org/t/p/w185";
    String IMG_URL_NORMAL = "https://image.tmdb.org/t/p/w342";
    String IMG_URL_BIG = "https://image.tmdb.org/t/p/w780";

    String UP_COMING = "upcoming";
    String POPULAR = "popular";

    String DAY = "day";

    @GET("discover/movie")
    Call<Results<MovieEntity>> getMovies(
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language,
            @QueryMap Map<String, String> options
    );

    @GET("movie/{movieId}")
    Call<MovieEntity> getMovie(
            @Path("movieId") int movieId,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("search/movie")
    Call<Results<MovieEntity>> searchMovie(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("movie/{movie}")
    Call<Results<MovieEntity>> getMovies(
            @Path("movie") String movie,
            @Query("api_key") String apiKey,
            @Query("page") int page,
            @Query("language") String language
    );

    @GET("trending/movie/{time_window}")
    Call<Results<MovieEntity>> getTrending(
            @Path("time_window") String timeWindow,
            @Query("api_key") String apiKey,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<Genres> getGenre(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/credits")
    Call<Credits> getCreadits(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{id}/videos")
    Call<Videos> getVideos(
            @Path("id") int id,
            @Query("api_key") String apiKey
    );

    @GET("movie/{id}/images")
    Call<Images> getImages(
            @Path("id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );
}

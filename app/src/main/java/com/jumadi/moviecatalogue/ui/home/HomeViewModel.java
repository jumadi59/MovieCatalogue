package com.jumadi.moviecatalogue.ui.home;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import com.jumadi.moviecatalogue.data.api.MovieRepository;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.Discover;
import com.jumadi.moviecatalogue.vo.Resource;

import java.util.List;

import javax.inject.Inject;

import static com.jumadi.moviecatalogue.data.api.ApiService.API_KEY;

public class HomeViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private final MutableLiveData<String> apiKey = new MutableLiveData<>();
    private final LiveData<Discover<MovieEntity>> result = Transformations.map(apiKey, new Function<String, Discover<MovieEntity>>() {
        @Override
        public Discover<MovieEntity> apply(String input) {
            return movieRepository.HomeSource(input);
        }
    });

    private LiveData<Resource<List<MovieEntity>>>  movies;

    private LiveData<Resource<List<MovieEntity>>> upcoming;
    private LiveData<Resource<List<Genres.GenreEntity>>> category;

    @Inject
    HomeViewModel(MovieRepository mDataRepository) {
        this.movieRepository = mDataRepository;

        if (movies == null)
            initializePageList();
    }

    private void initializePageList() {

        movies = Transformations.switchMap(result, Discover::getDiscover);
        upcoming = Transformations.switchMap(result, Discover::getUpcoming);
        category = Transformations.switchMap(result, Discover::getGenres);
    }

    LiveData<Resource<List<MovieEntity>>>  getMovies() {
        return movies;
    }

    LiveData<Resource<List<MovieEntity>>> getUpcoming() {
        return upcoming;
    }

    LiveData<Resource<List<Genres.GenreEntity>>> getGenres() {
        return category;
    }

    HomeViewModel setApiKey() {
        this.apiKey.setValue(API_KEY);
        return this;
    }
}
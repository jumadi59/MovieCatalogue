package com.jumadi.moviecatalogue.ui.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.paging.PagedList;

import com.jumadi.moviecatalogue.data.api.MovieRepository;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.vo.Resource;

import javax.inject.Inject;

public class FavoriteViewModel extends ViewModel {

    private final MovieRepository movieRepository;
    private final MutableLiveData<String> search = new MutableLiveData<>();
    private LiveData<Resource<PagedList<MovieEntity>>> movies;

    @Inject
    FavoriteViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
        initialize();
    }

    private void initialize() {
        movies = Transformations.switchMap(search, input -> {
            if (input == null || input.equals("")) {
                return movieRepository.getFavorite();
            } else {
                return  movieRepository.getFavorite(input);
            }
        });
    }

    LiveData<Resource<PagedList<MovieEntity>>> getMovie() {
        return movies;
    }

    public void insertMovie(MovieEntity movieEntity) {
        movieRepository.insertFavorite(movieEntity);
    }

    public void deleteMovie(MovieEntity movieEntity) {
        movieRepository.removeFavorite(movieEntity);
    }

    public void setSearch(String query) {
        search.postValue(query);
    }
    public void refresh() {
        search.postValue("");
    }
}

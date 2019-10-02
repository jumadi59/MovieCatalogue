package com.jumadi.moviecatalogue.ui.detail;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.api.MovieRepository;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.data.entitiy.MapKey;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Videos;
import com.jumadi.moviecatalogue.in.Detail;
import com.jumadi.moviecatalogue.vo.Resource;

import javax.inject.Inject;

public class DetailViewModel extends ViewModel {

    private final MovieRepository movieRepository;

    private final MutableLiveData<MapKey> mapKey = new MutableLiveData<>();
    private final LiveData<Detail<MovieEntity>> result = Transformations.map(mapKey, new Function<MapKey, Detail<MovieEntity>>() {
        @Override
        public Detail<MovieEntity> apply(MapKey input) {
            return movieRepository.detailSource(input.getApiKey(), input.getId());
        }
    });
    private LiveData<Resource<MovieEntity>> movie;
    private LiveData<MovieEntity> movieFromDb;

    private LiveData<Resource<Credits>> credits;

    private LiveData<Resource<Videos>> videos;

    private LiveData<Resource<Images>> images;

    @Inject
    DetailViewModel(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;

            initializePageList();
    }

    private void initializePageList() {
        if (movie == null)
            movie = Transformations.switchMap(result, Detail::getDetail);
        if (movieFromDb == null)
            movieFromDb = Transformations.switchMap(result, Detail::getFromDb);
        if (videos == null)
            videos = Transformations.switchMap(result, Detail::getVideos);
        if (images == null)
            images = Transformations.switchMap(result, Detail::getImages);
        if (credits == null)
            credits = Transformations.switchMap(result, Detail::getCredits);
    }

    public void setItemId(int itemId) {
        this.mapKey.setValue(new MapKey(ApiService.API_KEY, itemId));
    }

    LiveData<Resource<MovieEntity>> getMovie() {
        return movie;
    }

    public LiveData<Resource<Credits>> getCredits() {
        return credits;
    }

    LiveData<Resource<Videos>> getVideos() {
        return videos;
    }

    LiveData<Resource<Images>> getImages() {
        return images;
    }

    public void refresh() {
        if (mapKey.getValue() != null) {
            MapKey m = mapKey.getValue();
            mapKey.setValue(m);
        }
    }

    LiveData<MovieEntity> getMovieFromDb() {
        return movieFromDb;
    }
}

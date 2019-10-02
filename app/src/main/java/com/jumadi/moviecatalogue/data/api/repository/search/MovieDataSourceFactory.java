package com.jumadi.moviecatalogue.data.api.repository.search;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;

public class MovieDataSourceFactory extends DataSource.Factory<Integer, MovieEntity> {

    private final ApiService apiService;
    private final AppExecutors appExecutors;
    private final MutableLiveData<MovieDataSource> liveData = new MutableLiveData<>();
    private final String apiKey;

    public MovieDataSourceFactory(String apiKey, ApiService apiService, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.appExecutors = appExecutors;
    }

    @NonNull
    @Override
    public DataSource<Integer, MovieEntity> create() {
        MovieDataSource movieDataSource = new MovieDataSource(apiKey, apiService, appExecutors);
        liveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<MovieDataSource> getItemLiveDataSource() {
        return liveData;
    }

}

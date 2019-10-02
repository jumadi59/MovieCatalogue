package com.jumadi.moviecatalogue.data.api.repository.discover;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;

public class DiscoverDataSourceFactory extends DataSource.Factory<Integer, MovieEntity> {

    private final ApiService apiService;
    private final AppExecutors appExecutors;
    private final AppDatabase db;
    private final MutableLiveData<DiscoverDataSource> liveData = new MutableLiveData<>();
    private final String apiKey;

    public DiscoverDataSourceFactory(String apiKey, ApiService apiService, AppDatabase db, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.db = db;
        this.appExecutors = appExecutors;
    }

    @NonNull
    @Override
    public DataSource<Integer, MovieEntity> create() {
        DiscoverDataSource movieDataSource = new DiscoverDataSource(apiKey, apiService, db, appExecutors);
        liveData.postValue(movieDataSource);
        return movieDataSource;
    }

    public MutableLiveData<DiscoverDataSource> getItemLiveDataSource() {
        return liveData;
    }

}

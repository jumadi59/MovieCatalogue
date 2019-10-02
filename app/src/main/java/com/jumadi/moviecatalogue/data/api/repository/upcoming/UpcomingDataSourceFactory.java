package com.jumadi.moviecatalogue.data.api.repository.upcoming;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;

public class UpcomingDataSourceFactory extends DataSource.Factory<Integer, MovieEntity> {

    private final ApiService apiService;
    private final AppDatabase db;
    private final AppExecutors appExecutors;
    private final MutableLiveData<UpcomingDataSource> liveData = new MutableLiveData<>();
    private final String apiKey;

    public UpcomingDataSourceFactory(String apiKey, ApiService apiService, AppDatabase db, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.db = db;
        this.appExecutors = appExecutors;
    }

    @NonNull
    @Override
    public DataSource<Integer, MovieEntity> create() {
        UpcomingDataSource upcomingDataSource = new UpcomingDataSource(apiKey, apiService, db, appExecutors);
        liveData.postValue(upcomingDataSource);
        return upcomingDataSource;
    }

    public MutableLiveData<UpcomingDataSource> getItemLiveDataSource() {
        return liveData;
    }

}

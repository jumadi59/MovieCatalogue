package com.jumadi.moviecatalogue.data.api.repository.discover;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.Options;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.api.repository.NetworkState;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Results;
import com.jumadi.moviecatalogue.in.CallBackDataSource;
import com.jumadi.moviecatalogue.utils.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverDataSource extends PageKeyedDataSource<Integer, MovieEntity> {

    private static final String TAG = DiscoverDataSource.class.getSimpleName();

    private final String apiKey;
    private final ApiService apiService;
    private final AppExecutors appExecutors;
    private final AppDatabase db;

    private final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private final MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    private final Map<String, String> options = new HashMap<>();
    private final int page = 1;
    private CallBackDataSource callBackDataSource;

    DiscoverDataSource(String apiKey, ApiService apiService, AppDatabase db, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.db = db;
        this.appExecutors = appExecutors;

        options.put(Options.INCLUDE_ADULT, "false");
        options.put(Options.SORT_BY, Options.POPULARITY_DESC);
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MovieEntity> callback) {

        initialLoad.postValue(NetworkState.LOADING());

        Callback<Results<MovieEntity>> resultsCallback = new Callback<Results<MovieEntity>>() {
            @Override
            public void onResponse(@NonNull Call<Results<MovieEntity>> call, @NonNull Response<Results<MovieEntity>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    initialLoad.postValue(NetworkState.LOADED());
                    if (response.body() != null) {
                        callback.onResult(response.body().getResults(), null, page + 1);
                        Log.d(TAG, "loadInitial: "+response.body().getResults().size());
                    } else {
                        initialLoad.postValue(NetworkState.error(response.code(), response.message()));
                        callBackDataSource = () -> loadInitial(params, callback);
                    }
                } else {
                    initialLoad.postValue(NetworkState.error(response.code(), response.message()));
                    callBackDataSource = () -> loadInitial(params, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Results<MovieEntity>> call, @NonNull Throwable t) {
                initialLoad.postValue(NetworkState.error(0, t.getMessage()));
                callBackDataSource = () -> loadInitial(params, callback);

            }
        };

        List<MovieEntity> movieEntities = Utils.parseDiscoverToMovies(db.discoverDao().getAll());
        if (movieEntities.size() > 0)
            callback.onResult(movieEntities, null, page + 1);
        else
            apiService.getMovies(apiKey, page, ApiService.LANG_DEFAULT, options).enqueue(resultsCallback);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) {

        networkState.postValue(NetworkState.LOADING());
        Callback<Results<MovieEntity>> resultsCallback = new Callback<Results<MovieEntity>>() {
            @Override
            public void onResponse(@NonNull Call<Results<MovieEntity>> call, @NonNull Response<Results<MovieEntity>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        networkState.postValue(NetworkState.LOADED());
                        Integer key = params.key <= response.body().getTotalPages() ? params.key + 1 : null;
                        callback.onResult(response.body().getResults(), key);
                    } else {
                        networkState.postValue(NetworkState.error(response.code(), response.message()));
                        callBackDataSource = () -> loadAfter(params, callback);
                    }
                } else {
                    networkState.postValue(NetworkState.error(response.code(), response.message()));
                    callBackDataSource = () -> loadAfter(params, callback);
                }
            }

            @Override
            public void onFailure(@NonNull Call<Results<MovieEntity>> call, @NonNull Throwable t) {
                networkState.postValue(NetworkState.error(0, t.getMessage()));
                callBackDataSource = () -> loadAfter(params, callback);
            }
        };

        apiService.getMovies(apiKey, params.key, ApiService.LANG_DEFAULT, options).enqueue(resultsCallback);

    }

    public MutableLiveData<NetworkState> getNetworkState() {
        return networkState;
    }

    public void retryAllFailed() {
        CallBackDataSource source = callBackDataSource;
        callBackDataSource = null;
         appExecutors.getMainThread().execute(source::run);
    }

    public MutableLiveData<NetworkState> getInitialLoad() {
        return initialLoad;
    }

}

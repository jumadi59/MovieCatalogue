package com.jumadi.moviecatalogue.data.api.repository.trending;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.PageKeyedDataSource;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.api.repository.NetworkState;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Results;
import com.jumadi.moviecatalogue.in.CallBackDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TrendingDataSource extends PageKeyedDataSource<Integer, MovieEntity> {

    private static final String TAG = TrendingDataSource.class.getSimpleName();

    private final String apiKey;
    private final ApiService apiService;
    private final AppExecutors appExecutors;

    private final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private final MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    private final int page = 1;
    private CallBackDataSource callBackDataSource;

    TrendingDataSource(String apiKey, ApiService apiService, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.appExecutors = appExecutors;
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

        apiService.getTrending(ApiService.DAY, apiKey, page).enqueue(resultsCallback);
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

        apiService.getTrending(ApiService.DAY, apiKey, params.key).enqueue(resultsCallback);

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

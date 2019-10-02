package com.jumadi.moviecatalogue.data.api.repository.search;

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

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDataSource extends PageKeyedDataSource<Integer, MovieEntity> {

    private static final String TAG = MovieDataSource.class.getSimpleName();
    private static String query;
    private static Map<String, String> options;

    private final String apiKey;
    private final ApiService apiService;
    private final AppExecutors appExecutors;

    private final MutableLiveData<NetworkState> networkState = new MutableLiveData<>();
    private final MutableLiveData<NetworkState> initialLoad = new MutableLiveData<>();

    private final int page = 1;
    private CallBackDataSource callBackDataSource;

    MovieDataSource(String apiKey, ApiService apiService, AppExecutors appExecutors) {
        this.apiKey = apiKey;
        this.apiService = apiService;
        this.appExecutors = appExecutors;
    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MovieEntity> callback) {

        Callback<Results<MovieEntity>> resultsCallback = new Callback<Results<MovieEntity>>() {
            @Override
            public void onResponse(@NonNull Call<Results<MovieEntity>> call, @NonNull Response<Results<MovieEntity>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        if (response.body().getResults().size() > 0)
                            initialLoad.postValue(NetworkState.LOADED());
                        else
                            initialLoad.postValue(NetworkState.empty(null));

                        callback.onResult(response.body().getResults(), null, page + 1);
                        Log.d(TAG, "loadInitial: "+response.body().getResults().size());
                    } else {
                        initialLoad.postValue(NetworkState.empty(response.message()));
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

        if (query != null && !query.isEmpty() && options == null){
            initialLoad.postValue(NetworkState.LOADING());
            apiService.searchMovie(apiKey, query, page, ApiService.LANG_DEFAULT).enqueue(resultsCallback);
        } else if (query == null && options != null) {
            initialLoad.postValue(NetworkState.LOADING());
            apiService.getMovies(apiKey, page, ApiService.LANG_DEFAULT, options).enqueue(resultsCallback);
            Log.d(TAG, "options: "+options);
        }
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) { }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MovieEntity> callback) {

        Callback<Results<MovieEntity>> resultsCallback = new Callback<Results<MovieEntity>>() {
            @Override
            public void onResponse(@NonNull Call<Results<MovieEntity>> call, @NonNull Response<Results<MovieEntity>> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        networkState.postValue(NetworkState.LOADED());
                        Integer key = params.key <= response.body().getTotalPages() ? params.key + 1 : null;
                        Log.d(TAG, "loadAfter: "+response.body().getResults().size());
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

        if (query != null && !query.isEmpty() && options == null) {
            networkState.postValue(NetworkState.LOADING());
            apiService.searchMovie(apiKey, query, params.key, ApiService.LANG_DEFAULT).enqueue(resultsCallback);
        } else if (query == null && options != null) {
            networkState.postValue(NetworkState.LOADING());
            apiService.getMovies(apiKey, params.key, ApiService.LANG_DEFAULT, options).enqueue(resultsCallback);
        }

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

    public void search(String query, Map<String, String> options) {
        MovieDataSource.query = query;
        MovieDataSource.options = options;
        Log.d(TAG, "search: "+query);
        invalidate();
    }

    public void searchClose() {
        Log.d(TAG, "search close");
        MovieDataSource.query = null;
        options = null;
        invalidate();
    }

}

package com.jumadi.moviecatalogue.data.api.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jumadi.moviecatalogue.data.Options;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Results;
import com.jumadi.moviecatalogue.data.entitiy.Videos;
import com.jumadi.moviecatalogue.vo.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRequest {

    private static final String TAG = MovieRequest.class.getSimpleName();

    private final ApiService apiService;
    private static MovieRequest INSTANCE;

    public static MovieRequest getInstance(ApiService apiService) {
        if (INSTANCE == null) {
            INSTANCE = new MovieRequest(apiService);
        }
        return INSTANCE;
    }

    private MovieRequest(ApiService apiService) {
        this.apiService = apiService;
    }

    public LiveData<Resource<List<MovieEntity>>> getUpcoming(int page) {
        MutableLiveData<Resource<List<MovieEntity>>> results = new MutableLiveData<>();

        results.postValue(Resource.loading(null));
        apiService.getMovies(ApiService.UP_COMING, ApiService.API_KEY, page, ApiService.LANG_DEFAULT)
                .enqueue(new Callback<Results<MovieEntity>>() {
                    @Override
                    public void onResponse(@NonNull Call<Results<MovieEntity>> call,@NonNull Response<Results<MovieEntity>> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            if (response.body() != null) {
                                results.postValue(Resource.success(response.body().getResults()));
                            } else {
                                results.postValue(Resource.empty(response.message()));
                                Log.d(TAG, "getUpcoming empty "+ response.message());
                            }
                        } else {
                            results.postValue(Resource.error(response.message()));
                            Log.d(TAG, "getUpcoming error "+ response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Results<MovieEntity>> call,@NonNull Throwable t) {
                        results.postValue(Resource.error(t.getMessage()));
                        Log.d(TAG, "getUpcoming error "+ t.getMessage());
                    }
                });

        return results;
    }

    public LiveData<Resource<List<MovieEntity>>> getDiscover(int page) {
        MutableLiveData<Resource<List<MovieEntity>>> results = new MutableLiveData<>();
        Map<String, String> options = new HashMap<>();
        options.put(Options.SORT_BY , Options.POPULARITY_DESC);
        options.put(Options.INCLUDE_ADULT, "false");

        results.postValue(Resource.loading(null));
        apiService.getMovies(ApiService.API_KEY, page, ApiService.LANG_DEFAULT, options)
                .enqueue(new Callback<Results<MovieEntity>>() {
                    @Override
                    public void onResponse(@NonNull Call<Results<MovieEntity>> call, @NonNull Response<Results<MovieEntity>> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            if (response.body() != null) {
                                results.postValue(Resource.success(response.body().getResults()));
                            } else {
                                results.postValue(Resource.empty(response.message()));
                                Log.d(TAG, "getDiscover empty "+ response.message());
                            }
                        } else {
                            results.postValue(Resource.error(response.message()));
                            Log.d(TAG, "getDiscover error "+ response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Results<MovieEntity>> call,@NonNull Throwable t) {
                        results.postValue(Resource.error(t.getMessage()));
                        Log.d(TAG, "getDiscover error "+ t.getMessage());
                    }
                });
        return results;
    }

    public LiveData<Resource<List<Genres.GenreEntity>>> getGenres() {
        MutableLiveData<Resource<List<Genres.GenreEntity>>> results = new MutableLiveData<>();

        results.postValue(Resource.loading(null));
        apiService.getGenre(ApiService.API_KEY, ApiService.LANG_DEFAULT)
                .enqueue(new Callback<Genres>() {
                    @Override
                    public void onResponse(@NonNull Call<Genres> call,@NonNull Response<Genres> response) {
                        if (response.isSuccessful() && response.code() == 200) {
                            if (response.body() != null) {
                                results.postValue(Resource.success(response.body().getGenres()));
                            } else {
                                results.postValue(Resource.empty(response.message()));
                                Log.d(TAG, "getGenres error "+ response.message());
                            }
                        } else {
                            results.postValue(Resource.error(response.message()));
                            Log.d(TAG, "getGenres error "+ response.message());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Genres> call,@NonNull Throwable t) {
                        results.postValue(Resource.error(t.getMessage()));
                        Log.d(TAG, "getGenres error "+ t.getMessage());
                    }
                });

        return results;
    }

    public LiveData<Resource<MovieEntity>> getMovie(int id, String apiKey) {
        MutableLiveData<Resource<MovieEntity>> result = new MutableLiveData<>();

        apiService.getMovie(id, apiKey, ApiService.LANG_DEFAULT).enqueue(new Callback<MovieEntity>() {
            @Override
            public void onResponse(@NonNull Call<MovieEntity> call, @NonNull Response<MovieEntity> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        result.postValue(Resource.success(response.body()));
                        Log.d(TAG, "getMovie SUCCESS");
                    } else {
                        result.postValue(Resource.empty(response.message()));
                    }
                } else {
                    result.postValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieEntity> call, @NonNull Throwable t) {
                result.postValue(Resource.error(t.getMessage()));

            }
        });
        return result;
    }

    public LiveData<Resource<Videos>> getTrailer(int id, String apiKey) {
        MutableLiveData<Resource<Videos>> result = new MutableLiveData<>();

        result.postValue(Resource.loading(null));
        apiService.getVideos(id, apiKey).enqueue(new Callback<Videos>() {
            @Override
            public void onResponse(@NonNull Call<Videos> call, @NonNull Response<Videos> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        result.postValue(Resource.success(response.body()));
                    } else {
                        result.postValue(Resource.error(response.message()));
                    }
                } else {
                    result.postValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Videos> call, @NonNull Throwable t) {
                result.postValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }

    public LiveData<Resource<Images>> getPoster(int id, String apiKey) {
        MutableLiveData<Resource<Images>> result = new MutableLiveData<>();

        result.postValue(Resource.loading(null));
        apiService.getImages(id, apiKey, ApiService.LANG_DEFAULT_IMG).enqueue(new Callback<Images>() {
            @Override
            public void onResponse(@NonNull Call<Images> call, @NonNull Response<Images> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        result.postValue(Resource.success(response.body()));
                    } else {
                        result.postValue(Resource.error(response.message()));
                    }
                } else {
                    result.postValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Images> call, @NonNull Throwable t) {
                result.postValue(Resource.error(t.getMessage()));
                Log.d(TAG, "getPoster error "+ t.getMessage());
            }
        });
        return result;
    }

    public LiveData<Resource<Credits>> getCredits(int id, String apiKey) {
        MutableLiveData<Resource<Credits>> result = new MutableLiveData<>();

        result.postValue(Resource.loading(null));
        apiService.getCreadits(id, apiKey, ApiService.LANG_DEFAULT).enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(@NonNull Call<Credits> call, @NonNull Response<Credits> response) {
                if (response.isSuccessful() && response.code() == 200) {
                    if (response.body() != null) {
                        result.setValue(Resource.success(response.body()));
                    } else {
                        result.setValue(Resource.error(response.message()));
                    }
                } else {
                    result.postValue(Resource.error(response.message()));
                }
            }

            @Override
            public void onFailure(@NonNull Call<Credits> call, @NonNull Throwable t) {
                result.postValue(Resource.error(t.getMessage()));
            }
        });

        return result;
    }
}

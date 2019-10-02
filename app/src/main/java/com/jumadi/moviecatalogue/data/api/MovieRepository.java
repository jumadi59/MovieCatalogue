package com.jumadi.moviecatalogue.data.api;

import android.util.Log;

import androidx.annotation.MainThread;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.Options;
import com.jumadi.moviecatalogue.data.api.repository.MovieRequest;
import com.jumadi.moviecatalogue.data.api.repository.NetworkState;
import com.jumadi.moviecatalogue.data.api.repository.discover.DiscoverDataSource;
import com.jumadi.moviecatalogue.data.api.repository.discover.DiscoverDataSourceFactory;
import com.jumadi.moviecatalogue.data.api.repository.search.MovieDataSource;
import com.jumadi.moviecatalogue.data.api.repository.search.MovieDataSourceFactory;
import com.jumadi.moviecatalogue.data.api.repository.trending.TrendingDataSource;
import com.jumadi.moviecatalogue.data.api.repository.trending.TrendingDataSourceFactory;
import com.jumadi.moviecatalogue.data.api.repository.upcoming.UpcomingDataSource;
import com.jumadi.moviecatalogue.data.api.repository.upcoming.UpcomingDataSourceFactory;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.DiscoverEntity;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.UpComingEntity;
import com.jumadi.moviecatalogue.data.entitiy.Videos;
import com.jumadi.moviecatalogue.in.Detail;
import com.jumadi.moviecatalogue.in.Discover;
import com.jumadi.moviecatalogue.in.Listing;
import com.jumadi.moviecatalogue.utils.Utils;
import com.jumadi.moviecatalogue.vo.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MovieRepository extends ApiRepository<MovieEntity> {

    private static final String TAG = MovieRepository.class.getSimpleName();

    private final ApiService apiService;
    private final AppExecutors appExecutors;
    private final AppDatabase db;
    private final MovieRequest movieRequest;

    @Inject
    public MovieRepository(ApiService apiService, AppExecutors appExecutors, AppDatabase db, MovieRequest movieRequest) {
        this.apiService = apiService;
        this.appExecutors = appExecutors;
        this.db = db;
        this.movieRequest = movieRequest;
    }

    @MainThread
    @Override
    public Discover<MovieEntity> HomeSource(String apiKey) {
        return new Discover<MovieEntity>() {


            @Override
            public LiveData<Resource<List<MovieEntity>>> getDiscover() {
                MediatorLiveData<Resource<List<MovieEntity>>> result = new MediatorLiveData<>();
                LiveData<List<DiscoverEntity>> dbDiscover = db.discoverDao().getLiveAll();
                LiveData<Resource<List<MovieEntity>>> requestDiscover = movieRequest.getDiscover(1);

                result.setValue(Resource.loading(null));
                result.addSource(dbDiscover, discoverEntities -> {
                    result.removeSource(dbDiscover);
                    if (discoverEntities != null && discoverEntities.size() > 0) {
                        result.setValue(Resource.success(Utils.parseDiscoverToMovies(discoverEntities)));
                    } else {
                        result.addSource(requestDiscover, resource -> {
                            result.removeSource(requestDiscover);
                            result.setValue(resource);
                        });
                    }
                });

                return result;
            }

            @Override
            public LiveData<Resource<List<MovieEntity>>> getUpcoming() {
                MediatorLiveData<Resource<List<MovieEntity>>> result = new MediatorLiveData<>();
                LiveData<List<UpComingEntity>> dbUpComing = db.upComingDao().getLiveAll();
                LiveData<Resource<List<MovieEntity>>> requestUpComing = movieRequest.getUpcoming(1);

                result.setValue(Resource.loading(null));
                result.addSource(dbUpComing, upComingEntities -> {
                    result.removeSource(dbUpComing);
                    if (upComingEntities != null && upComingEntities.size() > 0) {
                        result.setValue(Resource.success(Utils.parseUpcomingToMovies(upComingEntities)));
                    } else {
                        result.addSource(requestUpComing, resource -> {
                            result.removeSource(requestUpComing);
                            result.setValue(resource);
                        });
                    }
                });

                return result;
            }

            @Override
            public LiveData<Resource<List<Genres.GenreEntity>>> getGenres() {
                MediatorLiveData<Resource<List<Genres.GenreEntity>>> result = new MediatorLiveData<>();
                LiveData<List<Genres.GenreEntity>> dbGenres = db.genresDao().getAll();
                result.setValue(Resource.loading(null));
                result.addSource(dbGenres, genreEntities -> {
                    result.removeSource(dbGenres);
                    if (genreEntities != null && genreEntities.size() > 0) {
                        result.setValue(Resource.success(genreEntities));
                    }
                });

                return result;
            }
        };
    }

    @Override
    public Listing<MovieEntity> discoverSource(String apiKey) {
        DiscoverDataSourceFactory dataSourceFactory = new DiscoverDataSourceFactory(apiKey, apiService, db, appExecutors);
        MutableLiveData<DiscoverDataSource> dataSource = dataSourceFactory.getItemLiveDataSource();
        return new Listing<MovieEntity>() {
            @Override
            public LiveData<PagedList<MovieEntity>> getPageList() {
                return new LivePagedListBuilder<>(dataSourceFactory, Utils.configPageList).build();
            }

            @Override
            public LiveData<NetworkState> getNetworkState() {
                return Transformations.switchMap(dataSource, DiscoverDataSource::getNetworkState);
            }

            @Override
            public LiveData<NetworkState> getInitialLoad() {
                return Transformations.switchMap(dataSource, DiscoverDataSource::getInitialLoad);
            }

            @Override
            public void retry() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().retryAllFailed();
            }

            @Override
            public void refresh() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().invalidate();
            }

            @Override
            public void search(String query) {}

            @Override
            public void searchClose() {}
        };
    }

    @MainThread
    @Override
    public Listing<MovieEntity> trendingSource(String apiKey) {
        TrendingDataSourceFactory dataSourceFactory = new TrendingDataSourceFactory(apiKey, apiService, appExecutors);
        MutableLiveData<TrendingDataSource> dataSource = dataSourceFactory.getItemLiveDataSource();
        return new Listing<MovieEntity>() {
            @Override
            public LiveData<PagedList<MovieEntity>> getPageList() {
                return new LivePagedListBuilder<>(dataSourceFactory, Utils.configPageList).build();
            }

            @Override
            public LiveData<NetworkState> getNetworkState() {
                return Transformations.switchMap(dataSource, TrendingDataSource::getNetworkState);
            }

            @Override
            public LiveData<NetworkState> getInitialLoad() {
                return Transformations.switchMap(dataSource, TrendingDataSource::getInitialLoad);
            }

            @Override
            public void retry() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().retryAllFailed();
            }

            @Override
            public void refresh() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().invalidate();
            }

            @Override
            public void search(String query) {}

            @Override
            public void searchClose() {}
        };
    }

    @MainThread
    @Override
    public Listing<MovieEntity> searchSource(String apiKey) {
        MovieDataSourceFactory dataSourceFactory = new MovieDataSourceFactory(apiKey, apiService, appExecutors);
        MutableLiveData<MovieDataSource> dataSource = dataSourceFactory.getItemLiveDataSource();

        return new Listing<MovieEntity>() {
            @Override
            public LiveData<PagedList<MovieEntity>> getPageList() {
                return new LivePagedListBuilder<>(dataSourceFactory, Utils.configPageList).build();
            }

            @Override
            public LiveData<NetworkState> getNetworkState() {
                return Transformations.switchMap(dataSource, MovieDataSource::getNetworkState);
            }

            @Override
            public LiveData<NetworkState> getInitialLoad() {
                return Transformations.switchMap(dataSource, MovieDataSource::getInitialLoad);
            }

            @Override
            public void retry() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().retryAllFailed();
            }

            @Override
            public void refresh() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().invalidate();
            }

            @Override
            public void search(String query) {
                if (dataSource.getValue() != null) {
                    appExecutors.getDiskIO().execute(() -> {
                        Genres.GenreEntity genreEntity = db.genresDao().getFromName(query);
                        if (genreEntity != null) {
                            Map<String, String> options = new HashMap<>();
                            options.put(Options.WITH_GENRES , String.valueOf(genreEntity.getId()));
                            options.put(Options.INCLUDE_ADULT, "false");
                            options.put(Options.SORT_BY, Options.POPULARITY_DESC);
                            dataSource.getValue().search(null, options);
                        } else
                            dataSource.getValue().search(query, null);
                    });
                }
            }

            @Override
            public void searchClose() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().searchClose();
            }
        };
    }

    @Override
    public Listing<MovieEntity> upComingSource(String apiKey) {
        UpcomingDataSourceFactory dataSourceFactory = new UpcomingDataSourceFactory(apiKey, apiService, db, appExecutors);
        MutableLiveData<UpcomingDataSource> dataSource = dataSourceFactory.getItemLiveDataSource();

        return new Listing<MovieEntity>() {
            @Override
            public LiveData<PagedList<MovieEntity>> getPageList() {
                return new LivePagedListBuilder<>(dataSourceFactory, Utils.configPageList).build();
            }

            @Override
            public LiveData<NetworkState> getNetworkState() {
                return Transformations.switchMap(dataSource, UpcomingDataSource::getNetworkState);
            }

            @Override
            public LiveData<NetworkState> getInitialLoad() {
                return Transformations.switchMap(dataSource, UpcomingDataSource::getInitialLoad);
            }

            @Override
            public void retry() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().retryAllFailed();
            }

            @Override
            public void refresh() {
                if (dataSource.getValue() != null)
                    dataSource.getValue().invalidate();
            }

            @Override
            public void search(String query) {}

            @Override
            public void searchClose() {}
        };
    }

    @MainThread
    @Override
    public Detail<MovieEntity> detailSource(String apiKey, int id) {
        return new Detail<MovieEntity>() {
            @Override
            public LiveData<Resource<MovieEntity>> getDetail() {
                MediatorLiveData<Resource<MovieEntity>> result = new MediatorLiveData<>();
                LiveData<MovieEntity> movie = db.movieFavoritesDao().get(id);

                result.setValue(Resource.loading(null));
                result.addSource(movie, movieEntity -> {
                    Log.d(TAG, "getDetail movie "+ movieEntity);
                    result.removeSource(movie);
                    if (movieEntity != null)
                        result.setValue(Resource.success(movieEntity));
                    else {
                        LiveData<Resource<MovieEntity>> detailRequest = movieRequest.getMovie(id, apiKey);
                        result.addSource(detailRequest, resource -> {
                            Log.d(TAG, "getDetail "+ resource.status);
                            result.removeSource(detailRequest);
                            appExecutors.getMainThread().execute(() -> result.setValue(resource));
                        });
                    }
                });
                return result;
            }

            @Override
            public LiveData<MovieEntity> getFromDb() {
                MediatorLiveData<MovieEntity> result = new MediatorLiveData<>();
                LiveData<DiscoverEntity> movieDiscover = db.discoverDao().get(id);
                LiveData<UpComingEntity> movieUpComing = db.upComingDao().get(id);
                LiveData<MovieEntity> movie = db.movieFavoritesDao().get(id);

                result.addSource(movie, movieEntity -> {
                    result.removeSource(movie);
                    if (movieEntity == null) {
                        result.addSource(movieDiscover, discoverEntity -> {
                            result.removeSource(movieDiscover);
                            if (discoverEntity != null)
                                result.setValue(Utils.parseDiscoverToMovie(discoverEntity));
                            else {
                                result.addSource(movieUpComing, upComingEntity -> {
                                    result.removeSource(movieUpComing);
                                    if (upComingEntity != null)
                                        result.setValue(Utils.parseUpcomingToMovie(upComingEntity));
                                });
                            }
                        });
                    }
                });

                return result;
            }

            @Override
            public LiveData<Resource<Images>> getImages() {
                return movieRequest.getPoster(id, apiKey);
            }

            @Override
            public LiveData<Resource<Videos>> getVideos() {
                return movieRequest.getTrailer(id, apiKey);
            }

            @Override
            public LiveData<Resource<Credits>> getCredits() {
                return movieRequest.getCredits(id, apiKey);
            }
        };
    }

    @MainThread
    public LiveData<Resource<PagedList<MovieEntity>>> getFavorite() {
        MediatorLiveData<Resource<PagedList<MovieEntity>>> result = new MediatorLiveData<>();
        LiveData<PagedList<MovieEntity>> movies = new LivePagedListBuilder<>(db.movieFavoritesDao().getAll(), 20).build();

        result.setValue(Resource.loading(null));
        result.addSource(movies, movieEntities -> {
            result.removeSource(movies);
            if (movieEntities.size() == 0) {
                result.setValue(Resource.empty(null));
            } else {
                appExecutors.getMainThread().execute(() -> result.setValue(Resource.success(movieEntities)));
            }
        });
        return result;
    }

    @MainThread
    public LiveData<Resource<PagedList<MovieEntity>>> getFavorite(String search) {
        MediatorLiveData<Resource<PagedList<MovieEntity>>> result = new MediatorLiveData<>();
        LiveData<PagedList<MovieEntity>> movies = new LivePagedListBuilder<>(db.movieFavoritesDao().getSearch(search), 20).build();

        result.setValue(Resource.loading(null));
        result.addSource(movies, movieEntities -> {
            result.removeSource(movies);
            if (movieEntities.size() == 0) {
                result.setValue(Resource.empty(null));
            } else {
                appExecutors.getMainThread().execute(() -> result.setValue(Resource.success(movieEntities)));
            }
        });
        return result;
    }

    @Override
    public void insertFavorite(MovieEntity data) {
        data.setFavorite(true);
        appExecutors.getDiskIO().execute(() -> db.movieFavoritesDao().insert(data));
    }

    @Override
    public void removeFavorite(MovieEntity data) {
        appExecutors.getDiskIO().execute(() -> db.movieFavoritesDao().delete(data));
    }
}

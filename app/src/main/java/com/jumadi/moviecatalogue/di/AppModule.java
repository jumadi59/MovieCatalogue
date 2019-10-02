package com.jumadi.moviecatalogue.di;

import android.app.Application;

import com.jumadi.moviecatalogue.AppExecutors;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.api.repository.MovieRequest;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.db.FavoritesDao;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    @Singleton
    @Provides
    ApiService provideMovieService() {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.SERVER);

        return builder.build().create(ApiService.class);
    }

    @Singleton
    @Provides
    AppDatabase provideDb(Application app) {
        return AppDatabase.getInstance(app);
    }

    @Singleton
    @Provides
    MovieRequest provideMovieRequest(ApiService apiService) {
        return MovieRequest.getInstance(apiService);
    }

    @Singleton
    @Provides
    FavoritesDao provideFavoriteMovieDb(AppDatabase appDatabase) {
        return appDatabase.movieFavoritesDao();
    }

    @Singleton
    @Provides
    AppExecutors provideAppExecutors() {
        return new AppExecutors();
    }
}

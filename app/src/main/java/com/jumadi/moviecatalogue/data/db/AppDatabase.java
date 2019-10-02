package com.jumadi.moviecatalogue.data.db;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.jumadi.moviecatalogue.data.entitiy.DiscoverEntity;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.UpComingEntity;

@Database(entities = {
        MovieEntity.class,
        Genres.GenreEntity.class,
        UpComingEntity.class,
        DiscoverEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract FavoritesDao movieFavoritesDao();
    public abstract GenreDao genresDao();
    public abstract UpComingDao upComingDao();
    public abstract DiscoverDao discoverDao();

    private static final String DATABASE_NAME = "movie.db";
    private static AppDatabase INSTANCE;

    public static AppDatabase getInstance(Application app) {
        if (INSTANCE == null) {
            synchronized(AppDatabase.class) {
                INSTANCE = Room.databaseBuilder(app, AppDatabase.class, DATABASE_NAME)
                        .build();
            }
        }
        return INSTANCE;
    }
}

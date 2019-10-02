package com.jumadi.moviecatalogue.data.db;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;

@Dao
public interface FavoritesDao {

    @Query("SELECT * from favorite")
    DataSource.Factory<Integer, MovieEntity> getAll();

    @Query("SELECT * from favorite where title LIKE :title")
    DataSource.Factory<Integer, MovieEntity> getSearch(String title);

    @Query("SELECT * from favorite where id =:id")
    LiveData<MovieEntity> get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(MovieEntity movieEntity);

    @Delete()
    void delete(MovieEntity movieEntity);
}

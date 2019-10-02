package com.jumadi.moviecatalogue.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jumadi.moviecatalogue.data.entitiy.Genres;

import java.util.List;

@Dao
public interface GenreDao {


    @Query("SELECT * from genre")
    LiveData<List<Genres.GenreEntity>> getAll();

    @Query("SELECT * from genre where id =:id")
    LiveData<Genres.GenreEntity> get(int id);

    @Query("SELECT * from genre where name =:name")
    Genres.GenreEntity getFromName(String name);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Genres.GenreEntity> genreEntities);

    @Query("DELETE FROM genre")
    void deleteAll();
}

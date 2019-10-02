package com.jumadi.moviecatalogue.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jumadi.moviecatalogue.data.entitiy.DiscoverEntity;

import java.util.List;

@Dao
public interface DiscoverDao {

    @Query("SELECT * from discover")
    LiveData<List<DiscoverEntity>> getLiveAll();

    @Query("SELECT * from discover")
    List<DiscoverEntity> getAll();

    @Query("SELECT * from discover where id =:id")
    LiveData<DiscoverEntity> get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DiscoverEntity> discoverEntities);

    @Query("DELETE FROM discover")
    void deleteAll();
}

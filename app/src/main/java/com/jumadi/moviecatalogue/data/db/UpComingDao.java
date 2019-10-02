package com.jumadi.moviecatalogue.data.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.jumadi.moviecatalogue.data.entitiy.UpComingEntity;

import java.util.List;

@Dao
public interface UpComingDao {

    @Query("SELECT * from upcoming")
    LiveData<List<UpComingEntity>> getLiveAll();

    @Query("SELECT * from upcoming")
    List<UpComingEntity> getAll();


    @Query("SELECT * from upcoming where id =:id")
    LiveData<UpComingEntity> get(int id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UpComingEntity> upComingEntities);

    @Query("DELETE FROM upcoming")
    void deleteAll();
}

package com.jumadi.moviecatalogue.in;

import androidx.lifecycle.LiveData;

import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.Videos;
import com.jumadi.moviecatalogue.vo.Resource;

public interface Detail<T> {

    LiveData<Resource<T>> getDetail();
    LiveData<MovieEntity> getFromDb();
    LiveData<Resource<Images>> getImages();
    LiveData<Resource<Videos>> getVideos();
    LiveData<Resource<Credits>> getCredits();
}

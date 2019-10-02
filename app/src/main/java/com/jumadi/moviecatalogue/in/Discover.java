package com.jumadi.moviecatalogue.in;

import androidx.lifecycle.LiveData;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.vo.Resource;

import java.util.List;

public interface Discover<T> {

    LiveData<Resource<List<T>>> getDiscover();
    LiveData<Resource<List<T>>> getUpcoming();
    LiveData<Resource<List<Genres.GenreEntity>>> getGenres();

}

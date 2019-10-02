package com.jumadi.moviecatalogue.data.api;

import com.jumadi.moviecatalogue.in.Detail;
import com.jumadi.moviecatalogue.in.Discover;
import com.jumadi.moviecatalogue.in.Listing;

abstract class ApiRepository<T> {


    public abstract Discover<T> HomeSource(String apiKey);
    public abstract Listing<T> discoverSource(String apiKey);
    public abstract Listing<T> trendingSource(String apiKey);
    public abstract Listing<T> searchSource(String apiKey);
    public abstract Listing<T> upComingSource(String apiKey);
    public abstract Detail<T> detailSource(String apiKey, int id);
    public abstract void insertFavorite(T data);
    public abstract void removeFavorite(T data);

}

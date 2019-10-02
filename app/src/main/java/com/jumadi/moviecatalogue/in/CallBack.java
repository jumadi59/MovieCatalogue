package com.jumadi.moviecatalogue.in;

public interface CallBack<T> {

    void onSuccess(T response);
    void onFailure(int statusCode, Throwable error);
}

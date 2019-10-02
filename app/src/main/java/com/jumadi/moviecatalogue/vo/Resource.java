package com.jumadi.moviecatalogue.vo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.jumadi.moviecatalogue.vo.Status.EMPTY;
import static com.jumadi.moviecatalogue.vo.Status.ERROR;
import static com.jumadi.moviecatalogue.vo.Status.LOADING;
import static com.jumadi.moviecatalogue.vo.Status.SUCCESS;

public class Resource<T> {

    @NonNull
    public final Status status;

    @Nullable
    public final String message;

    @Nullable
    public final T data;

    private Resource(@NonNull Status status, @Nullable T data, @Nullable String message) {
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> Resource<T> success(@Nullable T data) {
        return new Resource<>(SUCCESS, data, null);
    }

    public static <T> Resource<T> error(String msg) {
        return new Resource<>(ERROR, null, msg);
    }

    public static <T> Resource<T> empty(String msg) {
        return new Resource<>(EMPTY, null, msg);
    }

    public static <T> Resource<T> loading(@Nullable T data) {
        return new Resource<>(LOADING, data, null);
    }
}

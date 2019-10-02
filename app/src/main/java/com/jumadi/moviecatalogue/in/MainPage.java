package com.jumadi.moviecatalogue.in;

public interface MainPage<T> {
    void showViewData(T list);
    void showViewLoading();
    void showViewEmpty(String msg);
    void showViewError(String msg);
}

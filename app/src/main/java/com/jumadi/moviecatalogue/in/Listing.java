package com.jumadi.moviecatalogue.in;

import androidx.lifecycle.LiveData;
import androidx.paging.PagedList;

import com.jumadi.moviecatalogue.data.api.repository.NetworkState;

public interface Listing<T> {

    LiveData<PagedList<T>> getPageList();
    LiveData<NetworkState> getNetworkState();
    LiveData<NetworkState> getInitialLoad();

    void retry();
    void refresh();
    void search(String query);
    void searchClose();
}

package com.jumadi.moviecatalogue.data.api.repository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jumadi.moviecatalogue.data.api.StatusResponse;

public class NetworkState {

    @NonNull
    public final StatusResponse status;

    @Nullable
    public final String message;

    private NetworkState(@NonNull StatusResponse status, @Nullable String message) {
        this.status = status;
        this.message = message;
    }

    public static NetworkState LOADED() {
        return new NetworkState(StatusResponse.SUCCESS, null);
    }

    public static NetworkState LOADING() {
        return new NetworkState(StatusResponse.LOADING, null);
    }

    public static NetworkState empty(String msg) {
        return new NetworkState(StatusResponse.EMPTY, msg);
    }

    public static NetworkState error(int statusCode, String msg) {
        if (statusCode == 0) {
            msg = "Not connected to internet";
        }
        return new NetworkState(StatusResponse.ERROR, msg == null ? "An unexpected error occurred" : msg);
    }
}

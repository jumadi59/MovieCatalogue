package com.jumadi.moviecatalogue.data.entitiy;

public class MapKey {

    private String apiKey;
    private int id;

    public MapKey(String apiKey, int id) {
        this.apiKey = apiKey;
        this.id = id;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

package com.jumadi.moviecatalogue.data.entitiy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    private int id;
    private List<VideoEntity> results;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<VideoEntity> getResults() {
        return results;
    }

    public void setResults(List<VideoEntity> results) {
        this.results = results;
    }


    public static class VideoEntity {

        private String id;
        private String name;
        private String key;
        private String site;
        private int size;
        @SerializedName("iso_639_1")
        private String iso6391;
        @SerializedName("iso_3166_1")
        private String iso31661;
        private String type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getSite() {
            return site;
        }

        public void setSite(String site) {
            this.site = site;
        }

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public String getIso31661() {
            return iso31661;
        }

        public void setIso31661(String iso31661) {
            this.iso31661 = iso31661;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}

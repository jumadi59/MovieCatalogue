package com.jumadi.moviecatalogue.data.entitiy;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Images {

    private int id;
    @SerializedName("backdrops")
    private List<ImageEntity> backdrops;
    @SerializedName("posters")
    private List<ImageEntity> posters;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<ImageEntity> getBackdrops() {
        return backdrops;
    }

    public void setBackdrops(List<ImageEntity> backdrops) {
        this.backdrops = backdrops;
    }

    public List<ImageEntity> getPosters() {
        return posters;
    }

    public void setPosters(List<ImageEntity> posters) {
        this.posters = posters;
    }


    public static class ImageEntity {

        private int id;
        @SerializedName("file_path")
        private String filePath;
        private int height;
        private int width;
        @SerializedName("aspect_ratio")
        private double aspectRatio;
        @SerializedName("iso_639_1")
        private String iso6391;
        @SerializedName("vote_count")
        private int voteCount;
        @SerializedName("vote_average")
        private double voteAverage;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public double getAspectRatio() {
            return aspectRatio;
        }

        public void setAspectRatio(double aspectRatio) {
            this.aspectRatio = aspectRatio;
        }

        public String getIso6391() {
            return iso6391;
        }

        public void setIso6391(String iso6391) {
            this.iso6391 = iso6391;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void setVoteCount(int voteCount) {
            this.voteCount = voteCount;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public void setVoteAverage(double voteAverage) {
            this.voteAverage = voteAverage;
        }
    }
}

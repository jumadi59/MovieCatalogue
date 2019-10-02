package com.jumadi.moviecatalogue.data.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "upcoming")
public class UpComingEntity {

    @NonNull
    @PrimaryKey
    private int id;

    @SerializedName("vote_average")
    @ColumnInfo(name = "vote_average")
    private double voteAverage;

    @SerializedName("title")
    private String title;

    @SerializedName("poster_path")
    @ColumnInfo(name = "poster_path")
    private String posterPath;

    @SerializedName("overview")
    private String overview;

    @SerializedName("release_date")
    @ColumnInfo(name = "release_date")
    private String releaseDate;

    @SerializedName("original_language")
    @ColumnInfo(name = "original_language")
    private String originalLanguage;

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    public UpComingEntity() {}

    @Ignore
    public UpComingEntity(int id, double voteAverage, String title, String posterPath, String overview, String releaseDate, String originalLanguage, String backdropPath) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.backdropPath = backdropPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "title: " +  title + "\n" +
                "poster_path: " + posterPath + "\n" +
                "vote_average" + voteAverage + "\n" +
                "overview: " + overview + "\n" +
                "release_date: " + releaseDate + "\n" +
                "original_language: " + originalLanguage + "\n" +
                "backdrop: " + backdropPath;
    }
}

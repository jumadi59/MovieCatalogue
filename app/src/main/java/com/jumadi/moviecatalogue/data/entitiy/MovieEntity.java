package com.jumadi.moviecatalogue.data.entitiy;

import android.text.TextUtils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;

@Entity(tableName = "favorite")
public class MovieEntity {

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

    @SerializedName("genre")
    @ColumnInfo(name = "genres")
    private String genres;

    @SerializedName("genres")
    @Ignore
    private List<Genres.GenreEntity> genresList;

    @SerializedName("runtime")
    private int runtime;

    @ColumnInfo(name = "is_favorite")
    private boolean isFavorite;

    @SerializedName("backdrop_path")
    @ColumnInfo(name = "backdrop_path")
    private String backdropPath;

    public MovieEntity() {}

    @Ignore
    public MovieEntity(int id, double voteAverage, String title, String posterPath, String overview, String releaseDate, String originalLanguage, String genres, int runtime, String backdropPath) {
        this.id = id;
        this.voteAverage = voteAverage;
        this.title = title;
        this.posterPath = posterPath;
        this.overview = overview;
        this.releaseDate = releaseDate;
        this.originalLanguage = originalLanguage;
        this.genres = genres;
        this.runtime = runtime;
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

    public String getGenres() {
        if (genres == null && genresList !=null) {
            String[] g = new String[genresList.size()];
            int i=0;
            for (Genres.GenreEntity genre : genresList) {
                g[i] = genre.getGenre();
                i++;
            }
            genres = TextUtils.join(" | ", g);
        }
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public List<Genres.GenreEntity> getGenresList() {
        return genresList;
    }

    public void setGenresList(List<Genres.GenreEntity> genres) {
        this.genresList = genres;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
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
                "genres: " + genres + "\n" +
                "runtime: " + runtime + "\n" +
                "backdrop: " + backdropPath;
    }

}

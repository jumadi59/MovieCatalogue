package com.jumadi.moviecatalogue.data.entitiy;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class Genres {

    private List<GenreEntity> genres;

    public List<GenreEntity> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreEntity> genres) {
        this.genres = genres;
    }

    @Entity(tableName = "genre")
    public static class GenreEntity {

        @NonNull
        @PrimaryKey
        private int id;

        @ColumnInfo(name = "name")
        @SerializedName("name")
        private String genre;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGenre() {
            return genre;
        }

        public void setGenre(String genre) {
            this.genre = genre;
        }
    }

}

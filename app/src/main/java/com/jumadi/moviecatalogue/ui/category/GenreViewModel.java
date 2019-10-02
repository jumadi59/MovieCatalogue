package com.jumadi.moviecatalogue.ui.category;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.Genres;

import java.util.List;

import javax.inject.Inject;

public class GenreViewModel extends ViewModel {

    private final AppDatabase db;

    private final MutableLiveData<Integer> genreId = new MutableLiveData<>();
    private final LiveData<Genres.GenreEntity> genreEntity = Transformations.switchMap(genreId, new Function<Integer, LiveData<Genres.GenreEntity>>() {
        @Override
        public LiveData<Genres.GenreEntity> apply(Integer input) {
            return db.genresDao().get(input);
        }
    });

    @Inject
    GenreViewModel(AppDatabase db) {
        this.db = db;
    }

    LiveData<List<Genres.GenreEntity>> getGenreEntities() {
        return db.genresDao().getAll();
    }

    public LiveData<Genres.GenreEntity> getGenreEntity() {
        return genreEntity;
    }

    public void setGenreId(int id) {
        this.genreId.setValue(id);
    }
}

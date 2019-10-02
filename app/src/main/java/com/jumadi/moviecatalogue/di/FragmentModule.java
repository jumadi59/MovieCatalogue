package com.jumadi.moviecatalogue.di;

import com.jumadi.moviecatalogue.ui.category.GenreBottomSheetFragment;
import com.jumadi.moviecatalogue.ui.favorite.FavoriteFragment;
import com.jumadi.moviecatalogue.ui.home.HomeFragment;
import com.jumadi.moviecatalogue.ui.search.SearchFragment;
import com.jumadi.moviecatalogue.ui.trending.TrendingFragment;
import com.jumadi.moviecatalogue.ui.upcoming.UpComingFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class FragmentModule {

    @ContributesAndroidInjector
    public abstract HomeFragment contributeHomeFragment();

    @ContributesAndroidInjector
    public abstract TrendingFragment contributeTrendingFragment();

    @ContributesAndroidInjector
    public abstract SearchFragment contributeMovieFragment();

    @ContributesAndroidInjector
    public abstract UpComingFragment contributeUpComigFragment();

    @ContributesAndroidInjector
    public abstract FavoriteFragment contributeFavoriteFragment();

    @ContributesAndroidInjector
    public abstract GenreBottomSheetFragment contributeGenreBottomSheetFragment();

}

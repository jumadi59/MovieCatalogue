package com.jumadi.moviecatalogue.di;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.jumadi.moviecatalogue.ui.category.GenreViewModel;
import com.jumadi.moviecatalogue.ui.detail.DetailViewModel;
import com.jumadi.moviecatalogue.ui.home.DiscoverViewModel;
import com.jumadi.moviecatalogue.ui.home.HomeViewModel;
import com.jumadi.moviecatalogue.ui.favorite.FavoriteViewModel;
import com.jumadi.moviecatalogue.ui.search.SearchViewModel;
import com.jumadi.moviecatalogue.ui.trending.TrendingViewModel;
import com.jumadi.moviecatalogue.ui.upcoming.UpComingViewModel;
import com.jumadi.moviecatalogue.viewmodel.ViewModelFactory;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel.class)
    public abstract ViewModel bindMovieViewModel(SearchViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel.class)
    public abstract ViewModel bindHomeViewModel(HomeViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DiscoverViewModel.class)
    public abstract ViewModel bindDiscoverViewModel(DiscoverViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(TrendingViewModel.class)
    public abstract ViewModel bindTrendingViewModel(TrendingViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel.class)
    public abstract ViewModel bindDetailViewModel(DetailViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(UpComingViewModel.class)
    public abstract ViewModel bindUpComingViewModel(UpComingViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(FavoriteViewModel.class)
    public abstract ViewModel bindFavoriteViewModel(FavoriteViewModel viewModel);

    @Binds
    @IntoMap
    @ViewModelKey(GenreViewModel.class)
    public abstract ViewModel bindGenreViewModel(GenreViewModel viewModel);

    @Binds
    public abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory viewModelFactory);

}

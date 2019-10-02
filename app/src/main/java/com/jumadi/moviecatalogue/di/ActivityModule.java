package com.jumadi.moviecatalogue.di;

import com.jumadi.moviecatalogue.SplashActivity;
import com.jumadi.moviecatalogue.ui.detail.DetailActivity;
import com.jumadi.moviecatalogue.ui.main.MainActivity;
import com.jumadi.moviecatalogue.ui.play.PlayTrailerActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityModule {

    @ContributesAndroidInjector(modules = FragmentModule.class)
    public abstract MainActivity contributeMainActivity();

    @ContributesAndroidInjector
    public abstract DetailActivity contributeDetailActivity();

    @ContributesAndroidInjector
    public abstract SplashActivity contributeSplashActivity();

    @ContributesAndroidInjector
    public abstract PlayTrailerActivity contributePLayTrailerActivity();

}

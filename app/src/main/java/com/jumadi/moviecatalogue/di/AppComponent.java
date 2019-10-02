package com.jumadi.moviecatalogue.di;

import android.app.Application;

import com.jumadi.moviecatalogue.App;
import com.jumadi.moviecatalogue.ui.home.HomeViewHolder;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        ActivityModule.class
        })
public interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);

    void inject(HomeViewHolder homeViewHolder);
}

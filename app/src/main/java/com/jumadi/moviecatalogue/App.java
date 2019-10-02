package com.jumadi.moviecatalogue;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Preconditions;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.jumadi.moviecatalogue.di.AppComponent;
import com.jumadi.moviecatalogue.di.DaggerAppComponent;
import com.jumadi.moviecatalogue.in.Injectable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;
import dagger.android.support.AndroidSupportInjection;

public class App extends Application implements HasAndroidInjector {

    @Nullable
    private static AppComponent appComponent;

    @Inject
    public DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = DaggerAppComponent.builder().application(this).build();
        appComponent.inject(this);
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
                if (activity instanceof Injectable) {
                    AndroidInjection.inject(activity);
                }
                
                if (activity instanceof FragmentActivity) {
                    ((FragmentActivity) activity).getSupportFragmentManager()
                            .registerFragmentLifecycleCallbacks(new FragmentManager.FragmentLifecycleCallbacks() {
                        @Override
                        public void onFragmentCreated(@NonNull FragmentManager fm, @NonNull Fragment f, @Nullable Bundle savedInstanceState) {
                            super.onFragmentCreated(fm, f, savedInstanceState);
                            if (f instanceof Injectable) {
                                AndroidSupportInjection.inject(f);
                            }

                        }
                    }, true);
                }
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {

            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {

            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {

            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {

            }
        });
    }

    @NonNull
    public static AppComponent getAppComponent() {
        Preconditions.checkNotNull(appComponent);
        return appComponent;
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }
}

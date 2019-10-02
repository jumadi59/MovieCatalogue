package com.jumadi.moviecatalogue.in;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by Jumadi Janjaya on 27/09/19
 * jumadi@cumacoding.com
 */

public abstract class BaseActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int onViewLayout();

    protected abstract void onInitializeView();

    protected abstract void onNewClass(@Nullable Bundle savedInstanceState);

    protected abstract void onViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onViewLayout());
        onInitializeView();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        onNewClass(savedInstanceState);
        onViewModel();
    }

}

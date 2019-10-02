package com.jumadi.moviecatalogue.in;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by Jumadi Janjaya on 27/09/19
 * jumadi@cumacoding.com
 */

public abstract class BaseFragment extends Fragment {

    @LayoutRes
    protected abstract int onViewLayout();

    protected abstract void onInitializeView(@NonNull View view);

    protected abstract void onNewClass(@Nullable Bundle savedInstanceState);

    protected abstract void onViewModel();

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        return inflater.inflate(onViewLayout(), container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onInitializeView(view);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        onNewClass(savedInstanceState);
        onViewModel();
    }
}

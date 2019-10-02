package com.jumadi.moviecatalogue.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.BaseFragment;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.in.MainPage;
import com.jumadi.moviecatalogue.ui.detail.DetailActivity;
import com.jumadi.moviecatalogue.utils.CustomRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import javax.inject.Inject;

/**
 * Created by Jumadi Janjaya on 24/09/19
 * jumadi@cumacoding.com
 */

public class HomeFragment extends BaseFragment implements Injectable, MainPage<PagedList<MovieEntity>>, AdapterCallBack {

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;
    private DiscoverViewModel mViewModel;

    private CustomRecyclerView rvMovie;
    private HomeAdapter adapter;
    private RotateLoading rotateLoading;
    private LinearLayout contentError;

    public static Fragment newInstance() {
        return new HomeFragment();
    }

    @Override
    protected int onViewLayout() {
        return R.layout.fragment_list_main;
    }

    @Override
    protected void onInitializeView(@NonNull View view) {
        rvMovie = view.findViewById(R.id.rv_list);
        rotateLoading = view.findViewById(R.id.rotate_loading);
        contentError = view.findViewById(R.id.content_error);
    }

    @Override
    protected void onNewClass(@Nullable Bundle savedInstanceState) {
        adapter = new HomeAdapter(this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvMovie.setLayoutManager(manager);
        rvMovie.setAdapter(adapter);
        rvMovie.setVerticalScrollBarEnabled(false);

        rotateLoading.setVisibility(View.GONE);
        rvMovie.setVisibility(View.GONE);
        contentError.setVisibility(View.GONE);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getItemCount() <= 1) {
            if (mViewModel !=null)
                mViewModel.refrash();
        }
    }

    @Override
    protected void onViewModel() {
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(DiscoverViewModel.class).setApiKey();

        mViewModel.getMovies().observe(this, this::showViewData);
        mViewModel.getNetworkState().observe(this, networkState -> adapter.networkState(networkState));

    }

    @Override
    public void showViewData(PagedList<MovieEntity> list) {
        if (list !=null) {
            adapter.submitList(list);
        }

        rotateLoading.stop();
        rotateLoading.setVisibility(View.GONE);
        rvMovie.setVisibility(View.VISIBLE);
        contentError.setVisibility(View.GONE);

    }

    @Override
    public void showViewLoading() {}

    @Override
    public void showViewEmpty(String msg) {}

    @Override
    public void showViewError(String msg) {}

    @Override
    public void retry() {
        mViewModel.retry();
    }

    @Override
    public void to(Object obj) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_ID, (int) obj);
        getActivity().startActivity(intent);
    }
}

package com.jumadi.moviecatalogue.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.BaseFragment;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.in.MainPage;
import com.jumadi.moviecatalogue.in.MainSearch;
import com.jumadi.moviecatalogue.ui.detail.DetailActivity;
import com.jumadi.moviecatalogue.utils.CustomRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import javax.inject.Inject;

public class SearchFragment extends BaseFragment implements Injectable, MainPage<PagedList<MovieEntity>>, MainSearch, AdapterCallBack {

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;
    private SearchViewModel mViewModel;

    private CustomRecyclerView rvMovie;
    private MovieAdapter adapter;
    private RotateLoading rotateLoading;
    private LinearLayout contentError;
    private Button btnTryAgain;
    private TextView txtMsgError;

    private boolean isInitialize = true;
    private String query;

    public static SearchFragment newInstance() {
        return new SearchFragment();
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
        txtMsgError = view.findViewById(R.id.txt_msg_error);
        btnTryAgain = view.findViewById(R.id.btn_try_again);

    }

    @Override
    protected void onNewClass(@Nullable Bundle savedInstanceState) {
        adapter = new MovieAdapter(MovieAdapter.DIFF_CALLBACK, this);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvMovie.setHasFixedSize(true);
        rvMovie.setLayoutManager(manager);
        rvMovie.setAdapter(adapter);
    }

    @Override
    protected void onViewModel() {
        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(SearchViewModel.class).setApiKey();

        mViewModel.getMovies().observe(this, this::showViewData);
        mViewModel.getInitialLoad().observe(this, networkState -> {
            Log.d("Search", "getInitialLoad "+networkState.status);
            switch (networkState.status) {
                case LOADING:
                    showViewLoading();
                    break;
                case EMPTY:
                    showViewEmpty(networkState.message);
                    break;
                case ERROR:
                    showViewError(networkState.message);
                    break;
                case SUCCESS:
                    showViewData(null);
                    break;
            }
        });
        mViewModel.getNetworkState().observe(this, networkState -> adapter.networkState(networkState));

        btnTryAgain.setOnClickListener(view -> mViewModel.refresh());

    }

    @Override
    public void showViewData(@Nullable PagedList<MovieEntity> list) {
        if (isInitialize) {
            isInitialize = false;
            return;
        }

        if (list ==null) {
            rotateLoading.stop();
            rotateLoading.setVisibility(View.GONE);
            contentError.setVisibility(View.GONE);
            rvMovie.setVisibility(View.VISIBLE);
        }
        if (list !=null) {
            adapter.submitList(list);
            contentError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter.getItemCount() == 0) {
            if (mViewModel != null)
                mViewModel.refresh();
        }
    }

    @Override
    public void showViewLoading() {
        rvMovie.setVisibility(View.GONE);
        contentError.setVisibility(View.GONE);

        rotateLoading.start();
        rotateLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void showViewEmpty(@Nullable String msg) {

        if (msg !=null && !msg.isEmpty())
            txtMsgError.setText(msg);
        else {
            if (query !=null)
                txtMsgError.setText(getString(R.string.msg_no_query, "\""+query+"\""));
        }
        rotateLoading.setVisibility(View.GONE);
        rvMovie.setVisibility(View.GONE);
        contentError.setVisibility(View.VISIBLE);
        rotateLoading.stop();

        btnTryAgain.setVisibility(View.GONE);
    }

    @Override
    public void showViewError(@Nullable String msg) {
        contentError.setVisibility(View.VISIBLE);
        rotateLoading.stop();
        rotateLoading.setVisibility(View.GONE);
        rvMovie.setVisibility(View.GONE);

        if (msg !=null && !msg.isEmpty()) {
            txtMsgError.setText(msg);
        } else {
            txtMsgError.setText(getString(R.string.error_msg, getString(R.string.title_movies)));
        }

        btnTryAgain.setVisibility(View.VISIBLE);
    }

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

    @Override
    public void search(@NonNull String query) {
        this.query = query;
        mViewModel.searchMovie(query);
    }

    @Override
    public void searchOpen() {
        showViewEmpty(getString(R.string.msg_search, getString(R.string.title_movies)));
    }

    @Override
    public void searchClose() {
        mViewModel.searchClose();
    }

    @Override
    public void onLoad() {
            mViewModel.refresh();
    }

}
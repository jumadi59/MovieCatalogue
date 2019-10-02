package com.jumadi.moviecatalogue.ui.favorite;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.jumadi.moviecatalogue.ui.search.MovieAdapter;
import com.jumadi.moviecatalogue.utils.CustomRecyclerView;
import com.victor.loading.rotate.RotateLoading;

import javax.inject.Inject;

import static com.jumadi.moviecatalogue.ui.detail.DetailActivity.EXTRA_ID;
import static com.jumadi.moviecatalogue.ui.detail.DetailActivity.RESULT_REMOVE;

public class FavoriteFragment extends BaseFragment implements Injectable, MainPage<PagedList<MovieEntity>>, AdapterCallBack {

    private static final int REQUEST_CODE_DELETE_FAVORITE = 101;

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;
    private FavoriteViewModel mViewModel;

    private CustomRecyclerView rvMovie;
    private MovieAdapter adapter;
    private RotateLoading rotateLoading;
    private LinearLayout contentError;
    private Button btnTryAgain;
    private TextView txtMsgError;

    public static FavoriteFragment newInstance() {
        return new FavoriteFragment();
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

        mViewModel = new ViewModelProvider(this, mViewModelFactory).get(FavoriteViewModel.class);
        mViewModel.refresh();
        mViewModel.getMovie().observe(this, pagedListResource -> {
            switch (pagedListResource.status) {
                case LOADING:
                    showViewLoading();
                    break;
                case EMPTY:
                    showViewEmpty(pagedListResource.message);
                    break;
                case ERROR:
                    showViewError(pagedListResource.message);
                    break;
                case SUCCESS:
                    showViewData(pagedListResource.data);
                    break;
            }
        });

    }

    @Override
    public void showViewData(PagedList<MovieEntity> list) {

        if (list !=null) {
            adapter.submitList(list);
            rotateLoading.stop();
            rotateLoading.setVisibility(View.GONE);
            rvMovie.setVisibility(View.VISIBLE);
            contentError.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DELETE_FAVORITE && resultCode == RESULT_REMOVE) {
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
        else
            txtMsgError.setText(getString(R.string.msg_empty_data, getString(R.string.title_movies)));

        btnTryAgain.setVisibility(View.GONE);
        rotateLoading.setVisibility(View.GONE);
        rvMovie.setVisibility(View.GONE);
        contentError.setVisibility(View.VISIBLE);
        rotateLoading.stop();
    }

    @Override
    public void showViewError(String msg) {
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
    }

    @Override
    public void to(Object obj) {
        if (getActivity() == null) return;

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_ID, (int) obj);
        startActivityForResult(intent, REQUEST_CODE_DELETE_FAVORITE);
    }
}
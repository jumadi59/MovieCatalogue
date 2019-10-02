package com.jumadi.moviecatalogue.ui.home;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jumadi.moviecatalogue.App;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.CustomAdapter;
import com.jumadi.moviecatalogue.ui.category.GenreBottomSheetFragment;
import com.jumadi.moviecatalogue.ui.detail.DetailActivity;
import com.jumadi.moviecatalogue.ui.main.MainActivity;
import com.jumadi.moviecatalogue.utils.Utils;
import com.jumadi.moviecatalogue.vo.Status;

import java.util.List;

import javax.inject.Inject;

public class HomeViewHolder extends RecyclerView.ViewHolder implements AdapterCallBack, View.OnClickListener {

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;

    private RecyclerView rvUpComing;
    private LinearLayout linearCategory;

    private UpComingAdapter upComingAdapter;
    private TextView seeMore;
    private TextView seeMoreUpComing;
    private final Context context;

    HomeViewHolder(@NonNull View itemView) {
        super(itemView);
        App.getAppComponent().inject(this);
        this.context = itemView.getContext();
        onInitializeView(itemView);
    }

    private void onInitializeView(@NonNull View view) {
        rvUpComing = view.findViewById(R.id.rv_upcoming);
        linearCategory = view.findViewById(R.id.linear_category);
        seeMore = view.findViewById(R.id.see_more);
        seeMoreUpComing = view.findViewById(R.id.see_more_up_coming);
        onNewClass();
    }

    private void onNewClass() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        rvUpComing.setLayoutManager(linearLayoutManager);

        upComingAdapter = new UpComingAdapter(this);
        upComingAdapter.setConfigAdapter(new CustomAdapter.ConfigAdapter()
        .setLayoutManager(CustomAdapter.ConfigAdapter.HORIZONTAL_LAYOUT)
        .setMarginItem(20)
        .setMaxCount(10));
        rvUpComing.setAdapter(upComingAdapter);
        rvUpComing.setItemAnimator(new DefaultItemAnimator());
        rvUpComing.setNestedScrollingEnabled(false);
        rvUpComing.setHasFixedSize(false);

        seeMore.setOnClickListener(this);
        seeMoreUpComing.setOnClickListener(this);

        onViewModel();

    }

    private void onViewModel() {
        if (getActivity() == null) return;

        HomeViewModel mViewModelHome = new ViewModelProvider(getActivity(), mViewModelFactory).get(HomeViewModel.class).setApiKey();

        mViewModelHome.getUpcoming().observe(getActivity(), resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null) {
                showUpComing(Utils.Filter(resource.data));
            }
        });

        mViewModelHome.getGenres().observe(getActivity(), listResource -> {
            if (listResource.status == Status.SUCCESS && listResource.data != null) {
                showCategory(listResource.data);
            }
        });

    }

    @Override
    public void retry() {}

    @Override
    public void to(Object obj) {
        if (getActivity() == null) return;

        if (obj instanceof Genres.GenreEntity) {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).searchFromGenre((Genres.GenreEntity) obj);
        } else {
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            intent.putExtra(DetailActivity.EXTRA_ID, (int) obj);
            getActivity().startActivity(intent);
        }


    }

    private FragmentActivity getActivity() {
        return (FragmentActivity) context;
    }

    private String getString(@StringRes int idString) {
        return getActivity().getString(idString);
    }

    private void showUpComing(List<MovieEntity> list) {
        upComingAdapter.setMovieEntities(list);
        upComingAdapter.notifyDataSetChanged();
    }

    private void showCategory(List<Genres.GenreEntity> data) {
        LinearLayout action = linearCategory.findViewById(R.id.action);
        LinearLayout comedy = linearCategory.findViewById(R.id.comedy);
        LinearLayout sciFi = linearCategory.findViewById(R.id.horror);
        LinearLayout romance = linearCategory.findViewById(R.id.romance);

        action.setTag(Utils.getGenre(data, getString(R.string.genre_action)));
        comedy.setTag(Utils.getGenre(data, getString(R.string.genre_comedy)));
        sciFi.setTag(Utils.getGenre(data, getString(R.string.genre_horror)));
        romance.setTag(Utils.getGenre(data, getString(R.string.genre_romance)));

        action.setOnClickListener(this);
        comedy.setOnClickListener(this);
        sciFi.setOnClickListener(this);
        romance.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.see_more) {
            if (getActivity() !=null) {
                GenreBottomSheetFragment genreBottomSheetFragment = GenreBottomSheetFragment.getInstance(this);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .add(genreBottomSheetFragment, GenreBottomSheetFragment.TAG)
                        .show(genreBottomSheetFragment).commit();
            }

        } else if (view.getId() == R.id.see_more_up_coming) {
            if (getActivity() instanceof MainActivity)
                ((MainActivity) getActivity()).currentItem(4);
        } else {
            Genres.GenreEntity genre = (Genres.GenreEntity) view.getTag();
            if (genre != null) {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).searchFromGenre(genre);
                }
            }
        }
    }

}


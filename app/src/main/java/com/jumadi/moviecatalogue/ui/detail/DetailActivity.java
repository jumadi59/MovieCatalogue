package com.jumadi.moviecatalogue.ui.detail;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.BaseActivity;
import com.jumadi.moviecatalogue.in.CustomAdapter;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.ui.favorite.FavoriteViewModel;
import com.jumadi.moviecatalogue.ui.play.PlayTrailerActivity;
import com.jumadi.moviecatalogue.utils.Utils;
import com.victor.loading.rotate.RotateLoading;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL;
import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL_BIG;
import static com.jumadi.moviecatalogue.vo.Status.SUCCESS;

public class DetailActivity extends BaseActivity implements Injectable, AdapterCallBack {

    public static final String EXTRA_ID = "extra_id";
    public static final int RESULT_REMOVE = 1;

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;

    private DetailViewModel mViewModel;
    private FavoriteViewModel favoriteViewModel;

    private TextView titleToolbar;
    private ImageView imgBackdrop;
    private ImageView imgPoster;
    private TextView txtTitle;
    private TextView txtReleaseDate;
    private TextView txtRuntime;
    private TextView txtLanguage;
    private TextView txtDescription;
    private TextView txtGenres;
    private TextView txtRateCount;
    private TextView txtRateDari;
    private TextView txtMsgError;
    private TextView txtSeeMoreCast;
    private MaterialRatingBar ratings;
    private RotateLoading rotateLoading;
    private NestedScrollView contentLayout;
    private LinearLayout contentError;
    private Button btnTryAgain;
    private FloatingActionButton fab;
    private TextView titleFullCasts;
    private TextView titleTrailer;

    private RecyclerView rvVideo;
    private VideoAdapter videoAdapter;
    private RecyclerView rvImage;
    private ImageAdapter imageAdapter;
    private RecyclerView rvCast;
    private CastAdapter castAdapter;

    private MovieEntity dataMovie;
    private boolean isFavorite = false;
    private boolean showActionFavorite = false;
    private int itemId;

    private Credits credits;


    @Override
    protected int onViewLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onInitializeView() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
        AndroidInjection.inject(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_chevron_left_white_24dp);
        }

        titleToolbar = findViewById(R.id.title_toolbar);
        imgBackdrop = findViewById(R.id.img_backdrop);
        imgPoster = findViewById(R.id.img_poster);
        txtTitle = findViewById(R.id.txt_title);
        txtReleaseDate = findViewById(R.id.txt_release_date);
        txtRuntime = findViewById(R.id.txt_runtime);
        txtLanguage = findViewById(R.id.txt_language);
        txtDescription = findViewById(R.id.txt_description);
        txtGenres = findViewById(R.id.txt_gendres);
        txtRateCount = findViewById(R.id.txt_rate);
        txtRateDari = findViewById(R.id.rate);
        ratings = findViewById(R.id.rating_bar);
        contentLayout = findViewById(R.id.content_layout);
        rotateLoading = findViewById(R.id.rotate_loading);
        contentError = findViewById(R.id.content_error);
        txtMsgError = findViewById(R.id.txt_msg_error);
        btnTryAgain = findViewById(R.id.btn_try_again);
        fab = findViewById(R.id.fab);
        rvImage = findViewById(R.id.rv_images);
        rvVideo = findViewById(R.id.rv_videos);
        rvCast = findViewById(R.id.rv_casts);
        txtSeeMoreCast = findViewById(R.id.see_more_cast);
        titleFullCasts = findViewById(R.id.txt_full_cast);
        titleTrailer = findViewById(R.id.txt_trailer);
    }

    @Override
    protected void onNewClass(@Nullable Bundle savedInstanceState) {

        itemId = getIntent().getIntExtra(EXTRA_ID, 0);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(RecyclerView.HORIZONTAL);

        imageAdapter = new ImageAdapter();
        rvImage.setLayoutManager(manager);
        rvImage.setAdapter(imageAdapter);

        LinearLayoutManager manager1 = new LinearLayoutManager(this);
        manager1.setOrientation(RecyclerView.HORIZONTAL);
        videoAdapter = new VideoAdapter(this);
        videoAdapter.setConfigAdapter(new CustomAdapter.ConfigAdapter()
                .setLayoutManager(CustomAdapter.ConfigAdapter.HORIZONTAL_LAYOUT)
                .setMaxCount(5)
                .setMarginItem(16));
        rvVideo.setLayoutManager(manager1);
        rvVideo.setAdapter(videoAdapter);

        LinearLayoutManager manager2 = new LinearLayoutManager(this);
        manager2.setOrientation(RecyclerView.HORIZONTAL);
        castAdapter = new CastAdapter();
        castAdapter.setConfigAdapter(new CustomAdapter.ConfigAdapter()
                .setLayoutManager(CustomAdapter.ConfigAdapter.HORIZONTAL_LAYOUT)
                .setMaxCount(8)
                .setMarginItem(16));
        rvCast.setLayoutManager(manager2);
        rvCast.setAdapter(castAdapter);
    }

    @Override
    protected void onViewModel() {
        mViewModel =new ViewModelProvider(this, mViewModelFactory).get(DetailViewModel.class);
        favoriteViewModel = new ViewModelProvider(this, mViewModelFactory).get(FavoriteViewModel.class);
        mViewModel.setItemId(itemId);

        mViewModel.getMovie().observe(this, resource -> {
            switch (resource.status) {
                case LOADING:
                    showViewLoading();
                    break;
                case ERROR:
                    showViewError(resource.message);
                    break;
                case SUCCESS:
                    assert resource.data != null;
                    showViewDataMovie(resource.data);
                    dataMovie = resource.data;
                    isFavorite = dataMovie.isFavorite();
                    if (isFavorite)
                        fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    else
                        fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    invalidateOptionsMenu();
                    break;
            }
        });

        mViewModel.getCredits().observe(this, creditEntityResource -> {
            if (creditEntityResource.status == SUCCESS) {
                if (creditEntityResource.data != null) {
                    credits = creditEntityResource.data;

                    castAdapter.setCastEntities(creditEntityResource.data.getCasts());
                    castAdapter.notifyDataSetChanged();

                    if (castAdapter.getItemCount() > 0)
                        titleFullCasts.setVisibility(View.VISIBLE);

                    if (creditEntityResource.data.getCasts().size() > castAdapter.getItemCount())
                        txtSeeMoreCast.setVisibility(View.VISIBLE);
                    else
                        txtSeeMoreCast.setVisibility(View.GONE);
                }
            }
        });

        mViewModel.getVideos().observe(this, videosEntityResource -> {
            if (videosEntityResource.status == SUCCESS) {
                if (videosEntityResource.data !=null) {
                    videoAdapter.setVideoList(videosEntityResource.data.getResults());
                    videoAdapter.notifyDataSetChanged();
                    if (videoAdapter.getItemCount() > 0)
                        titleTrailer.setVisibility(View.VISIBLE);
                }
            }
        });

        mViewModel.getImages().observe(this, imagesEntityResource -> {
            Log.d("Data", "status "+imagesEntityResource.status);
            if (imagesEntityResource.status == SUCCESS) {
                if (imagesEntityResource.data != null)
                    imageAdapter.setImageEntities(imagesEntityResource.data.getPosters());
                imageAdapter.notifyDataSetChanged();
            }
        });

        mViewModel.getMovieFromDb().observe(this, movieEntity -> {
            if (movieEntity != null && rotateLoading.isStart())
                showViewDataMovie(movieEntity);
        });

        btnTryAgain.setOnClickListener(view -> mViewModel.refresh());
        txtSeeMoreCast.setOnClickListener(view -> {
            CastBottomSheetFragment castBottomSheetFragment = CastBottomSheetFragment.getInstance(credits);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(castBottomSheetFragment, CastBottomSheetFragment.TAG)
                    .show(castBottomSheetFragment).commit();
        });

        fab.setOnClickListener(view -> addOrDeleteFavorite());

        fab.addOnShowAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                showActionFavorite = false;
                invalidateOptionsMenu();
            }

            @Override
            public void onAnimationEnd(Animator animator) { }

            @Override
            public void onAnimationCancel(Animator animator) {
                showActionFavorite = true;
                invalidateOptionsMenu();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        fab.addOnHideAnimationListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                showActionFavorite = true;
                invalidateOptionsMenu();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                showActionFavorite = false;
                invalidateOptionsMenu();
            }

            @Override
            public void onAnimationRepeat(Animator animator) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.details_menu, menu);

        MenuItem favorite = menu.findItem(R.id.action_favorite);
        if (showActionFavorite) {
            favorite.setVisible(true);
            updateLayoutTitleToolbar(Utils.dpToPx(15));
        } else {
            favorite.setVisible(false);
            updateLayoutTitleToolbar(Utils.dpToPx(65));
        }

        if (isFavorite)
            favorite.setIcon(R.drawable.ic_favorite_white_24dp);
        else
            favorite.setIcon(R.drawable.ic_favorite_border_white_24dp);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite:
                addOrDeleteFavorite();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateLayoutTitleToolbar(int px) {
        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) titleToolbar.getLayoutParams();
        lp.rightMargin = px;
        titleToolbar.setLayoutParams(lp);
    }

    private void addOrDeleteFavorite() {
        if (dataMovie !=null) {
            if (isFavorite) {
                favoriteViewModel.deleteMovie(dataMovie);
                isFavorite = false;
                invalidateOptionsMenu();
                fab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                showMessage(R.string.success_remove_favorite);
                setResult(RESULT_REMOVE);
            } else {
                favoriteViewModel.insertMovie(dataMovie);
                isFavorite = true;
                invalidateOptionsMenu();
                fab.setImageResource(R.drawable.ic_favorite_white_24dp);
                showMessage(R.string.success_add_favorite);
            }
        }
    }

    private void showMessage(@StringRes int msg) {
        Snackbar.make(contentLayout, msg, Snackbar.LENGTH_LONG).setAction(R.string.undo, undoListener).show();
    }

    private final View.OnClickListener undoListener = view -> addOrDeleteFavorite();

    private void openVideo(String id) {
        Intent intent = new Intent(this, PlayTrailerActivity.class);
        intent.putExtra(PlayTrailerActivity.EXTRA_ID_TRAILER, id);
        startActivity(intent);
    }

    private void showViewLoading() {
        rotateLoading.start();
        rotateLoading.setVisibility(View.VISIBLE);

        contentLayout.setVisibility(View.GONE);
        contentError.setVisibility(View.GONE);
    }

    private void showViewError(String msg) {

        contentError.setVisibility(View.VISIBLE);
        if (msg !=null && !msg.isEmpty())
            txtMsgError.setText(msg);
        else
            txtMsgError.setText(getString(R.string.error_msg, getString(R.string.title_movies)));

        contentLayout.setVisibility(View.GONE);
        rotateLoading.setVisibility(View.GONE);
    }

    private void showViewDataMovie(@NonNull MovieEntity movie) {

        Glide.with(this)
                .load(IMG_URL_BIG+movie.getBackdropPath())
                .into(imgBackdrop);

        Glide.with(this)
                .load(IMG_URL+movie.getPosterPath())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error).apply(
                RequestOptions.bitmapTransform(new RoundedCorners(Utils.dpToPx(12))))
                .into(imgPoster);

        txtTitle.setText(movie.getTitle());
        txtReleaseDate.setText(Utils.parseDate(movie.getReleaseDate(), "MMM"));
        txtRuntime.setText(Utils.timeFormat(this, movie.getRuntime()));
        txtLanguage.setText(getResources().getString(R.string.lang , movie.getOriginalLanguage()));
        txtDescription.setText(movie.getOverview());
        txtGenres.setText(movie.getGenres());

        float rating = (float) (movie.getVoteAverage() * 5 / 10);
        ratings.setRating(rating);
        txtRateCount.setText(String.valueOf(rating));
        txtRateDari.setText(getResources().getString(R.string.rate_dari));

        rotateLoading.stop();
        rotateLoading.setVisibility(View.GONE);

        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void retry() {}

    @Override
    public void to(Object obj) {
        String id = (String) obj;
        openVideo(id);
    }
}

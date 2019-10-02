package com.jumadi.moviecatalogue;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jumadi.moviecatalogue.data.api.repository.MovieRequest;
import com.jumadi.moviecatalogue.data.db.AppDatabase;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.preference.PrefManager;
import com.jumadi.moviecatalogue.in.BaseActivity;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.ui.main.MainActivity;
import com.jumadi.moviecatalogue.utils.Utils;
import com.jumadi.moviecatalogue.vo.Status;
import com.victor.loading.rotate.RotateLoading;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements Injectable {

    private static final String TAG = SplashActivity.class.getSimpleName();

    @Inject
    public MovieRequest movieRequest;
    @Inject
    public AppDatabase db;
    @Inject
    public AppExecutors appExecutors;

    private ConstraintLayout constraintLayout;
    private ImageView imgLogo;
    private RotateLoading rotateLoading;
    private TextView txtInfo;
    private View bgAnimation;

    private final int targetCheckNetwork = 4;
    private int checkNetwork = 0;
    private int indexOld = -1;

    private final Handler handler = new Handler();
    private final List<String> stringList = new ArrayList<>();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            nextTextInfo(indexOld);
            int maxCheckNetwork = 6;
            if (checkNetwork == maxCheckNetwork) {
                requestUpComing();
                checkNetwork+=1;
            } else {
                if (isNetwork() && checkNetwork < targetCheckNetwork)
                    checkNetwork = targetCheckNetwork;
                else
                    checkNetwork+=1;
            }

            handler.postDelayed(runnable, 1000L);
        }
    };

    @Override
    protected int onViewLayout() {
       return R.layout.activity_splash;
    }

    @Override
    protected void onInitializeView() {
        constraintLayout = findViewById(R.id.container);
        imgLogo = findViewById(R.id.img_logo);
        rotateLoading = findViewById(R.id.rotate_loading);
        txtInfo = findViewById(R.id.txt_info);
        bgAnimation = findViewById(R.id.bg_animate);


        if (!PrefManager.getInitialize(getBaseContext())) {
            addMessageLoading("Initialize");
        } else {
            addMessageLoading("Loading");
        }
    }

    @Override
    protected void onNewClass(@Nullable Bundle savedInstanceState) {

        imgLogo.setImageResource(R.drawable.ic_app);
        startAnimation();
        rotateLoading.start();
        nextTextInfo(indexOld);
        handler.postDelayed(runnable, 1000L);
    }

    @Override
    public void onViewModel() {

    }

    private void requestUpComing() {
        movieRequest.getUpcoming(1).observe(this, resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null) {
                List<MovieEntity> movieEntities = resource.data;
                if (PrefManager.getInitialize(getBaseContext())) {
                    appExecutors.getDiskIO().execute(() -> db.upComingDao().deleteAll());
                }
                appExecutors.getDiskIO().execute(() -> db.upComingDao().insertAll(Utils.parseToUpComing(movieEntities)));
                requestDiscover();
            } else if (resource.status == Status.LOADING) {
                addMessageLoading("Loading");
            } else {
                requestDiscover();
            }
        });
    }

    private void requestDiscover() {
        movieRequest.getDiscover(1).observe(this, resource -> {
            if (resource.status == Status.SUCCESS && resource.data != null) {
                if (PrefManager.getInitialize(getBaseContext())) {
                    appExecutors.getDiskIO().execute(() -> db.discoverDao().deleteAll());
                }
                appExecutors.getDiskIO().execute(() -> db.discoverDao().insertAll(Utils.parseToDiscover(resource.data)));
                requestGenres();
            } else if (resource.status == Status.LOADING) {
                addMessageLoading("Loading");
            } else {
                requestGenres();
            }
        });
    }

    private void requestGenres() {
        movieRequest.getGenres().observe(this, listResource -> {
            if (listResource.status == Status.SUCCESS && listResource.data != null) {
                appExecutors.getDiskIO().execute(() -> db.genresDao().insertAll(listResource.data));
                PrefManager.setInitialize(getBaseContext(), true);
                startHome();
            } else {
                if (listResource.status == Status.LOADING) {
                    addMessageLoading("Loading");
                } else {
                    if (PrefManager.getInitialize(getBaseContext()))
                        startHome();
                    else
                        showDialog();
                }
            }
        });
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("OK", (dialogInterface, i) -> checkNetwork = targetCheckNetwork-1);
        builder.setNegativeButton("Exit", (dialogInterface, i) -> finish());
        builder.setTitle(R.string.error_msg_no_internet);
        builder.setMessage(R.string.error_msg_initialize);
        builder.show();
    }

    private void startAnimation() {
        imgLogo.setScaleX(0f);
        imgLogo.setScaleY(0f);
        imgLogo.animate().scaleX(0.6f).scaleY(0.6f).setDuration(500L).start();
        bgAnimation.animate().scaleY(13f).scaleX(13f).setDuration(1000L).start();
        bgAnimation.animate().setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {}

            @Override
            public void onAnimationEnd(Animator animator) {
                constraintLayout.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                bgAnimation.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {}

            @Override
            public void onAnimationRepeat(Animator animator) { }
        });
    }

    private void startHome() {
        handler.removeCallbacks(runnable);
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        finish();
        startActivity(intent);
    }

    private boolean isNetwork() {
        return true;
    }

    private void addMessageLoading(String msg) {
        if (stringList.size() > 0)
            stringList.clear();

        stringList.add(msg);
        stringList.add(msg+".");
        stringList.add(msg+"..");
        stringList.add(msg+"...");
        indexOld = -1;
    }

    private void nextTextInfo(int oldPos) {
        indexOld = oldPos+1;
        if (indexOld >= stringList.size()) {
            txtInfo.setText(stringList.get(0));
            indexOld = 0;
        } else {
            txtInfo.setText(stringList.get(indexOld));
        }
    }

}

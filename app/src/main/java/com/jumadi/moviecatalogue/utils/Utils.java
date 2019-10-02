package com.jumadi.moviecatalogue.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.util.Preconditions;
import androidx.paging.PagedList;

import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.DiscoverEntity;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.data.entitiy.UpComingEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Utils {

    @SuppressLint("SimpleDateFormat")
    public static String parseDate(String date, String month) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat dateFormatNew = new SimpleDateFormat("dd "+month+" yyyy", Locale.getDefault());
        if (date == null || date.equals("")) {
            return "";
        }
        try {
            Date today = dateFormat.parse(date);
            if (today != null) {
                return dateFormatNew.format(today);
            } else {
                return "";
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean isUpComing(String date, int before, int after) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date time = dateFormat.parse(date);
            assert time != null;

            Calendar cdBefore = Calendar.getInstance();
            cdBefore.set(Calendar.DAY_OF_MONTH, cdBefore.get(Calendar.DAY_OF_MONTH)-before);
            cdBefore.set(Calendar.AM_PM, Calendar.AM);
            cdBefore.set(Calendar.HOUR, 0);
            cdBefore.set(Calendar.MINUTE, 0);
            cdBefore.set(Calendar.SECOND, 0);
            cdBefore.set(Calendar.MILLISECOND, 0);

            Calendar cdAfter = Calendar.getInstance();
            cdAfter.set(Calendar.DAY_OF_MONTH, cdAfter.get(Calendar.DAY_OF_MONTH)+after);
            cdAfter.set(Calendar.AM_PM, Calendar.AM);
            cdAfter.set(Calendar.HOUR, 0);
            cdAfter.set(Calendar.MINUTE, 0);
            cdAfter.set(Calendar.SECOND, 0);
            cdAfter.set(Calendar.MILLISECOND, 0);
            if (after < 0)
                return time.getTime() >= cdBefore.getTime().getTime();
            else
                return time.getTime() >= cdBefore.getTime().getTime() && time.getTime() <= cdAfter.getTime().getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<MovieEntity> Filter(List<MovieEntity> list) {
        List<MovieEntity> movieEntities = new ArrayList<>();
        List<MovieEntity> movieEntities1 = new ArrayList<>();

        for (MovieEntity movieEntity : list) {
            if (isUpComing(movieEntity.getReleaseDate(), 20,30)) {
                movieEntities.add(movieEntity);
            } else {
                movieEntities1.add(movieEntity);
            }
        }

        movieEntities.addAll(movieEntities1);
        return movieEntities;
    }

    public static String timeFormat(@NonNull Context context, int time) {
        int h = time / 60;
        int m = time - (h*60);
        if (h > 0) {
            if (m > 0)
                return h + context.getResources().getString(R.string.time_hour) + " " +
                        m + context.getResources().getString(R.string.time_minute);
            else
                return h + context.getResources().getString(R.string.time_hour);
        } else {
            return m + context.getResources().getString(R.string.time_minute);
        }
    }

    public static final PagedList.Config configPageList = new PagedList.Config.Builder()
            .setEnablePlaceholders(true)
                .setInitialLoadSizeHint(10)
                .setPageSize(20).build();

    public static int calculateNoOfColumns(Context context, float columnWidthDp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5f);
    }

    public static int dpToPx(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dp * metrics.density + 0.5f);
    }

    @NonNull
    public static List<UpComingEntity> parseToUpComing(List<MovieEntity> movieEntities) {
        List<UpComingEntity> list = new ArrayList<>();
        for (MovieEntity movieEntity: movieEntities) {
            list.add(new UpComingEntity(
                    movieEntity.getId(),
                    movieEntity.getVoteAverage(),
                    movieEntity.getTitle(),
                    movieEntity.getPosterPath(),
                    movieEntity.getOverview(),
                    movieEntity.getReleaseDate(),
                    movieEntity.getOriginalLanguage(),
                    movieEntity.getBackdropPath()
            ));
        }

        return list;
    }

    @NonNull
    public static List<DiscoverEntity> parseToDiscover(List<MovieEntity> movieEntities) {
        List<DiscoverEntity> list = new ArrayList<>();
        for (MovieEntity movieEntity: movieEntities) {
            list.add(new DiscoverEntity(
                    movieEntity.getId(),
                    movieEntity.getVoteAverage(),
                    movieEntity.getTitle(),
                    movieEntity.getPosterPath(),
                    movieEntity.getOverview(),
                    movieEntity.getReleaseDate(),
                    movieEntity.getOriginalLanguage(),
                    movieEntity.getBackdropPath()
            ));
        }
        return list;
    }

    @NonNull
    public static MovieEntity parseUpcomingToMovie(UpComingEntity upComingEntity) {
        return new MovieEntity(
                upComingEntity.getId(),
                upComingEntity.getVoteAverage(),
                upComingEntity.getTitle(),
                upComingEntity.getPosterPath(),
                upComingEntity.getOverview(),
                upComingEntity.getReleaseDate(),
                upComingEntity.getOriginalLanguage(),
                "",
                0,
                upComingEntity.getBackdropPath()
        );
    }

    @NonNull
    public static List<MovieEntity> parseUpcomingToMovies(List<UpComingEntity> upComingEntities) {
        List<MovieEntity> list = new ArrayList<>();
        for (UpComingEntity upComingEntity: upComingEntities) {
            list.add(parseUpcomingToMovie(upComingEntity));
        }
        return list;
    }

    @NonNull
    public static MovieEntity parseDiscoverToMovie(DiscoverEntity discoverEntity) {
        return new MovieEntity(
                discoverEntity.getId(),
                discoverEntity.getVoteAverage(),
                discoverEntity.getTitle(),
                discoverEntity.getPosterPath(),
                discoverEntity.getOverview(),
                discoverEntity.getReleaseDate(),
                discoverEntity.getOriginalLanguage(),
                "",
                0,
                discoverEntity.getBackdropPath()
        );
    }

    @NonNull
    public static List<MovieEntity> parseDiscoverToMovies(List<DiscoverEntity> discoverEntities) {
        List<MovieEntity> list = new ArrayList<>();
        for (DiscoverEntity discoverEntity: discoverEntities) {
            list.add(parseDiscoverToMovie(discoverEntity));
        }
        return list;
    }

    @Nullable
    public static Genres.GenreEntity getGenre(List<Genres.GenreEntity> genreEntities, String name) {
        Genres.GenreEntity g = null;
        for (Genres.GenreEntity genreEntity : genreEntities) {
            if (genreEntity.getGenre().equals(name)) {
                g = genreEntity;
            }
        }

        return g;
    }

    @NonNull
    public static Drawable getVectorDrawable(@NonNull Context context,@DrawableRes int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);

        Preconditions.checkNotNull(drawable);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        return drawable;
    }
}

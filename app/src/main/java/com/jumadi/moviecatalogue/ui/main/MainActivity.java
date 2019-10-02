package com.jumadi.moviecatalogue.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.in.BaseActivity;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.in.MainSearch;
import com.jumadi.moviecatalogue.ui.favorite.FavoriteFragment;
import com.jumadi.moviecatalogue.ui.home.HomeFragment;
import com.jumadi.moviecatalogue.ui.search.SearchFragment;
import com.jumadi.moviecatalogue.ui.trending.TrendingFragment;
import com.jumadi.moviecatalogue.ui.upcoming.UpComingFragment;
import com.jumadi.moviecatalogue.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasAndroidInjector;

public class MainActivity extends BaseActivity implements Injectable, HasAndroidInjector {

    private static final String EXTRA_SELECT_MENU = "select_menu";
    private static final String TAG = MainActivity.class.getSimpleName();

    private BottomNavigationView navView;
    private AppCompatAutoCompleteTextView searchView;

    private MainSearch stateSearch;
    private final List<NavigationEntity> navigationEntityList = new ArrayList<>();
    private final List<Integer> recordCurrentItem = new ArrayList<>();
    private int currentItem = 0;
    private boolean isBackPressed;

    @Inject
    public DispatchingAndroidInjector<Object> dispatchingAndroidInjector;

    @Inject
    ViewModelProvider.Factory mViewModelFactory;
    private AppBarLayout appBarLayout;
    private Drawable iconClear;
    private Drawable iconSearch;
    private Drawable iconBack;

    @Override
    protected int onViewLayout() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getColor(R.color.colorPrimary));
        }
        return R.layout.activity_main;
    }

    @Override
    protected void onInitializeView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayShowHomeEnabled(false);
            actionBar.setDisplayShowCustomEnabled(true);
            actionBar.setCustomView(R.layout.toolbar_search);

            View customView = actionBar.getCustomView();
            ViewGroup.LayoutParams lp = customView.getLayoutParams();
            lp.width = ActionBar.LayoutParams.MATCH_PARENT;
            lp.height = ActionBar.LayoutParams.MATCH_PARENT;
            customView.setLayoutParams(lp);
            searchView = customView.findViewById(R.id.search);
        }

        appBarLayout = findViewById(R.id.app_bar);
        navView = findViewById(R.id.nav_view);

        navigationEntityList.add(
                new NavigationEntity(
                        R.id.navigation_home,
                        HomeFragment.newInstance(),
                        getString(R.string.title_home)));
        navigationEntityList.add(
                new NavigationEntity(
                        R.id.navigation_trending,
                        TrendingFragment.newInstance(),
                        getString(R.string.title_trending)));
        navigationEntityList.add(
                new NavigationEntity(
                        R.id.navigation_favorites,
                        FavoriteFragment.newInstance(),
                        getString(R.string.title_favorites)).setRemoved());
        navigationEntityList.add(
                new NavigationEntity(
                        534,
                        SearchFragment.newInstance(),
                        getString(R.string.title_search)));
        navigationEntityList.add(
                new NavigationEntity(
                        535,
                        UpComingFragment.newInstance(),
                        getString(R.string.title_upcoming)));
    }

    @Override
    protected void onNewClass(@Nullable Bundle savedInstanceState) {
        search();
        navView.setOnNavigationItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    currentItem(0);
                    return true;
                case R.id.navigation_trending:
                    currentItem(1);
                    return true;
                case R.id.navigation_favorites:
                    currentItem(2);
                    return true;
                    default:
                        return false;
            }
        });
        if (savedInstanceState != null)
            navView.setSelectedItemId(savedInstanceState.getInt(EXTRA_SELECT_MENU));
        else
            navView.setSelectedItemId(R.id.navigation_home);

    }

    @Override
    public void onViewModel() {}

    public void currentItem(int position) {
        FragmentManager fm = getSupportFragmentManager();

        Fragment currentFragment = fm.getPrimaryNavigationFragment();
        if (currentFragment != null) {
            NavigationEntity hideFragment = navigationEntityList.get(currentItem);
            if (hideFragment.isRemoved()) {
                fm.beginTransaction()
                        .remove(currentFragment)
                        .commitNowAllowingStateLoss();
            } else {
                fm.beginTransaction().hide(currentFragment)
                        .setPrimaryNavigationFragment(currentFragment)
                        .setReorderingAllowed(true).commitNowAllowingStateLoss();
            }
        }

        NavigationEntity showFragment = navigationEntityList.get(position);
        Fragment fragment = fm.findFragmentByTag(String.valueOf(showFragment.getId()));

        if (fragment == null) {
            fm.beginTransaction().add(R.id.container, showFragment.getFragment(), String.valueOf(showFragment.getId()))
            .show(showFragment.getFragment())
                    .setPrimaryNavigationFragment(showFragment.getFragment())
                    .setReorderingAllowed(true).commitNowAllowingStateLoss();
        } else {
            fm.beginTransaction().show(fragment)
                    .setPrimaryNavigationFragment(fragment)
                    .setReorderingAllowed(true).commitNowAllowingStateLoss();
        }

        appBarLayout.setExpanded(true, true);
        if (!findStateSearch(showFragment.getFragment())) {
            if (!isBackPressed)
                recordCurrentItem.add(currentItem);

            isBackPressed = false;
            currentItem = position;
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void search() {
        if (searchView == null) return;

        iconClear = Utils.getVectorDrawable(this, R.drawable.ic_action_clear);
        iconClear.setBounds(0, 0, Utils.dpToPx(24), Utils.dpToPx(24));
        iconSearch = Utils.getVectorDrawable(this, R.drawable.ic_action_search);
        iconSearch.setBounds(0, 0, Utils.dpToPx(24), Utils.dpToPx(24));
        iconBack = Utils.getVectorDrawable(this, R.drawable.ic_action_back);
        iconBack.setBounds(0, 0, Utils.dpToPx(24), Utils.dpToPx(24));

        searchView.setCompoundDrawablePadding(Utils.dpToPx(3));
        searchView.setCompoundDrawables(iconSearch, null, null, null);
        SearchListenerClass search = new SearchListenerClass();
        searchView.setOnKeyListener(search);
        searchView.setOnFocusChangeListener(search);
        searchView.setOnEditorActionListener(search);
        searchView.setOnTouchListener(search);
        searchView.addTextChangedListener(search);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECT_MENU, navView.getSelectedItemId());
    }

    public void searchFromGenre(Genres.GenreEntity genre){
        if (searchView != null) {
            searchOpen();

            searchView.setText(genre.getGenre());
            new Handler().postDelayed(() -> {
                Log.d(TAG, "searchFromGenre "+genre.getGenre());
                searchQuery();
            }, 500L);

        }
    }

    private boolean findStateSearch(Fragment fm) {
        if (stateSearch !=null)
            stateSearch.searchClose();

        stateSearch = null;
        if (searchView != null) {
            searchView.setText("");
            searchView.clearFocus();
            if (searchView.getCompoundDrawables()[0] == iconBack)
                searchView.setCompoundDrawables(iconSearch, null, null, null);
        }

        try {
            if (fm != null) {
                stateSearch = (MainSearch) fm;
                stateSearch.searchOpen();
                return true;
            } else
                return false;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void searchClose() {
        if (stateSearch !=null)
            stateSearch.searchClose();

        currentItem(currentItem);
        if (searchView != null) {
            searchView.setText("");
            searchView.clearFocus();
            if (searchView.getCompoundDrawables()[0] == iconBack)
                searchView.setCompoundDrawables(iconSearch, null, null, null);
        }
    }

    private void searchOpen() {
        if (stateSearch == null) {
            currentItem(3);
            stateSearch.searchOpen();
            searchView.requestFocus();

        }
    }

    private void searchQuery() {
        if (stateSearch != null && searchView != null) {
            stateSearch.search(searchView.getText().toString());
            if (searchView.hasFocus())
                searchView.clearFocus();
        }
    }

    @Override
    public void onBackPressed() {
        int recordSize = recordCurrentItem.size()-1;
        Integer current = recordSize >= 0? recordCurrentItem.get(recordSize) : 0;
        if (stateSearch != null) {
            searchClose();
        } else if (currentItem == 0)
            super.onBackPressed();
        else {
            recordCurrentItem.remove(recordSize);
            isBackPressed = true;
            if (current < 3)
                navView.setSelectedItemId(navigationEntityList.get(current).getId());
            else
                currentItem(current);

        }
    }

    @Override
    public AndroidInjector<Object> androidInjector() {
        return dispatchingAndroidInjector;
    }

    private class SearchListenerClass implements View.OnKeyListener, TextView.OnEditorActionListener,
            View.OnFocusChangeListener, View.OnTouchListener, TextWatcher {

        @Override
        public boolean onKey(View searchView, int keyCode, KeyEvent keyEvent) {

            if (keyCode == KeyEvent.KEYCODE_ENTER) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                searchQuery();
                return true;
            }
            return false;
        }

        @Override
        public boolean onEditorAction(TextView arg0, int actionId, KeyEvent arg2) {
            if (actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_DONE
                    || actionId == EditorInfo.IME_ACTION_NEXT
                    || actionId == EditorInfo.IME_ACTION_SEND
                    || actionId == EditorInfo.IME_ACTION_SEARCH
                    || (arg2.getAction() == KeyEvent.KEYCODE_ENTER) && !searchView.getText().toString().isEmpty()) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
                searchQuery();
                return true;
            }
            return false;
        }

        @Override
        public void onFocusChange(final View v, final boolean hasFocus) {
            if (hasFocus) {
                if (!searchView.getText().toString().isEmpty()){
                    searchView.setCompoundDrawables(iconBack, null, iconClear, null);
                    searchView.selectAll();
                }
                else
                    searchView.setCompoundDrawables(iconBack, null, null, null);

                if (stateSearch == null)
                    searchOpen();
            }

            if (!hasFocus) {
                Log.d(TAG, "stateSearch"+stateSearch);
                if (stateSearch != null)
                    searchView.setCompoundDrawables(iconBack, null, null, null);
                else
                    searchView.setCompoundDrawables(iconSearch, null, null, null);

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (searchView.getCompoundDrawables()[2] != null) {
                boolean tappedX = event.getX() > (searchView.getWidth()
                        - searchView.getPaddingRight() - iconClear.getIntrinsicWidth());
                if (tappedX) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (searchView.hasFocus()) {
                        searchView.setText("");
                        }
                    }
                    return true;
                }
            } else if (searchView.getCompoundDrawables()[0] != null && stateSearch != null) {
                boolean tappedX = event.getX() < (searchView.getPaddingLeft() + iconBack.getIntrinsicWidth());
                if (tappedX) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        searchClose();
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

        @Override
        public void afterTextChanged(Editable editable) {
            if (!searchView.getText().toString().isEmpty() && searchView.getCompoundDrawables()[2] == null && searchView.hasFocus())
                searchView.setCompoundDrawables(iconBack, null, iconClear, null);
            else if (searchView.getText().toString().isEmpty() && searchView.getCompoundDrawables()[2] != null && stateSearch != null)
                searchView.setCompoundDrawables(iconBack, null, null, null);
        }
    }
}
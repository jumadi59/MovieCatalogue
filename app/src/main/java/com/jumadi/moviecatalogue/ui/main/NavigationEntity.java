package com.jumadi.moviecatalogue.ui.main;

import androidx.fragment.app.Fragment;

class NavigationEntity {

    private int id;
    private String title;
    private Fragment fragment;
    private boolean isRemoved;

    public NavigationEntity(int id, Fragment fragment, String title) {
        this.id = id;
        this.fragment = fragment;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    boolean isRemoved() {
        return isRemoved;
    }

    NavigationEntity setRemoved() {
        isRemoved = true;
        return this;
    }
}

package com.jumadi.moviecatalogue.in;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumadi.moviecatalogue.utils.Utils;

import static com.jumadi.moviecatalogue.in.CustomAdapter.ConfigAdapter.GRID_LAYOUT;
import static com.jumadi.moviecatalogue.in.CustomAdapter.ConfigAdapter.HORIZONTAL_LAYOUT;
import static com.jumadi.moviecatalogue.in.CustomAdapter.ConfigAdapter.NONE;
import static com.jumadi.moviecatalogue.in.CustomAdapter.ConfigAdapter.VERTICAL_LAYOUT;

public abstract class CustomAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final int START = 1;
    private static final int END = 20;

    private ConfigAdapter configAdapter;

    protected abstract int getItemSize();

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return START;
        } else if (position == getItemCount()-1) {
            return END;
        } else {
            return 0;
        }
    }

    @Override
    public int getItemCount() {
        if (configAdapter.maxCount > 0 && getItemSize() > configAdapter.maxCount)
            return configAdapter.maxCount;
        else
            return getItemSize();
    }

    protected View setViewLayout(ViewGroup parent, View view, int viewType) {
        switch (configAdapter.layoutManager) {
            case GRID_LAYOUT:
                return setAutoFitGrid(parent, view);
            case HORIZONTAL_LAYOUT:
                return setMarginItemStartAnd(viewType, view);
            case VERTICAL_LAYOUT:
                return setMarginItemTopBottom(viewType, view);
                default:
                    return view;
        }

    }

    private View setMarginItemTopBottom(int viewType, View view) {
        if (configAdapter.marginItem == NONE) return view;

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (viewType == START) {
            lp.topMargin = Utils.dpToPx(configAdapter.marginItem);
            view.setLayoutParams(lp);
        } else if (viewType == END) {
            lp.bottomMargin = Utils.dpToPx(configAdapter.marginItem);
            view.setLayoutParams(lp);
        }

        return view;
    }

    private View setMarginItemStartAnd(int viewType, View view) {
        if (configAdapter.marginItem == NONE) return view;

        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (viewType == START) {
            lp.leftMargin = Utils.dpToPx(configAdapter.marginItem);
            view.setLayoutParams(lp);
        } else if (viewType == END) {
            lp.rightMargin = Utils.dpToPx(configAdapter.marginItem);
            view.setLayoutParams(lp);
        }

        return view;
    }

    private View setAutoFitGrid(ViewGroup viewGroup, View view) {
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) view.getLayoutParams();
        if (configAdapter.marginItem > 0 ) {
            int width = viewGroup.getMeasuredWidth() / configAdapter.gridCount;
            lp.width = width - (Utils.dpToPx(configAdapter.marginItem) * 2);
            lp.leftMargin = Utils.dpToPx(configAdapter.marginItem);
            lp.rightMargin = Utils.dpToPx(configAdapter.marginItem);
        } else {
            int width = viewGroup.getMeasuredWidth() / configAdapter.gridCount;
            lp.width = width - (lp.rightMargin + lp.rightMargin);
        }

        view.setLayoutParams(lp);

        return view;
    }

    public void setConfigAdapter(@NonNull ConfigAdapter configAdapter) {
        this.configAdapter = configAdapter;
    }

    public ConfigAdapter getConfigAdapter() {
        return configAdapter;
    }

    public static class ConfigAdapter {

        public static final int GRID_LAYOUT = 3;
        public static final int HORIZONTAL_LAYOUT = 2;
        static final int VERTICAL_LAYOUT = 1;

        static final int NONE = -1;

        private int layoutManager = NONE;
        private int maxCount = NONE;
        private int gridCount = NONE;
        private int marginItem = NONE;

        public int getLayoutManager() {
            return layoutManager;
        }

        public ConfigAdapter setLayoutManager(int layoutManager) {
            this.layoutManager = layoutManager;
            return this;
        }

        public int getMaxCount() {
            return maxCount;
        }

        public ConfigAdapter setMaxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public int getGridCount() {
            return gridCount;
        }

        public ConfigAdapter setGridCount(int gridCount) {
            this.gridCount = gridCount;
            return this;
        }

        public int getMarginItem() {
            return marginItem;
        }

        public ConfigAdapter setMarginItem(int marginItem) {
            this.marginItem = marginItem;
            return this;
        }
    }
}

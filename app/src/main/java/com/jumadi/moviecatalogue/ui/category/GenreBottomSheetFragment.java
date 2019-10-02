package com.jumadi.moviecatalogue.ui.category;

import android.app.Dialog;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.CustomAdapter;
import com.jumadi.moviecatalogue.in.Injectable;
import com.jumadi.moviecatalogue.utils.CustomRecyclerView;
import com.jumadi.moviecatalogue.utils.Utils;

import javax.inject.Inject;

public class GenreBottomSheetFragment extends BottomSheetDialogFragment implements Injectable, AdapterCallBack {

    public static final String TAG = GenreBottomSheetFragment.class.getSimpleName();

    @Inject
    public ViewModelProvider.Factory mViewModelFactory;

    private CategoryAdapter adapter;
    private CustomRecyclerView rvGenres;
    private TextView tvTitle;
    private String[] nameIcons;
    private int[] resIdIcons;

    private final AdapterCallBack adapterCallBack;

    public static GenreBottomSheetFragment getInstance(AdapterCallBack adapterCallBack) {
        return new GenreBottomSheetFragment(adapterCallBack);
    }

    private GenreBottomSheetFragment(AdapterCallBack adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new BottomSheetDialog(requireContext(), getTheme());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_bottom_sheet, container, false);
        if (getDialog().getWindow() != null)
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        view.setBackground(new ColorDrawable(Color.TRANSPARENT));

        tvTitle = view.findViewById(R.id.tv_title);
        rvGenres = view.findViewById(R.id.rv_list);

        return view;
    }

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle.setText(getString(R.string.category));

        nameIcons = getResources().getStringArray(R.array.name_icon_genres);
        TypedArray tArray = getResources().obtainTypedArray(R.array.id_icon_genres);

        int count = tArray.length();
        resIdIcons = new int[count];
        for (int i = 0; i < resIdIcons.length; i++) {
            resIdIcons[i] = tArray.getResourceId(i, 0);
        }
        tArray.recycle();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int spanCount = Utils.calculateNoOfColumns(requireContext(), 100);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), spanCount);
        rvGenres.setLayoutManager(manager);
        adapter = new CategoryAdapter(this, resIdIcons, nameIcons);
        adapter.setConfigAdapter(new CustomAdapter.ConfigAdapter()
                .setLayoutManager(CustomAdapter.ConfigAdapter.GRID_LAYOUT)
                .setGridCount(spanCount));
        rvGenres.setAdapter(adapter);

        GenreViewModel mViewModel = new ViewModelProvider(this, mViewModelFactory).get(GenreViewModel.class);

        mViewModel.getGenreEntities().observe(this, genreEntities -> {
            adapter.setGenreEntities(genreEntities);
            adapter.notifyDataSetChanged();
        });
    }

    @Override
    public void retry() {}

    @Override
    public void to(Object obj) {
        adapterCallBack.to(obj);
        dismiss();
    }
}

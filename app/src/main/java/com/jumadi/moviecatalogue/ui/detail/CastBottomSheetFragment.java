package com.jumadi.moviecatalogue.ui.detail;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.in.CustomAdapter;
import com.jumadi.moviecatalogue.utils.CustomRecyclerView;
import com.jumadi.moviecatalogue.utils.Utils;

public class CastBottomSheetFragment extends BottomSheetDialogFragment {

    public static final String TAG = CastBottomSheetFragment.class.getSimpleName();

    private final Credits list;
    private TextView tvTitle;
    private CustomRecyclerView rvCasts;

    static CastBottomSheetFragment getInstance(Credits list) {
        return new CastBottomSheetFragment(list);
    }

    private CastBottomSheetFragment(Credits list) {
        this.list = list;
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
        tvTitle = view.findViewById(R.id.tv_title);
        rvCasts = view.findViewById(R.id.rv_list);

        return view;
    }


    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle.setText(getString(R.string.full_cast));
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int spanCount = Utils.calculateNoOfColumns(requireContext(), 100);
        GridLayoutManager manager = new GridLayoutManager(requireContext(), spanCount);
        rvCasts.setLayoutManager(manager);
        rvCasts.setHasFixedSize(true);
        CastAdapter castAdapter = new CastAdapter();
        castAdapter.setConfigAdapter(new CustomAdapter.ConfigAdapter()
                .setLayoutManager(CustomAdapter.ConfigAdapter.GRID_LAYOUT)
                .setGridCount(spanCount));
        rvCasts.setAdapter(castAdapter);
        castAdapter.setCastEntities(list.getCasts());
        castAdapter.notifyDataSetChanged();
    }
}

package com.jumadi.moviecatalogue.ui.category;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Genres;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends CustomAdapter<CategoryAdapter.ViewHolder> {

    private List<Genres.GenreEntity> genreEntities = new ArrayList<>();
    private final AdapterCallBack callBack;
    private final int[] resIdIcons;
    private final String[] nameIcons;

    CategoryAdapter(AdapterCallBack callBack, int[] resIdIcons, String[] nameIcons) {
        this.callBack = callBack;
        this.resIdIcons = resIdIcons;
        this.nameIcons = nameIcons;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_genre, parent, false);
        return new ViewHolder(setViewLayout(parent, view, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Genres.GenreEntity genreEntity = genreEntities.get(position);

        try {
            if (genreEntity.getGenre().equals(nameIcons[position]))
                holder.imgIcon.setImageResource(resIdIcons[position]);
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        holder.txtName.setText(genreEntity.getGenre());
        holder.itemView.setOnClickListener(view -> callBack.to(genreEntity));

    }

    @Override
    public int getItemSize() {
        return genreEntities.size();
    }

    void setGenreEntities(List<Genres.GenreEntity> genreEntities) {
        this.genreEntities = genreEntities;
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        final ImageView imgIcon;
        final TextView txtName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.img_icon);
            txtName = itemView.findViewById(R.id.txt_name);
        }
    }
}

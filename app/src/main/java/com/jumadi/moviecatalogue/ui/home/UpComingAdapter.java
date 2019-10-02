package com.jumadi.moviecatalogue.ui.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.CustomAdapter;
import com.jumadi.moviecatalogue.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL_NORMAL;

class UpComingAdapter extends CustomAdapter<UpComingAdapter.ViewHolder> {

    private List<MovieEntity> movieEntities = new ArrayList<>();
    private final AdapterCallBack adapterCallBack;

    UpComingAdapter(AdapterCallBack adapterCallBack) {
        this.adapterCallBack = adapterCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_upcoming, parent, false);
        return new ViewHolder(setViewLayout(parent, view, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MovieEntity movie = movieEntities.get(position);
        Context context = holder.itemView.getContext();

        if (movie == null) return;

        Glide.with(context)
                .load(IMG_URL_NORMAL+movie.getBackdropPath())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgBackdrop);
        holder.title.setText(movie.getTitle());
        holder.releaseDate.setText(Utils.parseDate(movie.getReleaseDate(), "MMMM"));

        if (Utils.isUpComing(movie.getReleaseDate(), 7, 0))
            holder.txtAiring.setVisibility(View.VISIBLE);
        else
            holder.txtAiring.setVisibility(View.GONE);

        holder.itemView.setOnClickListener(view -> adapterCallBack.to(movie.getId()));
    }

    @Override
    public int getItemSize() {
        return movieEntities.size();
    }

    void setMovieEntities(List<MovieEntity> movieEntities) {
        this.movieEntities = movieEntities;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgBackdrop;
        final TextView txtAiring;
        final TextView title;
        final TextView releaseDate;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgBackdrop = itemView.findViewById(R.id.img_backdrop);
            txtAiring = itemView.findViewById(R.id.txt_airing);
            title = itemView.findViewById(R.id.txt_title);
            releaseDate = itemView.findViewById(R.id.txt_release_date);

        }
    }
}

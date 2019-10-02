package com.jumadi.moviecatalogue.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Images;
import com.jumadi.moviecatalogue.in.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL;

public class ImageAdapter extends CustomAdapter<ImageAdapter.ViewHolder> {

    private List<Images.ImageEntity> imageEntities = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_image, parent, false);
        return new ViewHolder(setViewLayout(parent, view, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Images.ImageEntity image = imageEntities.get(position);
        Context context = holder.itemView.getContext();

        if (holder.itemView instanceof ImageView) {
            ImageView imageView = (ImageView) holder.itemView;
            Glide.with(context)
                    .load(IMG_URL+image.getFilePath())
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error).apply(
                    RequestOptions.bitmapTransform(new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.round_radius_size))))
                    .into(imageView);
        }
    }

    @Override
    public int getItemSize() {
        return imageEntities.size();
    }

    void setImageEntities(List<Images.ImageEntity> imageEntities) {
        this.imageEntities = imageEntities;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}

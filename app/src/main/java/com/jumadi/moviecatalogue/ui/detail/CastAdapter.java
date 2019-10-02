package com.jumadi.moviecatalogue.ui.detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.entitiy.Credits;
import com.jumadi.moviecatalogue.in.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL;

public class CastAdapter  extends CustomAdapter<CastAdapter.ViewHolder> {

    private List<Credits.CastEntity> castEntities = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_credit, parent, false);
        return new ViewHolder(setViewLayout(parent, view, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Credits.CastEntity castEntity = castEntities.get(position);
        Context context = holder.itemView.getContext();

        if (castEntity == null) return;

        holder.txtName.setText(castEntity.getName());
        holder.txtCharacter.setText(castEntity.getCharacter());
        Glide.with(context)
                .load(IMG_URL+castEntity.getProfilePath())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error).apply(
                RequestOptions.bitmapTransform(new CircleCrop()))
                .into(holder.imgProfile);
    }

    @Override
    public int getItemSize() {
        return castEntities.size();
    }

    public void setCastEntities(List<Credits.CastEntity> castEntities) {
        this.castEntities = castEntities;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ImageView imgProfile;
        final TextView txtName;
        final TextView txtCharacter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProfile = itemView.findViewById(R.id.img_profile);
            txtName = itemView.findViewById(R.id.txt_name);
            txtCharacter = itemView.findViewById(R.id.txt_character);
        }
    }
}

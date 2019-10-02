package com.jumadi.moviecatalogue.ui.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.api.ApiService;
import com.jumadi.moviecatalogue.data.entitiy.Videos;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.in.CustomAdapter;

import java.util.ArrayList;
import java.util.List;

public class VideoAdapter extends CustomAdapter<VideoAdapter.ViewHolder> {

    private List<Videos.VideoEntity> videoEntities = new ArrayList<>();
    private final ThumbnailListener thumbnailListener = new ThumbnailListener();

    private final AdapterCallBack callBack;

    VideoAdapter(AdapterCallBack callBack) {
        this.callBack = callBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_video, parent, false);
        return new ViewHolder(setViewLayout(parent, view, viewType));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Videos.VideoEntity video = videoEntities.get(position);

        holder.thumbnailView.setOnClickListener(view -> callBack.to(video.getKey()));

        if (holder.thumbnailView.getTag() == null) {
            holder.thumbnailView.setTag(video.getKey());
            holder.thumbnailView.initialize(ApiService.API_KEY_YOUTUBE, thumbnailListener);
        }
    }

    @Override
    public int getItemSize() {
        return videoEntities.size();
    }

    void setVideoList(List<Videos.VideoEntity> videoList) {
        this.videoEntities = videoList;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final YouTubeThumbnailView thumbnailView;
        final ImageView imgPlay;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailView = itemView.findViewById(R.id.youtube_thumb);
            imgPlay = itemView.findViewById(R.id.img_play);
        }
    }

    private final class ThumbnailListener implements
            YouTubeThumbnailView.OnInitializedListener,
            YouTubeThumbnailLoader.OnThumbnailLoadedListener {

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView view, YouTubeThumbnailLoader loader) {
            loader.setOnThumbnailLoadedListener(this);
            view.setImageResource(R.drawable.video_loading);
            String videoId = (String) view.getTag();
            loader.setVideo(videoId);
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView view, YouTubeInitializationResult loader) {
            view.setImageResource(R.drawable.video_error);
        }

        @Override
        public void onThumbnailLoaded(YouTubeThumbnailView view, String videoId) {
        }

        @Override
        public void onThumbnailError(YouTubeThumbnailView view, YouTubeThumbnailLoader.ErrorReason errorReason) {
            view.setImageResource(R.drawable.video_error);
        }
    }
}

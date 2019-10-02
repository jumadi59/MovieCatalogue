package com.jumadi.moviecatalogue.ui.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.api.StatusResponse;
import com.jumadi.moviecatalogue.data.api.repository.NetworkState;
import com.jumadi.moviecatalogue.data.entitiy.MovieEntity;
import com.jumadi.moviecatalogue.in.AdapterCallBack;
import com.jumadi.moviecatalogue.utils.Utils;
import com.victor.loading.rotate.RotateLoading;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;

import static com.jumadi.moviecatalogue.data.api.ApiService.IMG_URL;
import static com.jumadi.moviecatalogue.utils.Utils.dpToPx;

public class MovieAdapter extends PagedListAdapter<MovieEntity, RecyclerView.ViewHolder> {

    public static final DiffUtil.ItemCallback<MovieEntity> DIFF_CALLBACK = new DiffUtil.ItemCallback<MovieEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull MovieEntity oldItem, @NonNull MovieEntity newItem) {
            return oldItem.equals(newItem);
        }

    };

    private NetworkState networkState;
    private final AdapterCallBack adapterCallBack;

    public MovieAdapter(@NonNull DiffUtil.ItemCallback<MovieEntity> diffCallback, AdapterCallBack adapterCallBack) {
        super(diffCallback);
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasExtraRow() && position == getItemCount() - 1) {
            return R.layout.layout_item_loading;
        } else {
            return R.layout.layout_item;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == R.layout.layout_item) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == R.layout.layout_item_loading){
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_loading, parent, false);
            return new LoadingViewHolder(view);
        }else {
            throw new IllegalArgumentException("unknown view type");
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof ItemViewHolder) {
            MovieEntity movieEntity = getItem(position);
            ItemViewHolder holder = ((ItemViewHolder) viewHolder);
            Context context = holder.itemView.getContext();

            if (movieEntity == null) return;

            Glide.with(context)
                    .load(IMG_URL+movieEntity.getPosterPath())
                    .placeholder(R.drawable.image_loading)
                    .error(R.drawable.image_error).apply(
                    RequestOptions.bitmapTransform(new RoundedCorners(context.getResources().getDimensionPixelSize(R.dimen.round_radius_size))))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(holder.imgPoster);

            holder.txtTitle.setText(movieEntity.getTitle());
            holder.txtReleaseDate.setText(Utils.parseDate(movieEntity.getReleaseDate(), "MMM"));
            holder.txtDescription.setText(movieEntity.getOverview());

            float rating = (float) (movieEntity.getVoteAverage() * 5 / 10);
            holder.ratings.setRating(rating);

            holder.cardView.setOnClickListener(v -> adapterCallBack.to(movieEntity.getId()));

        } else if (viewHolder instanceof LoadingViewHolder) {
            LoadingViewHolder holder = ((LoadingViewHolder) viewHolder);
            holder.btnRetry.setOnClickListener(view -> adapterCallBack.retry());
            holder.contentError.setOnClickListener(view -> adapterCallBack.retry());

            if (networkState !=null) {
                switch (networkState.status) {
                    case SUCCESS:
                        holder.contentError.setVisibility(View.GONE);
                        holder.rotateLoading.stop();
                        holder.rotateLoading.setVisibility(View.GONE);
                        break;
                    case ERROR:
                        holder.contentError.setVisibility(View.VISIBLE);
                        holder.txtMsgError.setText((networkState.message == null) ? holder.itemView.getContext().getText(R.string.error_msg) : networkState.message);
                        holder.rotateLoading.stop();
                        holder.rotateLoading.setVisibility(View.GONE);
                        break;
                    case LOADING:
                        holder.rotateLoading.start();
                        holder.rotateLoading.setVisibility(View.VISIBLE);
                        holder.contentError.setVisibility(View.GONE);
                        break;
                }
            }
        }

    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (hasExtraRow() ? 1 : 0);
    }

    private boolean hasExtraRow() {
        return networkState != null && networkState.status != StatusResponse.SUCCESS;
    }

    public void networkState(NetworkState networkState) {
        NetworkState previousState = this.networkState;
        boolean hadExtraRow = hasExtraRow();
        this.networkState = networkState;
        boolean hasExtraRow = hasExtraRow();
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(getItemCount());
            } else {
                notifyItemInserted(getItemCount());
            }
        } else if (hasExtraRow && previousState != networkState) {
            notifyItemChanged(getItemCount() - 1);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        final CardView cardView;
        final ImageView imgPoster;
        final TextView txtTitle;
        final TextView txtReleaseDate;
        final TextView txtDescription;
        final MaterialRatingBar ratings;

        ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.cardView);
            imgPoster = itemView.findViewById(R.id.img_poster);
            txtTitle = itemView.findViewById(R.id.txt_title);
            txtReleaseDate = itemView.findViewById(R.id.txt_release_date);
            txtDescription = itemView.findViewById(R.id.txt_description);
            ratings = itemView.findViewById(R.id.ratings);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                 ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) imgPoster.getLayoutParams();
                 lp.setMargins(lp.leftMargin, lp.topMargin, lp.rightMargin, lp.bottomMargin + dpToPx(8));
                 imgPoster.setLayoutParams(lp);
            }
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        final RotateLoading rotateLoading;
        final LinearLayout contentError;
        final ImageButton btnRetry;
        final TextView txtMsgError;

        LoadingViewHolder(@NonNull View itemView) {
            super(itemView);

            rotateLoading = itemView.findViewById(R.id.rotate_loading);
            contentError = itemView.findViewById(R.id.content_error);
            btnRetry = itemView.findViewById(R.id.btn_retry);
            txtMsgError = itemView.findViewById(R.id.txt_msg_error);

        }
    }
}

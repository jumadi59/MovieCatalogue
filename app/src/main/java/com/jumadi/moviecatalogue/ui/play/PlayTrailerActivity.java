package com.jumadi.moviecatalogue.ui.play;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.jumadi.moviecatalogue.R;
import com.jumadi.moviecatalogue.data.api.ApiService;

public class PlayTrailerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    public static final String EXTRA_ID_TRAILER = "extra_id_trailer";
    private static final int RECOVERY_DIALOG_REQUEST = 1;

    private YouTubePlayerView playerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_trailer);
        playerView = findViewById(R.id.youtube_view);
        playerView.initialize(ApiService.API_KEY_YOUTUBE, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        youTubePlayer.loadVideo(getIntent().getStringExtra(EXTRA_ID_TRAILER));

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {
        if (errorReason.isUserRecoverableError()) {
            errorReason.getErrorDialog(this, RECOVERY_DIALOG_REQUEST).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_DIALOG_REQUEST) {
            if (getYouTubePlayerProvider() != null) {
                getYouTubePlayerProvider().initialize(ApiService.API_KEY_YOUTUBE, this);
            }
        }
    }

    private YouTubePlayer.Provider getYouTubePlayerProvider() {
        return playerView;
    }

}

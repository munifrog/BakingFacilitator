package com.example.bakingfacilitator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.util.Media;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.net.URL;

public class ViewerActivity extends AppCompatActivity implements Media.Listener {
    public static final String PARCELABLE_DIRECTION = "one_direction";

    private SimpleExoPlayerView mPlayerView;
    private Media mMedia;
    private Direction mDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(PARCELABLE_DIRECTION)) {
            finish();
        } else {
            mDirection = intent.getParcelableExtra(PARCELABLE_DIRECTION);

            TextView tvDirection = findViewById(R.id.tv_description);
            tvDirection.setText(mDirection.getDescribeFull());

            mPlayerView = findViewById(R.id.exo_player_frame);
            URL video = mDirection.getUrlVideo();
            if (video == null) {
                mPlayerView.setVisibility(View.GONE);
            } else {
                ProgressBar pb = findViewById(R.id.pb_loading_indicator);
                pb.setVisibility(View.VISIBLE);

                mPlayerView.setVisibility(View.VISIBLE);
                Uri uri = Uri.parse(video.toString());
                mMedia = new Media(this, this, uri);
                mPlayerView.setPlayer(mMedia.getPlayer());
            }
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (!isLoading) {
            ProgressBar pb = findViewById(R.id.pb_loading_indicator);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMedia != null) {
            mMedia.destroy();
        }
    }
}

package com.example.bakingfacilitator.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.util.Media;
import com.google.android.exoplayer2.ui.PlayerView;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ViewerActivity extends AppCompatActivity implements Media.Listener {
    public static final String PARCELABLE_DIRECTION_ARRAY = "all_directions";
    public static final String CURRENT_DIRECTION_INDEX = "current_direction";

    private PlayerView mPlayerView;
    private Media mMedia;
    private List<Direction> mDirections;
    private int mCurrentIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Intent intent = getIntent();

        if (intent == null || !intent.hasExtra(PARCELABLE_DIRECTION_ARRAY)) {
            finish();
        } else {
            mDirections = intent.getParcelableArrayListExtra(PARCELABLE_DIRECTION_ARRAY);
            mCurrentIndex = intent.getIntExtra(CURRENT_DIRECTION_INDEX, 0);
            Direction direction = mDirections.get(mCurrentIndex);

            TextView tvDirection = findViewById(R.id.tv_description);
            tvDirection.setText(direction.getDescribeFull());

            mPlayerView = findViewById(R.id.exo_player_frame);
            URL video = direction.getUrlVideo();
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
    protected void onDestroy() {
        super.onDestroy();
        if (mMedia != null) {
            mMedia.destroy();
        }
    }

    public void onPreviousClick(View view) {
        int newIndex = mCurrentIndex - 1;
        if (newIndex < 0) {
            Toast.makeText(this, getString(R.string.announce_none_before), Toast.LENGTH_LONG).show();
        } else {
            navigate(newIndex);
        }
    }

    public void onNextClick(View view) {
        int newIndex = mCurrentIndex + 1;
        if (newIndex >= mDirections.size()) {
            Toast.makeText(this, getString(R.string.announce_none_after), Toast.LENGTH_LONG).show();
        } else {
            navigate(newIndex);
        }
    }

    private void navigate(int newPosition) {
        Intent intent = new Intent(ViewerActivity.this, ViewerActivity.class);
        intent.putParcelableArrayListExtra(
                PARCELABLE_DIRECTION_ARRAY,
                (ArrayList<Direction>) mDirections
        );
        intent.putExtra(CURRENT_DIRECTION_INDEX, newPosition);
        finish();
        startActivity(intent);
    }
}

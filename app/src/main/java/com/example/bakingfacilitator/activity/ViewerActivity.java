package com.example.bakingfacilitator.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.util.Media;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

import static com.example.bakingfacilitator.activity.RecipeActivity.CURRENT_DIRECTION;

public class ViewerActivity extends AppCompatActivity implements Media.Listener,
        ExoPlayer.EventListener
{
    public static final String PARCELABLE_DIRECTION_ARRAY = "all_directions";
    public static final String CURRENT_DIRECTION_INDEX = "current_direction";

    private static final String MSG_FAILED_TO_CONNECT = "Unable to connect";

    private PlayerView mPlayerView;
    private Media mMedia;
    private List<Direction> mDirections;
    private int mCurrentIndex;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private ImageView mDefaultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(CURRENT_DIRECTION_INDEX);
        }

        Configuration config = getResources().getConfiguration();
        if (config.screenWidthDp > 300 && config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // https://stackoverflow.com/questions/10407159/how-to-manage-startactivityforresult-on-android
            Intent returnIntent = new Intent();
            returnIntent.putExtra(CURRENT_DIRECTION, mCurrentIndex);
            setResult(RESULT_OK, returnIntent);
            finish();
        }

        Intent intent = getIntent();
        if (intent == null || !intent.hasExtra(PARCELABLE_DIRECTION_ARRAY)) {
            finish();
        } else {
            mDirections = intent.getParcelableArrayListExtra(PARCELABLE_DIRECTION_ARRAY);
            mCurrentIndex = intent.getIntExtra(CURRENT_DIRECTION_INDEX, 0);
        }

        mPlayerView = findViewById(R.id.exo_player_frame);
        mErrorMessage = findViewById(R.id.tv_error_video_load);
        mProgressBar = findViewById(R.id.pb_loading_indicator);
        mDefaultImage = findViewById(R.id.iv_default_image);

        setupDirectionView();
    }

    private void setupDirectionView() {
        Direction direction = mDirections.get(mCurrentIndex);

        TextView tvDirection = findViewById(R.id.tv_description);
        tvDirection.setText(direction.getDescribeFull());

        if (mMedia != null) {
            mMedia.destroy();
        }
        mErrorMessage.setVisibility(View.GONE);
        URL video = direction.getUrlVideo();
        if (video == null) {
            mPlayerView.setVisibility(View.GONE);
            mDefaultImage.setVisibility(View.VISIBLE);

            URL imageUrl = direction.getUrlThumb();
            if (imageUrl != null) {
                Uri imageUri = Uri.parse(imageUrl.toString());
                if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                    Picasso.Builder picassoBuilder = new Picasso.Builder(this).listener(new Picasso.Listener() {
                        @Override
                        public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                            mErrorMessage.setText(getString(R.string.error_image_not_available));
                            mErrorMessage.setVisibility(View.VISIBLE);
                        }
                    });
                    Picasso picasso = picassoBuilder.build();
                    picasso.load(imageUri).placeholder(R.drawable.preparation).into(mDefaultImage);
                }
            }
        } else {
            mProgressBar.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.VISIBLE);
            mDefaultImage.setVisibility(View.GONE);

            Uri uri = Uri.parse(video.toString());
            mMedia = new Media(this, this, uri);

            ExoPlayer player = mMedia.getPlayer();
            player.addListener(this);
            mPlayerView.setPlayer(player);
        }
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (error.getMessage().contains(MSG_FAILED_TO_CONNECT)) {
            mErrorMessage.setText(getString(R.string.error_video_not_available));
            mErrorMessage.setVisibility(View.VISIBLE);
            mPlayerView.setVisibility(View.GONE);
            mDefaultImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        switch (playbackState) {
            case Player.STATE_READY:
            case Player.STATE_IDLE:
                mProgressBar.setVisibility(View.GONE);
                break;
            default:
            case Player.STATE_BUFFERING:
            case Player.STATE_ENDED:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMedia != null) {
            mMedia.getPlayer().removeListener(this);
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
        mCurrentIndex = newPosition;
        setupDirectionView();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_DIRECTION_INDEX, mCurrentIndex);
    }
}

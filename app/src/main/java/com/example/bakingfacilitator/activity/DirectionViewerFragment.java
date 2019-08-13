package com.example.bakingfacilitator.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.util.Media;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.net.URL;

import static com.example.bakingfacilitator.activity.RecipeActivity.CURRENT_PLAYBACK_POSITION;
import static com.example.bakingfacilitator.activity.RecipeActivity.CURRENT_PLAYBACK_STATE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionViewerFragment extends Fragment implements ExoPlayer.EventListener {
    private static final String PARCELABLE_DIRECTION = "one_direction";
    private static final String MSG_FAILED_TO_CONNECT = "Unable to connect";

    private PlayerView mPlayerView;
    private Media mMedia;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;
    private ImageView mDefaultImage;

    public DirectionViewerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param direction Current Direction.
     * @return A new instance of fragment DirectionViewerFragment.
     */
    static DirectionViewerFragment newInstance(
            Direction direction,
            long playPosition,
            boolean playImmediately
    ) {
        DirectionViewerFragment fragment = new DirectionViewerFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARCELABLE_DIRECTION, direction);
        args.putLong(CURRENT_PLAYBACK_POSITION, playPosition);
        args.putBoolean(CURRENT_PLAYBACK_STATE, playImmediately);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(
                R.layout.fragment_direction_viewer,
                container,
                false
        );

        if (getArguments() != null) {
            LinearLayout buttonView = view.findViewById(R.id.btn_direction_navigation);
            buttonView.setVisibility(View.GONE);

            Bundle args = getArguments();
            Direction direction = args.getParcelable(PARCELABLE_DIRECTION);

            long playPosition = args.getLong(CURRENT_PLAYBACK_POSITION);
            boolean playImmediately = args.getBoolean(CURRENT_PLAYBACK_STATE);

            TextView tvDirection = view.findViewById(R.id.tv_description);
            tvDirection.setText(direction.getDescribeFull());

            mPlayerView = view.findViewById(R.id.exo_player_frame);
            mErrorMessage = view.findViewById(R.id.tv_error_video_load);
            mDefaultImage = view.findViewById(R.id.iv_default_image);
            mProgressBar = view.findViewById(R.id.pb_loading_indicator);

            mErrorMessage.setVisibility(View.GONE);
            URL video = direction.getUrlVideo();
            if (video == null) {
                mPlayerView.setVisibility(View.GONE);
                mDefaultImage.setVisibility(View.VISIBLE);

                URL imageUrl = direction.getUrlThumb();
                if (imageUrl != null) {
                    Uri imageUri = Uri.parse(imageUrl.toString());
                    if (imageUri != null && imageUri.getPath() != null && !imageUri.getPath().isEmpty()) {
                        Picasso.Builder picassoBuilder = new Picasso.Builder(view.getContext()).listener(new Picasso.Listener() {
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
                mMedia = new Media(view.getContext(), uri);

                ExoPlayer player = mMedia.getPlayer();
                player.setPlayWhenReady(playImmediately);
                player.seekTo(playPosition);
                player.addListener(this);
                mPlayerView.setPlayer(player);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mMedia != null) {
            mMedia.getPlayer().removeListener(this);
            mMedia.destroy();
        }
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
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (mPlayerView != null && mMedia != null) {
            mPlayerView.setPlayer(mMedia.getPlayer());
        }
    }

    long getCurrentPlayPosition() {
        long result = 0;
        if (mMedia != null) {
            result = mMedia.getPlayer().getCurrentPosition();
        }
        return result;
    }

    boolean getCurrentPlayImmediatelyState() {
        boolean result = true;
        if (mMedia != null) {
            result = mMedia.getPlayer().getPlayWhenReady();
        }
        return result;
    }
}

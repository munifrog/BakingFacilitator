package com.example.bakingfacilitator.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
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

import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DirectionViewerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DirectionViewerFragment extends Fragment implements Media.Listener,
        ExoPlayer.EventListener
{
    private static final String PARCELABLE_DIRECTION = "one_direction";
    private static final String MSG_FAILED_TO_CONNECT = "Unable to connect";

    private PlayerView mPlayerView;
    private Media mMedia;
    private ProgressBar mProgressBar;
    private TextView mErrorMessage;

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
    static DirectionViewerFragment newInstance(Direction direction) {
        DirectionViewerFragment fragment = new DirectionViewerFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARCELABLE_DIRECTION, direction);
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

            Direction direction = getArguments().getParcelable(PARCELABLE_DIRECTION);

            TextView tvDirection = view.findViewById(R.id.tv_description);
            tvDirection.setText(direction.getDescribeFull());

            FrameLayout exoFrame = view.findViewById(R.id.exo_frame_and_progress);
            mPlayerView = view.findViewById(R.id.exo_player_frame);
            mErrorMessage = view.findViewById(R.id.tv_error_video_load);
            URL video = direction.getUrlVideo();

            if (video == null) {
                exoFrame.setVisibility(View.GONE);
            } else {
                mProgressBar = view.findViewById(R.id.pb_loading_indicator);
                mProgressBar.setVisibility(View.VISIBLE);
                mErrorMessage.setVisibility(View.GONE);
                exoFrame.setVisibility(View.VISIBLE);

                Uri uri = Uri.parse(video.toString());
                mMedia = new Media(view.getContext(), this, uri);

                ExoPlayer player = mMedia.getPlayer();
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
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        if (error.getMessage().contains(MSG_FAILED_TO_CONNECT)) {
            mErrorMessage.setVisibility(View.VISIBLE);
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
}

package com.example.bakingfacilitator.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.adapt.LinearDirectionAdapter;
import com.example.bakingfacilitator.adapt.LinearIngredientAdapter;
import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.model.Recipe;

import java.util.ArrayList;

import static com.example.bakingfacilitator.activity.ViewerActivity.CURRENT_DIRECTION_INDEX;
import static com.example.bakingfacilitator.activity.ViewerActivity.PARCELABLE_DIRECTION_ARRAY;

public class RecipeActivity extends AppCompatActivity implements LinearIngredientAdapter.Listener,
        LinearDirectionAdapter.Listener
{
    public static final String PARCELABLE_RECIPE = "one_recipe";
    public static final String CURRENT_DIRECTION = "current_direction";
    public static final String CURRENT_PLAYBACK_POSITION = "current_playback_position";
    public static final String CURRENT_PLAYBACK_STATE = "current_playback_state";

    private static final int RESULT_REQUEST_CODE = 132;

    private Recipe mRecipe;
    private boolean mTwoPane;
    private int mCurrentDirection;
    private FragmentManager mFragmentManager;
    private DirectionViewerFragment mFragment;
    private long mLastPlayPosition;
    private boolean mLastPlayImmediatelyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARCELABLE_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(PARCELABLE_RECIPE);
            mCurrentDirection = savedInstanceState.getInt(
                    CURRENT_DIRECTION,
                    0
            );
            mLastPlayPosition = savedInstanceState.getLong(
                    CURRENT_PLAYBACK_POSITION,
                    mLastPlayPosition
            );
            mLastPlayImmediatelyState = savedInstanceState.getBoolean(
                    CURRENT_PLAYBACK_STATE,
                    mLastPlayImmediatelyState
            );
        } else {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(PARCELABLE_RECIPE)) {
                finish();
            } else {
                mRecipe = intent.getParcelableExtra(PARCELABLE_RECIPE);
                mCurrentDirection = 0;
                mLastPlayPosition = 0;
                mLastPlayImmediatelyState = true;
            }
        }

        TextView tvName = findViewById(R.id.tv_recipe_name);
        tvName.setText(mRecipe.getRecipeName());

        RecyclerView rvIngredients = findViewById(R.id.rv_ingredients);
        LinearLayoutManager ingredientManager = new LinearLayoutManager(this);
        rvIngredients.setLayoutManager(ingredientManager);
        LinearIngredientAdapter ingredientAdapter = new LinearIngredientAdapter(
                mRecipe.getIngredients(),
                mRecipe.getChecked(),
                this
        );
        rvIngredients.setAdapter(ingredientAdapter);

        RecyclerView rvDirections = findViewById(R.id.rv_directions);
        LinearLayoutManager directionManager = new LinearLayoutManager(this);
        rvDirections.setLayoutManager(directionManager);
        LinearDirectionAdapter directionAdapter = new LinearDirectionAdapter(
                mRecipe.getDirections(),
                this
        );
        rvDirections.setAdapter(directionAdapter);

        mTwoPane = findViewById(R.id.two_pane_view) != null;
        if (mTwoPane) {
            mFragmentManager = getSupportFragmentManager();
        }
    }

    @Override
    public void onIngredientClick(int position) {
        mRecipe.setChecked(position, mRecipe.getChecked(position));
    }

    @Override
    public void onDirectionClick(int position) {
        if (mTwoPane) {
            if (position != mCurrentDirection) {
                mCurrentDirection = position;
                mLastPlayPosition = 0;
                mLastPlayImmediatelyState = true;
                replaceFragment();
            }
        } else {
            mCurrentDirection = position;
            Intent intent = new Intent(RecipeActivity.this, ViewerActivity.class);
            intent.putParcelableArrayListExtra(
                    PARCELABLE_DIRECTION_ARRAY,
                    (ArrayList<Direction>) mRecipe.getDirections()
            );
            intent.putExtra(CURRENT_DIRECTION_INDEX, position);
            startActivityForResult(intent, RESULT_REQUEST_CODE);
        }
    }

    private void generateNewFragment() {
        releaseFragment();
        mFragment = DirectionViewerFragment.newInstance(
                mRecipe.getDirections().get(mCurrentDirection),
                mLastPlayPosition,
                mLastPlayImmediatelyState
        );
    }

    private void addFragment() {
        if (mTwoPane && mFragmentManager != null) {
            generateNewFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_direction, mFragment)
                    .commit();
        }
    }

    private void replaceFragment() {
        if (mTwoPane && mFragmentManager != null) {
            generateNewFragment();
            mFragmentManager.beginTransaction()
                    .replace(R.id.fragment_direction, mFragment)
                    .commit();
        }
    }

    private void removeFragment() {
        if (mTwoPane && mFragmentManager != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment) // audio stops completely
                    .commit();
            releaseFragment();
        }
    }

    private void releaseFragment() {
        if (mFragment != null) {
            mFragment.release();
            mFragment = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            addFragment();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            if (mTwoPane && mFragmentManager != null) {
                addFragment();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Default to using the previous values since the user might tilt back again
        if (mTwoPane && mFragment != null) {
            mLastPlayPosition = mFragment.getCurrentPlayPosition();
            mLastPlayImmediatelyState = mFragment.getCurrentPlayImmediatelyState();
        }
        removeFragment();
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unable to remove fragment this late;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_REQUEST_CODE == requestCode && data != null && RESULT_OK == resultCode) {
            mCurrentDirection = data.getIntExtra(CURRENT_DIRECTION, 0);
            mLastPlayPosition = data.getLongExtra(CURRENT_PLAYBACK_POSITION, 0);
            mLastPlayImmediatelyState = data.getBooleanExtra(CURRENT_PLAYBACK_STATE, true);
            replaceFragment();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLE_RECIPE, mRecipe);
        outState.putInt(CURRENT_DIRECTION, mCurrentDirection);
        outState.putLong(CURRENT_PLAYBACK_POSITION, mLastPlayPosition);
        outState.putBoolean(CURRENT_PLAYBACK_STATE, mLastPlayImmediatelyState);
    }
}

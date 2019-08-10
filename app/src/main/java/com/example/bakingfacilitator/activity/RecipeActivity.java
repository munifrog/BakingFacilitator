package com.example.bakingfacilitator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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

    private Recipe mRecipe;
    private boolean mTwoPane;
    private int mCurrentDirection;
    private FragmentManager mFragmentManager;
    private DirectionViewerFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARCELABLE_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(PARCELABLE_RECIPE);
            mCurrentDirection = savedInstanceState.getInt(CURRENT_DIRECTION, 0);
        } else {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(PARCELABLE_RECIPE)) {
                finish();
            } else {
                mRecipe = intent.getParcelableExtra(PARCELABLE_RECIPE);
                mCurrentDirection = 0;
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
            mFragment = DirectionViewerFragment.newInstance(
                    mRecipe.getDirections().get(mCurrentDirection)
            );
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
                mFragment = DirectionViewerFragment.newInstance(
                        mRecipe.getDirections().get(mCurrentDirection)
                );
                mFragmentManager.beginTransaction()
                        .replace(R.id.fragment_direction, mFragment)
                        .commit();
            }
        } else {
            mCurrentDirection = position;
            Intent intent = new Intent(RecipeActivity.this, ViewerActivity.class);
            intent.putParcelableArrayListExtra(
                    PARCELABLE_DIRECTION_ARRAY,
                    (ArrayList<Direction>) mRecipe.getDirections()
            );
            intent.putExtra(CURRENT_DIRECTION_INDEX, position);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mTwoPane && mFragmentManager != null) {
            mFragmentManager.beginTransaction()
                    .add(R.id.fragment_direction, mFragment)
                    .commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mTwoPane && mFragmentManager != null) {
            mFragmentManager.beginTransaction()
                    .remove(mFragment) // audio stops completely
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLE_RECIPE, mRecipe);
        outState.putInt(CURRENT_DIRECTION, mCurrentDirection);
    }
}

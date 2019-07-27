package com.example.bakingfacilitator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.adapt.LinearDirectionAdapter;
import com.example.bakingfacilitator.adapt.LinearIngredientAdapter;
import com.example.bakingfacilitator.model.Recipe;

import static com.example.bakingfacilitator.activity.ViewerActivity.PARCELABLE_DIRECTION;

public class RecipeActivity extends AppCompatActivity implements LinearIngredientAdapter.Listener,
        LinearDirectionAdapter.Listener
{
    public static final String PARCELABLE_RECIPE = "one_recipe";

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if (savedInstanceState != null && savedInstanceState.containsKey(PARCELABLE_RECIPE)) {
            mRecipe = savedInstanceState.getParcelable(PARCELABLE_RECIPE);
        } else {
            Intent intent = getIntent();
            if (intent == null || !intent.hasExtra(PARCELABLE_RECIPE)) {
                finish();
            } else {
                mRecipe = intent.getParcelableExtra(PARCELABLE_RECIPE);
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
    }

    @Override
    public void onIngredientClick(int position) {
        mRecipe.setChecked(position, mRecipe.getChecked(position));
    }

    @Override
    public void onDirectionClick(int position) {
        Intent intent = new Intent(RecipeActivity.this, ViewerActivity.class);
        intent.putExtra(PARCELABLE_DIRECTION, mRecipe.getDirections().get(position));
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCELABLE_RECIPE, mRecipe);
    }
}

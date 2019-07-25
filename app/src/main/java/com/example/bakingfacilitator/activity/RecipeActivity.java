package com.example.bakingfacilitator.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.adapt.LinearIngredientAdapter;
import com.example.bakingfacilitator.model.Ingredient;
import com.example.bakingfacilitator.model.Recipe;

import java.util.List;

public class RecipeActivity extends AppCompatActivity implements LinearIngredientAdapter.Listener {
    private static final String TAG = RecipeActivity.class.getSimpleName();

    public static final String PARCELABLE_RECIPE = "one_recipe";

    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        Log.v(TAG, "savedInstanceState: \"" + savedInstanceState + "\"");


        Intent intent = getIntent();
        Log.v(TAG, "intent: \"" + intent + "\"");

        if (intent == null || !intent.hasExtra(PARCELABLE_RECIPE)) {
            finish();
        } else {
            mRecipe = intent.getParcelableExtra(PARCELABLE_RECIPE);

            TextView tvName = findViewById(R.id.tv_recipe_name);
            tvName.setText(mRecipe.getRecipeName());

            List<Ingredient> ingredients = mRecipe.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                Log.v(TAG, "ingredients[" + i + "] = " + ingredients.get(i).getIngredientName());
            }

            RecyclerView rvIngredients = findViewById(R.id.rv_ingredients);
            LinearLayoutManager ingredientManager = new LinearLayoutManager(this);
            rvIngredients.setLayoutManager(ingredientManager);
            LinearIngredientAdapter ingredientAdapter = new LinearIngredientAdapter(
                    mRecipe.getIngredients(),
                    this
            );
            rvIngredients.setAdapter(ingredientAdapter);

        }
    }

    @Override
    public void onIngredientClick(int position) {

    }
}

package com.example.bakingfacilitator.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.adapt.GridMenuAdapter;
import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.model.ViewModel;
import com.example.bakingfacilitator.model.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import static com.example.bakingfacilitator.activity.RecipeActivity.PARCELABLE_RECIPE;

public class MenuActivity extends AppCompatActivity implements ViewModel.Listener,
        GridMenuAdapter.Listener
{
    public static final String PARCELABLE_RECIPE_LIST = "entire_recipe_array";

    private static final int COLUMN_SPAN_PORTRAIT_PHONE = 1;
    private static final int COLUMN_SPAN_PORTRAIT_TABLET = 1;
    private static final int COLUMN_SPAN_LANDSCAPE_PHONE = 2;
    private static final int COLUMN_SPAN_LANDSCAPE_TABLET = 3;

    private GridMenuAdapter mAdapter;
    private ViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAdapter = new GridMenuAdapter(this);
        setupViewModel();

        List<Recipe> recipes;
        if (savedInstanceState == null || !savedInstanceState.containsKey(PARCELABLE_RECIPE_LIST)) {
            ProgressBar pb = findViewById(R.id.pb_loading_indicator);
            pb.setVisibility(View.VISIBLE);
            recipes = mViewModel.getRecipes();
        } else {
            recipes = savedInstanceState.getParcelableArrayList(PARCELABLE_RECIPE_LIST);
        }
        mAdapter.setRecipes(recipes);

        Configuration config = getResources().getConfiguration();
        int spanCount = getColumnSpan(config.screenWidthDp, config.orientation);

        RecyclerView gridView = findViewById(R.id.rv_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        gridView.setLayoutManager(gridLayoutManager);
        gridView.setAdapter(mAdapter);
    }

    private void setupViewModel() {
        ViewModelFactory vmf = new ViewModelFactory(getApplication(), this);
        mViewModel = ViewModelProviders.of(this, vmf).get(ViewModel.class);
    }

    private int getColumnSpan(int screenWidth, int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (screenWidth < 300) ? COLUMN_SPAN_PORTRAIT_PHONE : COLUMN_SPAN_PORTRAIT_TABLET;
        } else { // orientation == Configuration.ORIENTATION_LANDSCAPE
            return (screenWidth < 300) ? COLUMN_SPAN_LANDSCAPE_PHONE : COLUMN_SPAN_LANDSCAPE_TABLET;
        }
    }

    @Override
    public void onInternetFailure() {
        Handler mainHandler = new Handler(getMainLooper());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Toast.makeText(
                        getApplicationContext(),
                        R.string.error_internet_failure,
                        Toast.LENGTH_LONG
                ).show();
            }
        };
        mainHandler.post(runnable);
    }

    @Override
    public void onClick(int position) {
        List<Recipe> recipes = mViewModel.getRecipes();
        if (recipes != null) {
            Intent recipeIntent = new Intent(MenuActivity.this, RecipeActivity.class);
            recipeIntent.putExtra(PARCELABLE_RECIPE, recipes.get(position));
            startActivity(recipeIntent);
        }
    }

    @Override
    public void onDatabaseSetChanged() {
        updateRecipesShown();
    }

    private void updateRecipesShown() {
        List<Recipe> recipes = mViewModel.getRecipes();
        if (recipes != null) {
            mAdapter.setRecipes(recipes);
            ProgressBar pb = findViewById(R.id.pb_loading_indicator);
            pb.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PARCELABLE_RECIPE_LIST, (ArrayList<Recipe>) mViewModel.getRecipes());
    }
}

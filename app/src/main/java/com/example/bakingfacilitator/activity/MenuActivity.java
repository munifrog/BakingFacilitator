package com.example.bakingfacilitator.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.adapt.GridMenuAdapter;
import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.thread.ExtractRecipes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements ExtractRecipes.Listener,
        GridMenuAdapter.Listener
{
    public static final String PARCELABLE_RECIPE_LIST = "entire_recipe_array";

    private static final int COLUMN_SPAN_PORTRAIT_PHONE = 1;
    private static final int COLUMN_SPAN_PORTRAIT_TABLET = 1;
    private static final int COLUMN_SPAN_LANDSCAPE_PHONE = 2;
    private static final int COLUMN_SPAN_LANDSCAPE_TABLET = 3;

    private List<Recipe> mRecipes;
    private GridMenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        mAdapter = new GridMenuAdapter(this);

        if (savedInstanceState == null || !savedInstanceState.containsKey(PARCELABLE_RECIPE_LIST)) {
            ExtractRecipes recipeGetter = new ExtractRecipes(this);
            String recipeDownload = getString(R.string.recipe_download_url);
            try {
                recipeGetter.execute(new URL(recipeDownload));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            List<Recipe> recipes = savedInstanceState.getParcelableArrayList(PARCELABLE_RECIPE_LIST);
            onLoad(recipes);
        }

        Configuration config = getResources().getConfiguration();
        int spanCount = getColumnSpan(config.screenWidthDp, config.orientation);

        RecyclerView gridView = findViewById(R.id.rv_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, spanCount);
        gridView.setLayoutManager(gridLayoutManager);
        gridView.setAdapter(mAdapter);
    }

    private int getColumnSpan(int screenWidth, int orientation) {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            return (screenWidth < 300) ? COLUMN_SPAN_PORTRAIT_PHONE : COLUMN_SPAN_PORTRAIT_TABLET;
        } else { // orientation == Configuration.ORIENTATION_LANDSCAPE
            return (screenWidth < 300) ? COLUMN_SPAN_LANDSCAPE_PHONE : COLUMN_SPAN_LANDSCAPE_TABLET;
        }
    }

    @Override
    public void onLoad(List<Recipe> recipes) {
        mRecipes = recipes;
        mAdapter.setRecipes(recipes);
    }

    @Override
    public void onInternetFailure() {
        Toast.makeText(this, getString(R.string.error_internet_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(int position) {
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(PARCELABLE_RECIPE_LIST, (ArrayList<Recipe>) mRecipes);
    }
}

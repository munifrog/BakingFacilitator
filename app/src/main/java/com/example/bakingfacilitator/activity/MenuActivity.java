package com.example.bakingfacilitator.activity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.thread.ExtractRecipes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MenuActivity extends AppCompatActivity implements ExtractRecipes.Listener {
    List<Recipe> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        ExtractRecipes recipeGetter = new ExtractRecipes(this);
        String recipeDownload = getString(R.string.recipe_download_url);
        try {
            recipeGetter.execute(new URL(recipeDownload));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad(List<Recipe> recipes) {
        mRecipes = recipes;
    }

    @Override
    public void onInternetFailure() {
        Toast.makeText(this, getString(R.string.error_internet_failure), Toast.LENGTH_LONG).show();
    }
}

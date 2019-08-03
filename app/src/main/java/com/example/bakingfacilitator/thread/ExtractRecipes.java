package com.example.bakingfacilitator.thread;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.bakingfacilitator.db.RecipeDatabase;
import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.model.ViewModel;
import com.example.bakingfacilitator.util.Http;
import com.example.bakingfacilitator.util.Json;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExtractRecipes extends AsyncTask<URL, Void, LiveData<List<Recipe>>> {
    private ViewModel mViewModel;
    private Listener mListener;

    public ExtractRecipes(ViewModel viewModel, Listener listener) {
        mViewModel = viewModel;
        mListener = listener;
    }

    public interface Listener {
        void onLoad(LiveData<List<Recipe>> recipes);
        void onInternetFailure();
    }

    @Override
    protected LiveData<List<Recipe>> doInBackground(URL... urls) {
        RecipeDatabase db = mViewModel.getDatabase();
        List<Recipe> recipes = new ArrayList<>();
        try {
            if (urls[0] != null) {
                recipes = Json.extractRecipe(Http.getResponse(urls[0]));
            } else {
                recipes = Json.extractRecipe(Http.getDefaultResponse());
            }
            List<Recipe> previousRecipes = db.dao().immediateLoadRecipes();
            if (previousRecipes != null) {
                db.dao().deleteRecipes(previousRecipes);
            }
            db.dao().insertRecipes(recipes);
            db.dao().immediateLoadRecipes();
        } catch (RuntimeException e) {
            e.printStackTrace();
            mListener.onInternetFailure();
        }

        return db.dao().signaledLoadRecipes();
    }

    @Override
    protected void onPostExecute(LiveData<List<Recipe>> recipes) {
        super.onPostExecute(recipes);
        mListener.onLoad(recipes);
    }
}

package com.example.bakingfacilitator.thread;

import android.os.AsyncTask;

import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.util.Http;
import com.example.bakingfacilitator.util.Json;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ExtractRecipes extends AsyncTask<URL, Void, List<Recipe>> {
    private Listener mListener;

    public ExtractRecipes(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onLoad(List<Recipe> recipes);
        void onInternetFailure();
    }

    @Override
    protected List<Recipe> doInBackground(URL... urls) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            if (urls[0] != null) {
                recipes = Json.extractRecipe(Http.getResponse(urls[0]));
            } else {
                recipes = Json.extractRecipe(Http.getDefaultResponse());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            mListener.onInternetFailure();
        }

        return recipes;
    }

    @Override
    protected void onPostExecute(List<Recipe> recipes) {
        super.onPostExecute(recipes);
        mListener.onLoad(recipes);
    }
}

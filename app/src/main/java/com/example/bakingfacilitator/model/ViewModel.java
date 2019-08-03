package com.example.bakingfacilitator.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.db.RecipeDatabase;
import com.example.bakingfacilitator.thread.ExtractRecipes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class ViewModel extends AndroidViewModel implements ExtractRecipes.Listener {

    private LiveData<List<Recipe>> mRecipesLive;
    private List<Recipe> mRecipes;
    private RecipeDatabase mDatabase;
    private Observer mObserver;
    private Listener mListener;

    ViewModel(@NonNull Application application, Listener listener) {
        super(application);

        mListener = listener;
        mDatabase = RecipeDatabase.getInstance(application);
        mObserver = new Observer<List<Recipe>>() {
            @Override
            public void onChanged(List<Recipe> recipes) {
                mRecipes = recipes;
                mListener.onDatabaseSetChanged();
            }
        };

        loadRecipes();
    }

    public interface Listener {
        void onInternetFailure();
        void onDatabaseSetChanged();
    }

    private void loadRecipes() {
        loadWebRecipes();
        loadDBRecipes();
    }

    private void loadWebRecipes() {
        ExtractRecipes recipeGetter = new ExtractRecipes(this,this);
        String recipeDownload = getApplication().getString(R.string.recipe_download_url);
        try {
            recipeGetter.execute(new URL(recipeDownload));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLoad(LiveData<List<Recipe>> recipes) {
        setLiveRecipes(recipes);
    }

    private void loadDBRecipes() {
        removeDBObserver();
        mRecipesLive = mDatabase.dao().signaledLoadRecipes();
        // noinspection unchecked
        mRecipesLive.observeForever(mObserver);
    }

    private void removeDBObserver() {
        if (mRecipesLive != null) {
            // noinspection unchecked
            mRecipesLive.removeObserver(mObserver);
        }
    }

    @Override
    public void onInternetFailure() {
        mListener.onInternetFailure();
    }

    public List<Recipe> getRecipes() { return mRecipes; }
    public LiveData<List<Recipe>> getLiveRecipes() { return mRecipesLive; }
    private void setLiveRecipes(LiveData<List<Recipe>> newRecipes) {
        if (newRecipes != null) {
            removeDBObserver();
            mRecipesLive = newRecipes;
            // noinspection unchecked
            mRecipesLive.observeForever(mObserver);
        }
    }

    public RecipeDatabase getDatabase() { return mDatabase; }
}

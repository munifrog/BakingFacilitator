package com.example.bakingfacilitator.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.bakingfacilitator.model.Recipe;

import java.util.List;

@Dao
public interface DataAccessObject {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long [] insertRecipes(List<Recipe> recipes);

    @Delete
    void deleteRecipes(List<Recipe> recipes);

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    LiveData<List<Recipe>> signaledLoadRecipes();

    @Query("SELECT * FROM recipes ORDER BY name ASC")
    List<Recipe> immediateLoadRecipes();
}

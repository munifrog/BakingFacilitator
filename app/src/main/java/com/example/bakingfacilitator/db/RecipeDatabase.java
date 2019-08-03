package com.example.bakingfacilitator.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.bakingfacilitator.model.Recipe;

@TypeConverters(RecipeTypeConverters.class)
@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
public abstract class RecipeDatabase extends RoomDatabase {

    private static final Object LOCK = new Object();
    private static final String DB_NAME = "stored_recipes";
    private static RecipeDatabase sInstance;

    public static RecipeDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(
                        context,
                        RecipeDatabase.class,
                        DB_NAME
                )
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return sInstance;
    }

    public abstract DataAccessObject dao();
}

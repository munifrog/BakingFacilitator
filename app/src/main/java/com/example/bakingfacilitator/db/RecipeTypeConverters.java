package com.example.bakingfacilitator.db;

import androidx.room.TypeConverter;

import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.model.Ingredient;

import java.util.List;

import static com.example.bakingfacilitator.util.Json.ingredientList2jsonString;
import static com.example.bakingfacilitator.util.Json.jsonString2ingredientList;
import static com.example.bakingfacilitator.util.Json.directionList2jsonString;
import static com.example.bakingfacilitator.util.Json.jsonString2directionList;

class RecipeTypeConverters {
    // The suggestion to use JSON strings for ArrayLists comes from
    // https://stackoverflow.com/questions/44986626/android-room-database-how-to-handle-arraylist-in-an-entity
    @TypeConverter
    public static String convertIngredients2Json(List<Ingredient> ingredients) {
        return ingredientList2jsonString(ingredients);
    }
    @TypeConverter
    public static List<Ingredient> convertJson2Ingredients(String json) {
        return jsonString2ingredientList(json);
    }

    @TypeConverter
    public static String convertDirection2Json(List<Direction> directions) {
        return directionList2jsonString(directions);
    }
    @TypeConverter
    public static List<Direction> convertJson2Directions(String json) {
        return jsonString2directionList(json);
    }
}

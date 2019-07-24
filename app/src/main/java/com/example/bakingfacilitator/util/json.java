package com.example.bakingfacilitator.util;

import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.model.Ingredient;
import com.example.bakingfacilitator.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class json {
    private static final String RECIPE_L01_ONE_ID                     = "id";
    private static final String RECIPE_L01_ONE_NAME                   = "name";
    private static final String RECIPE_L01_LIST_INGREDIENTS           = "ingredients";
    private static final String RECIPE_L02_INGREDIENT_ONE_AMOUNT      = "quantity";
    private static final String RECIPE_L02_INGREDIENT_ONE_UNIT        = "measure";
    private static final String RECIPE_L02_INGREDIENT_ONE_NAME        = "ingredient";
    private static final String RECIPE_L01_LIST_DIRECTION             = "steps";
    private static final String RECIPE_L02_DIRECTION_ONE_ORDER        = "id";
    private static final String RECIPE_L02_DIRECTION_ONE_DESC_SHORT   = "shortDescription";
    private static final String RECIPE_L02_DIRECTION_ONE_DESC_FULL    = "description";
    private static final String RECIPE_L02_DIRECTION_ONE_URL_VIDEO    = "videoURL";
    private static final String RECIPE_L02_DIRECTION_ONE_URL_THUMB    = "thumbnailURL";
    private static final String RECIPE_L01_ONE_SERVINGS               = "servings";
    private static final String RECIPE_L01_ONE_IMAGE                  = "image";

    public static List<Recipe> extractRecipe(String json) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipeArray = new JSONArray(json);
            int numRecipes = recipeArray.length();
            JSONObject jsonRecipe;

            long recipeId;
            String recipeName;
            JSONArray  jsonIngredients;
            JSONObject currentIngredient;
            double ingredientAmount;
            String ingredientUnit;
            String ingredientName;
            JSONArray  jsonDirections;
            JSONObject currentDirection;
            long directionOrder;
            String directionDescribeShort;
            String directionDescribeFull;
            String directionUrlVideo;
            String directionUrlThumb;
            int recipeServings;
            String recipeImage;

            for (int i = 0; i < numRecipes; i++) {
                jsonRecipe = (JSONObject) recipeArray.get(i);
                recipeId = jsonRecipe.getLong(RECIPE_L01_ONE_ID);
                recipeName = jsonRecipe.getString(RECIPE_L01_ONE_NAME);
                jsonIngredients = jsonRecipe.getJSONArray(RECIPE_L01_LIST_INGREDIENTS);
                List<Ingredient> recipeIngredients = new ArrayList<>();
                if (jsonIngredients != null) {
                    int numIngredients = jsonIngredients.length();
                    for (int j = 0; j < numIngredients; j++) {
                        currentIngredient = (JSONObject) jsonIngredients.get(j);
                        ingredientAmount = currentIngredient.getDouble(RECIPE_L02_INGREDIENT_ONE_AMOUNT);
                        ingredientUnit = currentIngredient.getString(RECIPE_L02_INGREDIENT_ONE_UNIT);
                        ingredientName = currentIngredient.getString(RECIPE_L02_INGREDIENT_ONE_NAME);
                        Ingredient newIngredient = new Ingredient(
                                ingredientName,
                                (float) ingredientAmount,
                                ingredientUnit
                        );
                        recipeIngredients.add(newIngredient);
                    }
                }
                jsonDirections = jsonRecipe.getJSONArray(RECIPE_L01_LIST_DIRECTION);
                List<Direction> recipeDirections = new ArrayList<>();
                if (jsonDirections != null) {
                    int numIngredients = jsonDirections.length();
                    for (int j = 0; j < numIngredients; j++) {
                        currentDirection = (JSONObject) jsonDirections.get(j);
                        directionOrder = currentDirection.getLong(RECIPE_L02_DIRECTION_ONE_ORDER);
                        directionDescribeShort = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_DESC_SHORT);
                        directionDescribeFull = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_DESC_FULL);
                        directionUrlVideo = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_URL_VIDEO);
                        directionUrlThumb = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_URL_THUMB);
                        Direction newDirection = new Direction(
                                directionOrder,
                                directionDescribeFull,
                                directionDescribeShort,
                                directionUrlVideo,
                                directionUrlThumb
                        );
                        recipeDirections.add(newDirection);
                    }
                }
                recipeServings = jsonRecipe.getInt(RECIPE_L01_ONE_SERVINGS);
                recipeImage = jsonRecipe.getString(RECIPE_L01_ONE_IMAGE);

                Recipe newRecipe = new Recipe(
                        recipeId,
                        recipeName,
                        recipeIngredients,
                        recipeDirections,
                        recipeServings,
                        recipeImage
                );
                recipes.add(newRecipe);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipes;
    }
}

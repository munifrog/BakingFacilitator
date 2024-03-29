package com.example.bakingfacilitator.util;

import com.example.bakingfacilitator.model.Direction;
import com.example.bakingfacilitator.model.Ingredient;
import com.example.bakingfacilitator.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Json {
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

    private static final String REGEX_FIND_INITIAL_NUMBERING          = "[0-9]+\\.\\s+";
    private static final String REGEX_REPLACE_INITIAL_NUMBERING       = "";
    private static final String REGEX_FIND_DEGREE_TEMPERATURE         = "\ufffd";
    private static final String REGEX_REPLACE_DEGREE_TEMPERATURE      = "\u00b0";
    private static final String REGEX_FIND_ENDING_PERIOD              = "\\.\\s*$";
    private static final String REGEX_REPLACE_ENDING_PERIOD           = "";
    private static final String REGEX_FIND_TOO_CLOSE_COMMA            = ",(\\S)";
    private static final String REGEX_REPLACE_TOO_CLOSE_COMMA         = ", $1";
    private static final String REGEX_FIND_TOO_CLOSE_PARENTHESES      = "(\\S)(\\()";
    private static final String REGEX_REPLACE_TOO_CLOSE_PARENTHESES   = "$1 $2";

    public static List<Recipe> extractRecipe(String json) {
        List<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray recipeArray = new JSONArray(json);
            int numRecipes = recipeArray.length();
            JSONObject jsonRecipe;

            long recipeId;
            String recipeName;
            int recipeServings;
            String recipeImage;

            for (int i = 0; i < numRecipes; i++) {
                jsonRecipe = (JSONObject) recipeArray.get(i);
                recipeId = jsonRecipe.getLong(RECIPE_L01_ONE_ID);
                recipeName = jsonRecipe.getString(RECIPE_L01_ONE_NAME);
                List<Ingredient> recipeIngredients = extractIngredients(
                        jsonRecipe.getJSONArray(RECIPE_L01_LIST_INGREDIENTS)
                );
                List<Direction> recipeDirections = extractDirections(
                        jsonRecipe.getJSONArray(RECIPE_L01_LIST_DIRECTION)
                );
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

    private static List<Ingredient> extractIngredients(JSONArray jsonIngredients) {
        List<Ingredient> recipeIngredients = new ArrayList<>();
        if (jsonIngredients != null) {
            JSONObject currentIngredient;
            double ingredientAmount;
            String ingredientUnit;
            String ingredientName;
            int numIngredients = jsonIngredients.length();
            for (int j = 0; j < numIngredients; j++) {
                try {
                    currentIngredient = (JSONObject) jsonIngredients.get(j);
                    ingredientAmount = currentIngredient.getDouble(RECIPE_L02_INGREDIENT_ONE_AMOUNT);
                    ingredientUnit = currentIngredient.getString(RECIPE_L02_INGREDIENT_ONE_UNIT);
                    ingredientName = currentIngredient.getString(RECIPE_L02_INGREDIENT_ONE_NAME);
                    ingredientName = ingredientName.replaceAll(
                            REGEX_FIND_TOO_CLOSE_COMMA,
                            REGEX_REPLACE_TOO_CLOSE_COMMA
                    );
                    ingredientName = ingredientName.replaceAll(
                            REGEX_FIND_TOO_CLOSE_PARENTHESES,
                            REGEX_REPLACE_TOO_CLOSE_PARENTHESES
                    );
                    Ingredient newIngredient = new Ingredient(
                            ingredientName,
                            (float) ingredientAmount,
                            ingredientUnit
                    );
                    recipeIngredients.add(newIngredient);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return recipeIngredients;
    }

    private static List<Direction> extractDirections(JSONArray jsonDirections) {
        List<Direction> recipeDirections = new ArrayList<>();
        if (jsonDirections != null) {
            JSONObject currentDirection;
            long directionOrder;
            String directionDescribeShort;
            String directionDescribeFull;
            String directionUrlVideo;
            String directionUrlThumb;
            int numIngredients = jsonDirections.length();
            for (int j = 0; j < numIngredients; j++) {
                try {
                    currentDirection = (JSONObject) jsonDirections.get(j);
                    directionOrder = currentDirection.getLong(RECIPE_L02_DIRECTION_ONE_ORDER);
                    directionDescribeShort = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_DESC_SHORT);
                    directionDescribeShort = directionDescribeShort.replaceFirst(
                            REGEX_FIND_ENDING_PERIOD,
                            REGEX_REPLACE_ENDING_PERIOD
                    );
                    directionDescribeFull = currentDirection.getString(RECIPE_L02_DIRECTION_ONE_DESC_FULL);
                    directionDescribeFull = directionDescribeFull.replaceFirst(
                            REGEX_FIND_DEGREE_TEMPERATURE,
                            REGEX_REPLACE_DEGREE_TEMPERATURE
                    );
                    directionDescribeFull = directionDescribeFull.replaceFirst(
                            REGEX_FIND_INITIAL_NUMBERING,
                            REGEX_REPLACE_INITIAL_NUMBERING
                    );
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return recipeDirections;
    }

    public static String ingredientList2jsonString(List<Ingredient> ingredients) {
        JSONArray jsonArray = new JSONArray();
        Ingredient currentIngredient;
        JSONObject currentJsonObject;
        int size = ingredients.size();
        for (int i = 0; i < size; i++) {
            currentIngredient = ingredients.get(i);
            try {
                currentJsonObject = new JSONObject();
                currentJsonObject.put(RECIPE_L02_INGREDIENT_ONE_NAME, currentIngredient.getIngredientName());
                currentJsonObject.put(RECIPE_L02_INGREDIENT_ONE_AMOUNT, currentIngredient.getAmount());
                currentJsonObject.put(RECIPE_L02_INGREDIENT_ONE_UNIT, currentIngredient.getUnit());
                jsonArray.put(currentJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static List<Ingredient> jsonString2ingredientList(String json) {
        List<Ingredient> ingredients = new ArrayList<>();
        try {
            JSONArray ingredientArray = new JSONArray(json);
            ingredients = extractIngredients(ingredientArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ingredients;
    }

    public static String directionList2jsonString(List<Direction> directions) {
        JSONArray jsonArray = new JSONArray();
        Direction currentDirection;
        JSONObject currentJsonObject;
        URL tempUrl;
        int size = directions.size();
        for (int i = 0; i < size; i++) {
            currentDirection = directions.get(i);
            try {
                currentJsonObject = new JSONObject();
                currentJsonObject.put(RECIPE_L02_DIRECTION_ONE_ORDER, currentDirection.getOrder());
                currentJsonObject.put(RECIPE_L02_DIRECTION_ONE_DESC_SHORT, currentDirection.getDescribeShort());
                currentJsonObject.put(RECIPE_L02_DIRECTION_ONE_DESC_FULL, currentDirection.getDescribeFull());
                tempUrl = currentDirection.getUrlVideo();
                currentJsonObject.put(RECIPE_L02_DIRECTION_ONE_URL_VIDEO, tempUrl == null ? "" : tempUrl.toString());
                tempUrl = currentDirection.getUrlThumb();
                currentJsonObject.put(RECIPE_L02_DIRECTION_ONE_URL_THUMB, tempUrl == null ? "" : tempUrl.toString());
                jsonArray.put(currentJsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonArray.toString();
    }

    public static List<Direction> jsonString2directionList(String json) {
        List<Direction> directions = new ArrayList<>();
        try {
            JSONArray directionArray = new JSONArray(json);
            directions = extractDirections(directionArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return directions;
    }
}

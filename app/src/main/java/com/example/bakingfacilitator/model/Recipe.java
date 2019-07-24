package com.example.bakingfacilitator.model;

import java.util.List;

public class Recipe {
    private long mOrder;
    private String mRecipeName;
    private List<Ingredient> mIngredients;
    private List<Direction> mDirections;
    private int mServings;
    private String mImage;

    public Recipe(
            long order,
            String name,
            List<Ingredient> ingredients,
            List<Direction> directions,
            int servings,
            String image
    ) {
        mOrder = order;
        mRecipeName = name;
        mIngredients = ingredients;
        mDirections = directions;
        mServings = servings;
        mImage = image;
    }

    public void setOrder(long mOrder) { this.mOrder = mOrder; }
    public long getOrder() { return mOrder; }

    public void setRecipeName(String mRecipeName) { this.mRecipeName = mRecipeName; }
    public String getRecipeName() { return mRecipeName; }

    public void setIngredients(List<Ingredient> mIngredients) { this.mIngredients = mIngredients; }
    public List<Ingredient> getIngredients() { return mIngredients; }

    public void setDirections(List<Direction> mDirections) { this.mDirections = mDirections; }
    public List<Direction> getDirections() { return mDirections; }

    public void setServings(int mServings) { this.mServings = mServings; }
    public int getServings() { return mServings; }

    public void setImage(String mImage) { this.mImage = mImage; }
    public String getImage() { return mImage; }
}

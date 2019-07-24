package com.example.bakingfacilitator.model;

public class Ingredient {
    private String mIngredientName;
    private float mAmount;
    private String mUnit;

    public Ingredient (
            String name,
            float amount,
            String unit
    ) {
        mIngredientName = name;
        mAmount = amount;
        mUnit = unit;
    }

    public String getIngredientName() { return mIngredientName; }
    public void setIngredientName(String mIngredientName) { this.mIngredientName = mIngredientName; }

    public float getAmount() { return mAmount; }
    public void setAmount(float mAmount) { this.mAmount = mAmount; }

    public String getUnit() { return mUnit; }
    public void setUnit(String mUnit) { this.mUnit = mUnit; }
}

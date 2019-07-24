package com.example.bakingfacilitator.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getIngredientName());
        dest.writeFloat(getAmount());
        dest.writeString(getUnit());
    }

    private Ingredient (Parcel parcel) {
        setIngredientName(parcel.readString());
        setAmount(parcel.readFloat());
        setUnit(parcel.readString());
    }

    public static final Parcelable.Creator<Ingredient> CREATOR = new Parcelable.Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel source) {
            return new Ingredient(source);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[0];
        }
    };
}

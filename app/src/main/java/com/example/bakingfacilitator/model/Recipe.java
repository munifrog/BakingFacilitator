package com.example.bakingfacilitator.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {
    @PrimaryKey
    @ColumnInfo(name = "id")
    private long mOrder;

    @ColumnInfo(name = "name")
    private String mRecipeName;

    @ColumnInfo(name = "ingredients")
    private List<Ingredient> mIngredients;

    @Ignore
    private boolean[] mChecked;

    @ColumnInfo(name = "directions")
    private List<Direction> mDirections;

    @ColumnInfo(name = "servings")
    private int mServings;

    @ColumnInfo(name = "image")
    private String mImage;

    public Recipe(
            long order,
            String recipeName,
            List<Ingredient> ingredients,
            List<Direction> directions,
            int servings,
            String image
    ) {
        this(
                order,
                recipeName,
                ingredients,
                new boolean[ingredients.size()],
                directions,
                servings,
                image
        );
    }

    @Ignore
    public Recipe(
            long order,
            String recipeName,
            List<Ingredient> ingredients,
            boolean[] checked,
            List<Direction> directions,
            int servings,
            String image
    ) {
        mOrder = order;
        mRecipeName = recipeName;
        mIngredients = ingredients;
        mChecked = checked;
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

    @Ignore public boolean[] getChecked() { return mChecked; }
    @Ignore public void setChecked(boolean[] mChecked) { this.mChecked = mChecked; }

    public boolean getChecked(int position) { return mChecked[position]; }
    public void setChecked(int position, boolean newState) { this.mChecked[position] = newState; }

    public void setDirections(List<Direction> mDirections) { this.mDirections = mDirections; }
    public List<Direction> getDirections() { return mDirections; }

    public void setServings(int mServings) { this.mServings = mServings; }
    public int getServings() { return mServings; }

    public void setImage(String mImage) { this.mImage = mImage; }
    public String getImage() { return mImage; }

    @Ignore
    @Override
    public int describeContents() {
        return 0;
    }

    @Ignore
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(getOrder());
        dest.writeString(getRecipeName());
        dest.writeList(getIngredients());
        dest.writeBooleanArray(getChecked());
        dest.writeList(getDirections());
        dest.writeInt(getServings());
        dest.writeString(getImage());
    }

    @Ignore
    private Recipe (Parcel parcel) {
        setOrder(parcel.readLong());
        setRecipeName(parcel.readString());
        // noinspection unchecked
        List<Ingredient> ingredients = parcel.readArrayList(Ingredient.class.getClassLoader());
        setIngredients(ingredients);
        boolean [] array = new boolean[ingredients != null ? ingredients.size() : 0];
        parcel.readBooleanArray(array);
        setChecked(array);
        // noinspection unchecked
        setDirections(parcel.readArrayList(Direction.class.getClassLoader()));
        setServings(parcel.readInt());
        setImage(parcel.readString());
    }

    @Ignore
    public static final Parcelable.Creator<Recipe> CREATOR = new Parcelable.Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel source) {
            return new Recipe(source);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[0];
        }
    };
}

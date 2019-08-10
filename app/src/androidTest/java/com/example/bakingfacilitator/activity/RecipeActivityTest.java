package com.example.bakingfacilitator.activity;

import android.content.Intent;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Recipe;
import com.example.bakingfacilitator.util.Json;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.example.bakingfacilitator.activity.RecipeActivity.PARCELABLE_RECIPE;

public class RecipeActivityTest {
    private static final String MOCK_RECIPE_TITLE = "Mock Recipe";
    private static final String MOCK_RECIPE_INGREDIENT_NAME_0 = "Mock Ingredient 1";
    private static final String MOCK_RECIPE_INGREDIENT_NAME_1 = "Mock Ingredient 2";
    private static final String MOCK_RECIPE_INGREDIENT_NAME_2 = "Mock Ingredient 3";
    private static final String MOCK_RECIPE_INGREDIENT_QUANTITY_0 = "2";
    private static final String MOCK_RECIPE_INGREDIENT_QUANTITY_1 = "6";
    private static final String MOCK_RECIPE_INGREDIENT_QUANTITY_2 = "4";
    private static final String MOCK_RECIPE_INGREDIENT_UNIT_0 = "CUP";
    private static final String MOCK_RECIPE_INGREDIENT_UNIT_1 = "TBLSP";
    private static final String MOCK_RECIPE_INGREDIENT_UNIT_2 = "OZ";
    private static final String MOCK_RECIPE_DIRECTION_SHORT_0 = "Step 1";
    private static final String MOCK_RECIPE_DIRECTION_SHORT_1 = "Step 2";
    private static final String MOCK_RECIPE_DIRECTION_SHORT_2 = "Last Step";
    private static final String MOCK_RECIPE_DIRECTION_LONG_0 = "Intro";
    private static final String MOCK_RECIPE_DIRECTION_LONG_1 =
            "1. Preheat the oven to 350\\u00b0F. Butter a 9\\\" deep dish pie pan.";
    private static final String MOCK_RECIPE_DIRECTION_LONG_2 =
            "2. Pour the filling into the prepared crust and smooth the top. " +
            "Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. " +
            "Then it's ready to serve!";
    private static final String MOCK_RECIPE_DIRECTION_VIDEO_0 =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4";
    private static final String MOCK_RECIPE_DIRECTION_VIDEO_1 = "";
    private static final String MOCK_RECIPE_DIRECTION_VIDEO_2 =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4";

    private static final String JSON_MOCK_RECIPE = "[\n" +
            "  {\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"" + MOCK_RECIPE_TITLE + "\",\n" +
            "    \"ingredients\": [\n" +
            "      {\n" +
            "        \"quantity\": \"" + MOCK_RECIPE_INGREDIENT_QUANTITY_0 + "\",\n" +
            "        \"measure\": \"" + MOCK_RECIPE_INGREDIENT_UNIT_0 + "\",\n" +
            "        \"ingredient\": \"" + MOCK_RECIPE_INGREDIENT_NAME_0 + "\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"quantity\": " + MOCK_RECIPE_INGREDIENT_QUANTITY_1 + ",\n" +
            "        \"measure\": \"" + MOCK_RECIPE_INGREDIENT_UNIT_1 + "\",\n" +
            "        \"ingredient\": \"" + MOCK_RECIPE_INGREDIENT_NAME_1 + "\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"quantity\": " + MOCK_RECIPE_INGREDIENT_QUANTITY_2 + ",\n" +
            "        \"measure\": \"" + MOCK_RECIPE_INGREDIENT_UNIT_2 + "\",\n" +
            "        \"ingredient\": \"" + MOCK_RECIPE_INGREDIENT_NAME_2 + "\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"steps\": [\n" +
            "      {\n" +
            "        \"id\": 0,\n" +
            "        \"shortDescription\": \"" + MOCK_RECIPE_DIRECTION_SHORT_0 + "\",\n" +
            "        \"description\": \"" + MOCK_RECIPE_DIRECTION_LONG_0 + "\",\n" +
            "        \"videoURL\": \"" + MOCK_RECIPE_DIRECTION_VIDEO_0 + "\",\n" +
            "        \"thumbnailURL\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 1,\n" +
            "        \"shortDescription\": \"" + MOCK_RECIPE_DIRECTION_SHORT_1 + "\",\n" +
            "        \"description\": \"" + MOCK_RECIPE_DIRECTION_LONG_1 + "\",\n" +
            "        \"videoURL\": \"" + MOCK_RECIPE_DIRECTION_VIDEO_1 + "\",\n" +
            "        \"thumbnailURL\": \"\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"id\": 6,\n" +
            "        \"shortDescription\": \"" + MOCK_RECIPE_DIRECTION_SHORT_2 + "\",\n" +
            "        \"description\": \"" + MOCK_RECIPE_DIRECTION_LONG_2 + "\",\n" +
            "        \"videoURL\": \"" + MOCK_RECIPE_DIRECTION_VIDEO_2 + "\",\n" +
            "        \"thumbnailURL\": \"\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"servings\": 8,\n" +
            "    \"image\": \"\"\n" +
            "  }\n" +
            "]";

    private static final int MOCK_EXPECTED_INGREDIENT_INDEX = 1;
    private static final float MOCK_EXPECTED_INGREDIENT_QUANTITY = Float.valueOf(MOCK_RECIPE_INGREDIENT_QUANTITY_1);
    private static final String MOCK_EXPECTED_INGREDIENT_UNIT = " tbsp";
    private static final String MOCK_EXPECTED_INGREDIENT_NAME = MOCK_RECIPE_INGREDIENT_NAME_1;

    private static final int MOCK_EXPECTED_DIRECTION_INDEX = 2;
    // Text without beginning number
    private static final String MOCK_EXPECTED_DIRECTION_TEXT = "Pour the filling into the prepared " +
            "crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the " +
            "pie for at least 2 hours. Then it's ready to serve!";

    // TODO: Run these tests on a tablet in the horizontal orientation

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityTestRule =
            new ActivityTestRule<>(RecipeActivity.class, false, false);

    @Before
    public void setUp() throws Exception {
        // https://stackoverflow.com/questions/31752303/espresso-startactivity-that-depends-on-intent
        List<Recipe> recipes = Json.extractRecipe(JSON_MOCK_RECIPE);
        Recipe recipe = recipes.get(0);
        Intent recipeIntent = new Intent();
        recipeIntent.putExtra(PARCELABLE_RECIPE, recipe);
        mActivityTestRule.launchActivity(recipeIntent);
    }

    // https://github.com/googlesamples/android-testing/blob/master/ui/espresso/RecyclerViewSample/app/src/androidTest/java/com/example/android/testing/espresso/RecyclerViewSample/RecyclerViewSampleTest.java
    @Test
    public void onIngredientClick() {
        onView(withId(R.id.rv_ingredients))
                // Wake the screen before running tests; otherwise the click will be absorbed
                .perform(RecyclerViewActions.actionOnItemAtPosition(MOCK_EXPECTED_INGREDIENT_INDEX, click()))
        ;
        String expectedText = mActivityTestRule.getActivity().getString(
                R.string.format_ingredient_unit,
                MOCK_EXPECTED_INGREDIENT_QUANTITY,
                MOCK_EXPECTED_INGREDIENT_UNIT,
                MOCK_EXPECTED_INGREDIENT_NAME
        );
        onView(withText(expectedText))
                .check(matches(isChecked()))
        ;
    }

    @Test
    public void onDirectionClick() {
        onView(withId(R.id.rv_directions))
                // Wake the screen before running tests; otherwise the click will be absorbed
                .perform(RecyclerViewActions.actionOnItemAtPosition(MOCK_EXPECTED_DIRECTION_INDEX, click()))
        ;
        onView(withText(MOCK_EXPECTED_DIRECTION_TEXT))
                .check(matches(isDisplayed()))
        ;
    }
}
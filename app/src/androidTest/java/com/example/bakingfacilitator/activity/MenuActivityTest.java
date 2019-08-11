package com.example.bakingfacilitator.activity;

import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.bakingfacilitator.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;

import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class MenuActivityTest {
    private static final String EXPECTED_LAUNCHED_ACTIVITY_NAME = "Nutella Pie";

    @Rule
    public ActivityTestRule<MenuActivity> mActivityTestRule =
            new ActivityTestRule<>(MenuActivity.class);

    @Test
    public void clickingMenuItemLaunchesCorrectRecipe() {
        // Wake screen before running tests; otherwise the click will be absorbed
        onView(withId(R.id.frame_menu))
                .perform(click())
        ;
        // This author suggests using RecyclerViewActions class from espresso-contrib:
        // https://spin.atomicobject.com/2016/04/15/espresso-testing-recyclerviews/
        onView(withId(R.id.rv_menu))
                .perform(RecyclerViewActions.actionOnItemAtPosition(2, click()))
        ;
        onView(withId(R.id.tv_recipe_name))
                .check(matches(withText(EXPECTED_LAUNCHED_ACTIVITY_NAME)))
        ;
    }
}
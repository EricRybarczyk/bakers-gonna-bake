package com.example.ericr.bakersgonnabake;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

public class RecipeListFragmentTest {
    // notes to self for resources that helped me learn how to interact with Fragments in Espresso:
    // https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
    // https://android.jlelse.eu/the-basics-of-android-espresso-testing-activities-fragments-7a8bfbc16dc5

    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    private static final int RECYCLERVIEW_TEST_ITEM_POSITION = 2;
    private IdlingResource idlingResource;

    @Before
    public void init(){
        // sources for this FragmentManager technique to initialize the Fragment from the Activity under test:
        // https://android.jlelse.eu/the-basics-of-android-espresso-testing-activities-fragments-7a8bfbc16dc5
        // https://pavneetsblog.wordpress.com/2017/10/13/testing-fragments-and-network-call-using-espresso/
        mainActivityTestRule.getActivity().getSupportFragmentManager().beginTransaction();

        // *******************************************************************
        // Non-plagiarism statement: the technique for SimpleIdlingResource
        // is directly taken from AOSP and Udacity materials
        // *******************************************************************
        RecipeListFragment recipeListFragment = (RecipeListFragment) mainActivityTestRule.getActivity().getSupportFragmentManager().findFragmentById(R.id.recipe_list_fragment);
        idlingResource = recipeListFragment.getIdlingResource();
        IdlingRegistry.getInstance().register(idlingResource);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void clickRecyclerViewItem_OpensRecipeStepsActivity() {

        // Arrange
        final ViewInteraction recyclerViewInteraction = onView(ViewMatchers.withId(R.id.rv_recipes));

        // Act
        recyclerViewInteraction.perform(RecyclerViewActions.actionOnItemAtPosition(RECYCLERVIEW_TEST_ITEM_POSITION, click()));

        // Assert
        onView(withId(R.id.recipe_step_list_fragment)).check(matches(isDisplayed()));
    }
}

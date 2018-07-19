package com.example.ericr.bakersgonnabake;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class RecipeListFragmentTest {
    // notes to self for resources that helped me learn how to interact with Fragments in Espresso:
    // https://developer.android.com/training/testing/espresso/lists#recycler-view-list-items
    // https://android.jlelse.eu/the-basics-of-android-espresso-testing-activities-fragments-7a8bfbc16dc5

    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);

    private static final int RECYCLERVIEW_TEST_ITEM_POSITION = 2;
    private boolean isTablet;
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

        String indicator = mainActivityTestRule.getActivity().getResources().getString(R.string.screen_type);
        isTablet = indicator.equals(RecipeAppConstants.SCREEN_TABLET);
    }

    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }

    @Test
    public void clickRecyclerViewItem_OpensRecipeStepsActivity() {

        // Most of this test code was generated using the Espresso Test Recorder.
        // I unified the code from separate tests on a phone and a tablet into a singe test.
        // This allows for the different layout on the different size devices.
        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.rv_recipes),
                        childAtPosition(
                                withId(R.id.recipe_list_fragment),
                                0)));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction recyclerView2;

        if (isTablet) {

            recyclerView2 = onView(
                    allOf(withId(R.id.recipe_steps_list),
                            childAtPosition(
                                    allOf(withId(R.id.recipe_list_fragment),
                                            childAtPosition(
                                                    IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                    0)),
                                    0),
                            isDisplayed()));

        } else {

            recyclerView2 = onView(
                    allOf(withId(R.id.recipe_steps_list),
                            childAtPosition(
                                    allOf(withId(R.id.recipe_step_list_fragment),
                                            childAtPosition(
                                                    withId(android.R.id.content),
                                                    0)),
                                    0),
                            isDisplayed()));

        }

        recyclerView2.check(matches(isDisplayed()));

    }

    // this method was generated by Android Studio with Espresso Test Recorder
    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}

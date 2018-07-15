package com.example.ericr.bakersgonnabake;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.runner.RunWith;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

@RunWith(AndroidJUnit4.class)
public class MainActivityBasicTest {

    @Rule public ActivityTestRule<MainActivity> mainActivityTestRule = new ActivityTestRule<>(MainActivity.class, true);


    @Test
    public void LaunchMainActivity_DisplaysRecyclerView() {
        // I think this test has little real value, but I am just starting to learn this testing framework

        // Arrange - find the view to test
        final ViewInteraction mainView = onView(ViewMatchers.withId(R.id.rv_recipes));

        // Act - perform the action on the view
        // No action performed in this test, other than loading the view

        // Assert - check if the view did what was expected
        mainView.check(matches(isDisplayed()));

    }



}

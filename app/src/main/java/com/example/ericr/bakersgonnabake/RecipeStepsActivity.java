package com.example.ericr.bakersgonnabake;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

public class RecipeStepsActivity extends AppCompatActivity
    implements RecipeStepListFragment.OnRecipeStepClickListener {

    private static final String TAG = RecipeStepsActivity.class.getSimpleName();
    private int recipeId;
    private boolean isTabletLayout;

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        if (getString(R.string.screen_type).equals(RecipeAppConstants.SCREEN_TABLET)) {
            isTabletLayout = true;
        }

        Intent starter = getIntent();

        if (starter.hasExtra(RecipeAppConstants.KEY_RECIPE_ID)) {
            recipeId = starter.getIntExtra(RecipeAppConstants.KEY_RECIPE_ID, RecipeAppConstants.ERROR_RECIPE_ID);
        } else {
            recipeId = RecipeAppConstants.ERROR_RECIPE_ID;
            Log.e(TAG, "Missing expected data: " + RecipeAppConstants.KEY_RECIPE_ID);
            return;
        }

        if (isTabletLayout && savedInstanceState == null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
            detailFragment.setArguments(getRecipeStepBundleArgs(RecipeAppConstants.INGREDIENT_STEP_INDICATOR));
            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_detail_container, detailFragment)
                    .commit();
        }
    }

    @NonNull
    private Bundle getRecipeStepBundleArgs(int recipeStepId) {
        Bundle args = new Bundle();
        args.putInt(RecipeAppConstants.KEY_RECIPE_ID, recipeId);
        args.putInt(RecipeAppConstants.KEY_STEP_ID, recipeStepId); // start with Ingredients step displayed
        return args;
    }

    @Override
    public void onRecipeStepClicked(int stepId) {
        if (isTabletLayout) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            RecipeStepDetailFragment detailFragment = new RecipeStepDetailFragment();
            detailFragment.setArguments(getRecipeStepBundleArgs(stepId));
            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_container, detailFragment)
                    .commit();
        } else {
            Class destination = RecipeStepDetail.class;
            Intent intentToStart = new Intent(this, destination);
            intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, recipeId);
            intentToStart.putExtra(RecipeAppConstants.KEY_STEP_ID, stepId);
            startActivity(intentToStart);
        }

    }
}

package com.example.ericr.bakersgonnabake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

public class RecipeStepDetail extends AppCompatActivity {


    private int recipeId;
    private int stepId;
    private static final String TAG = RecipeStepDetail.class.getSimpleName();

    public int getRecipeId() {
        return recipeId;
    }
    public int getStepId() {
        return stepId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        Intent starter = getIntent();

        if (starter.hasExtra(RecipeAppConstants.KEY_RECIPE_ID)) {
            recipeId = starter.getIntExtra(RecipeAppConstants.KEY_RECIPE_ID, RecipeAppConstants.ERROR_RECIPE_ID);
        } else {
            recipeId = RecipeAppConstants.ERROR_RECIPE_ID;
        }

        if (starter.hasExtra(RecipeAppConstants.KEY_STEP_ID)) {
            stepId = starter.getIntExtra(RecipeAppConstants.KEY_STEP_ID, RecipeAppConstants.ERROR_STEP_ID);
        } else {
            stepId = RecipeAppConstants.ERROR_STEP_ID;
            Log.e(TAG, "Missing expected data: " + RecipeAppConstants.KEY_STEP_ID);
        }

    }
}

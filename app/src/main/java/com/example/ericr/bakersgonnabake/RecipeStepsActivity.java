package com.example.ericr.bakersgonnabake;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

public class RecipeStepsActivity extends AppCompatActivity
    implements RecipeStepListFragment.OnRecipeStepClickListener{

    private static final String TAG = RecipeStepsActivity.class.getSimpleName();
    private int recipeId;

    public int getRecipeId() {
        return recipeId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        Intent starter = getIntent();

        if (starter.hasExtra(RecipeAppConstants.KEY_RECIPE_ID)) {
            recipeId = starter.getIntExtra(RecipeAppConstants.KEY_RECIPE_ID, RecipeAppConstants.ERROR_RECIPE_ID);
        } else {
            recipeId = RecipeAppConstants.ERROR_RECIPE_ID;
            Log.e(TAG, "Missing expected data: " + RecipeAppConstants.KEY_RECIPE_ID);
        }
    }

    @Override
    public void onRecipeStepClicked(int stepId) {
        Toast.makeText(this, "You have been called", Toast.LENGTH_LONG).show();
    }
}

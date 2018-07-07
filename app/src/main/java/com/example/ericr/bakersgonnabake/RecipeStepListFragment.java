package com.example.ericr.bakersgonnabake;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.service.RecipeService;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListFragment extends Fragment {

    private static final String TAG = RecipeStepListFragment.class.getSimpleName();
    OnRecipeStepClickListener recipeStepClickListener;
    @BindView(R.id.recipe_steps_list) protected RecyclerView recipeStepsListRecyclerView;
    private Recipe activeRecipe;

    // interface for callback to host activity when item is clicked
    public interface OnRecipeStepClickListener {
        void onRecipeStepClicked(int stepId);
    }

    // required default constructor
    public RecipeStepListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // make sure the host activity has implemented the callback interface
        try {
            recipeStepClickListener = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // wire up things to RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL ,false);
        recipeStepsListRecyclerView.setLayoutManager(layoutManager);
        recipeStepsListRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        int recipeId = RecipeAppConstants.ERROR_RECIPE_ID;
        RecipeStepsActivity parentActivity = (RecipeStepsActivity) getActivity();
        if (parentActivity != null) {
            recipeId = parentActivity.getRecipeId();
        }
        activeRecipe = getRecipe(recipeId);
        RecipeStepAdapter adapter = new RecipeStepAdapter(getActivity(), activeRecipe);
        recipeStepsListRecyclerView.setAdapter(adapter);

    }

    private Recipe getRecipe(int recipeId) {
        List<Recipe> recipeList = null;
        Type resultType = new TypeToken<List<Recipe>>() {}.getType();
        try {
            recipeList = Reservoir.get(RecipeService.RECIPE_LIST_CACHE_KEY, resultType);
        } catch (IOException e) {
            Log.e(TAG, "Reservoir exception" + e.getMessage());
        }
        if (recipeList != null) {
            for (Recipe recipe : recipeList) {
                if (recipe.getId() == recipeId) {
                    return recipe;
                }
            }
        }
        return null; // TODO - implement some error condition & display
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO - implement saved instance state
    }
}

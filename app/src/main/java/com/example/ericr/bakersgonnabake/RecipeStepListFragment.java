package com.example.ericr.bakersgonnabake;

import android.content.Context;
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

import com.example.ericr.bakersgonnabake.data.RecipeDataStore;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListFragment extends Fragment
    implements RecipeDataStore.RecipeListLoaderCallbacks {

    private static final String TAG = RecipeStepListFragment.class.getSimpleName();
    OnRecipeStepClickListener recipeStepClickListener;
    @BindView(R.id.recipe_steps_list) protected RecyclerView recipeStepsListRecyclerView;
    private int activeRecipeId;

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

        activeRecipeId = RecipeAppConstants.ERROR_RECIPE_ID;
        RecipeStepsActivity parentActivity = (RecipeStepsActivity) getActivity();
        if (parentActivity != null) {
            activeRecipeId = parentActivity.getRecipeId();
        }

        (new RecipeDataStore(getActivity())).loadRecipeList(this);

    }

    @Override
    public void onLoadFinished(List<Recipe> recipeList) {
        if (recipeList != null) {
            for (Recipe recipe : recipeList) {
                if (recipe.getId() == activeRecipeId) {
                    RecipeStepAdapter adapter = new RecipeStepAdapter(getActivity(), recipe, recipeStepClickListener);
                    recipeStepsListRecyclerView.setAdapter(adapter);
                }
            }
        }
    }

    @Override
    public void onLoadError(String errorMessage) {
        Log.e(TAG, errorMessage);
        // TODO - implement some error condition & display
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO - implement saved instance state
    }
}

package com.example.ericr.bakersgonnabake;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ericr.bakersgonnabake.IdlingResource.SimpleIdlingResource;
import com.example.ericr.bakersgonnabake.data.RecipeDataStore;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeListFragment extends Fragment
        implements RecipeDataStore.RecipeListLoaderCallbacks, RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;
    private boolean isTabletLayout;
    private static final String TAG = RecipeListFragment.class.getSimpleName();

    // *******************************************************************
    // Non-plagiarism statement: the technique for SimpleIdlingResource
    // is directly taken from AOSP and Udacity materials
    // *******************************************************************
    @Nullable private SimpleIdlingResource idlingResource;

    @VisibleForTesting
    @NonNull
    public SimpleIdlingResource getIdlingResource() {
        if (idlingResource == null) {
            idlingResource = new SimpleIdlingResource();
        }
        return idlingResource;
    }

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, rootView);

        if (getString(R.string.screen_type).equals(RecipeAppConstants.SCREEN_TABLET)) {
            isTabletLayout = true;
        }

        // wire up things to RecyclerView
        RecyclerView.LayoutManager layoutManager;
        if (isTabletLayout) {
            layoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL ,false);
        }
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);

        (new RecipeDataStore(getActivity())).loadRecipeList(this, idlingResource);

        return rootView;
    }

    @Override
    public void onLoadFinished(List<Recipe> recipeList) {
        RecipeAdapter recipeAdapter = new RecipeAdapter(recipeList, RecipeListFragment.this);
        recipeRecyclerView.setAdapter(recipeAdapter);
    }

    @Override
    public void onLoadError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }

    // Note to self: I think this is typically handled in the Activity, not the Fragment, but
    // in this case it will work since all devices will have this fragment alone in MainActivity.
    @Override
    public void onClick(int recipeId) {
        Class destination = RecipeStepsActivity.class;
        Intent intentToStart = new Intent(getActivity(), destination);
        intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, recipeId);
        startActivity(intentToStart);
    }


}

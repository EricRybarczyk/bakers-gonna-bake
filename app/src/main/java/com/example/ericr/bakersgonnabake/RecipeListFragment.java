package com.example.ericr.bakersgonnabake;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class RecipeListFragment extends Fragment
        implements RecipeDataStore.RecipeListLoaderCallbacks, RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;
    private static final String TAG = RecipeListFragment.class.getSimpleName();

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, rootView);

        // wire up things to RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL ,false);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);

        (new RecipeDataStore(getActivity())).loadRecipeList(this);

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

    // TODO - review this click handler location, perhaps move up to Activity.
    // Note to self: I think this is typically handled in the Activity, not the Fragment
    // but in this case I think it will work ok since all devices will have this fragment alone
    // in the MainActivity. However, I might move it to have better/typical code organization.
    @Override
    public void onClick(int recipeId) {
        Class destination = RecipeStepsActivity.class;
        Intent intentToStart = new Intent(getActivity(), destination);
        intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, recipeId);
        startActivity(intentToStart);
    }

}

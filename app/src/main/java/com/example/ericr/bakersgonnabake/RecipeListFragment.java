package com.example.ericr.bakersgonnabake;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.service.RecipeService;
import com.example.ericr.bakersgonnabake.service.UdacityBakingEndpoint;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment
        implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;
    private static final String TAG = RecipeListFragment.class.getSimpleName();
    private static final int CACHE_SIZE_BYTES = 1024 * 1000;

    public RecipeListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container, false);

        ButterKnife.bind(this, rootView);
        this.initializeCache();

        // wire up things to RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL ,false);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);

        loadRecipeData();

        return rootView;
    }

    private void initializeCache() {
        try {
            Reservoir.init(getActivity(), CACHE_SIZE_BYTES);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize cache: " + e.getMessage());
        }
    }

    private void loadRecipeData() {
        // TODO - figure out how to establish cache age and clear if too old
        // check the cache first
        try {
            if (Reservoir.contains(RecipeService.RECIPE_LIST_CACHE_KEY)) {
                Type resultType = new TypeToken<List<Recipe>>() {}.getType();
                List<Recipe> cachedRecipes = Reservoir.get(RecipeService.RECIPE_LIST_CACHE_KEY, resultType);
                RecipeAdapter recipeAdapter = new RecipeAdapter(cachedRecipes, RecipeListFragment.this::onClick);
                recipeRecyclerView.setAdapter(recipeAdapter);
                Log.i(TAG, "cachedRecipes+: " + cachedRecipes.size() + " recipes returned!");
            }
        } catch (IOException e) {
            Log.e(TAG, "Reservoir cache check exception" + e.getMessage());
        }

        // get the data from the service via retrofit
        UdacityBakingEndpoint endpoint = RecipeService.getClient().create(UdacityBakingEndpoint.class);
        Call<List<Recipe>> recipeCall = endpoint.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> resultRecipes = response.body();
                cacheRecipeData(resultRecipes);
                RecipeAdapter recipeAdapter = new RecipeAdapter(resultRecipes, RecipeListFragment.this::onClick);
                recipeRecyclerView.setAdapter(recipeAdapter);
                Log.i(TAG, "resultRecipes from Retrofit: " + resultRecipes.size() + " recipes returned!");
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getActivity(), "Retrofit onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }


    private void cacheRecipeData(List<Recipe> resultRecipes) {
        try {
            if (!Reservoir.contains(RecipeService.RECIPE_LIST_CACHE_KEY)) {
                Reservoir.putAsync(RecipeService.RECIPE_LIST_CACHE_KEY, resultRecipes, new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Reservoir success: " + resultRecipes.size() + " data is cached");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Reservoir exception" + e.getMessage());
                    }
                });
            }
        } catch (IOException e) {
            Log.e(TAG, "Reservoir cache check exception" + e.getMessage());
        }
    }

    @Override
    public void onClick(int recipeId) {
        // check the cache
        List<Recipe> cachedRecipes = null;
        Type resultType = new TypeToken<List<Recipe>>() {}.getType();
        try {
            cachedRecipes = Reservoir.get(RecipeService.RECIPE_LIST_CACHE_KEY, resultType);
        } catch (IOException e) {
            Log.i(TAG, "Reservoir exception" + e.getMessage());
        }
        if (cachedRecipes != null) {
            Toast.makeText(getActivity(), "Cached Recipes Found!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "You clicked a Recipe!", Toast.LENGTH_LONG).show();
        }
    }
}

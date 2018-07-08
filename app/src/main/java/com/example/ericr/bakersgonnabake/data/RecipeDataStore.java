package com.example.ericr.bakersgonnabake.data;

import android.content.Context;
import android.util.Log;

import com.anupcowkur.reservoir.Reservoir;
import com.anupcowkur.reservoir.ReservoirPutCallback;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.service.RecipeService;
import com.example.ericr.bakersgonnabake.service.UdacityBakingEndpoint;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This class encapsulates all data access for the app, including:
 *      * Calling service to fetch data via async
 *      * Cache of recipe data
 *      * Removal of cache data when it gets too old
 */
public class RecipeDataStore {

    private static final String TAG = RecipeDataStore.class.getSimpleName();
    private static final int CACHE_SIZE_BYTES = 1024 * 1000;
    private static final long CACHE_MAX_AGE_MILLISECONDS = 1000 * 60 * 10; // 10 minutes
    private static final String RECIPE_LIST_CACHE_KEY = "recipe_list_cache_key";
    private static final String RECIPE_CACHE_AGE_KEY = "recipe_cache_age_key";

    public interface RecipeListLoaderCallbacks {
        void onLoadFinished(List<Recipe> recipeList);
        void onLoadError(String errorMessage);
    }

    public interface RecipeLoaderCallbacks {
        void onLoadFinished(Recipe recipe);
        void onLoadError(String errorMessage);
    }


    public RecipeDataStore(Context context) {
        initializeCache(context);
    }

    private void initializeCache(Context context) {
        try {
            Reservoir.init(context, CACHE_SIZE_BYTES);
        } catch (IOException e) {
            Log.e(TAG, "Failed to initialize cache: " + e.getMessage());
        }
        // clear the cache if it is too old
        try {
            if (Reservoir.contains(RECIPE_CACHE_AGE_KEY)) {
                Type type = new TypeToken<Date>() {
                }.getType();
                Date cacheWriteDate = Reservoir.get(RECIPE_CACHE_AGE_KEY, type);
                Date now = new Date();
                if (now.getTime() - cacheWriteDate.getTime() > CACHE_MAX_AGE_MILLISECONDS) {
                    Reservoir.clear();
                }
            } else { // missing cache date indicates inconsistent data state, best to clear
                Reservoir.clear();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to clear cache: " + e.getMessage());
        }
    }

    public void loadRecipeList(final RecipeListLoaderCallbacks callbacks) {
        List<Recipe> result;
        // check the cache first
        try {
            if (Reservoir.contains(RECIPE_LIST_CACHE_KEY)) {
                Type resultType = new TypeToken<List<Recipe>>() {}.getType();
                result = Reservoir.get(RECIPE_LIST_CACHE_KEY, resultType);
                Log.i(TAG, "Recipes returned from cache");
                callbacks.onLoadFinished(result);
                return;
            }
        } catch (IOException e) {
            callbacks.onLoadError("Reservoir cache exception" + e.getMessage());
        }

        // if no cache, get the data from the service and cache it
        UdacityBakingEndpoint endpoint = RecipeService.getClient().create(UdacityBakingEndpoint.class);
        Call<List<Recipe>> recipeCall = endpoint.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> resultRecipes = response.body();
                cacheRecipeData(resultRecipes);
                Log.i(TAG, "Network data load complete");
                callbacks.onLoadFinished(resultRecipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                callbacks.onLoadError(t.getMessage());
            }
        });
    }

    private void cacheRecipeData(List<Recipe> resultRecipes) {
        try {
            if (!Reservoir.contains(RECIPE_LIST_CACHE_KEY)) {
                // first the cache date
                Reservoir.putAsync(RECIPE_CACHE_AGE_KEY, new Date(), new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Reservoir cache date is cached");
                    }
                    @Override
                    public void onFailure(Exception e) {
                        Log.e(TAG, "Reservoir exception" + e.getMessage());
                    }
                });
                // then the recipe data
                Reservoir.putAsync(RECIPE_LIST_CACHE_KEY, resultRecipes, new ReservoirPutCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "Reservoir data is cached");
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
}

package com.example.ericr.bakersgonnabake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import com.example.ericr.bakersgonnabake.model.Recipe;

import java.util.List;

// TODO - verify: don't think I need this when using Retrofit since it handles loading on background thread

public class RecipeListAsyncTaskLoader extends AsyncTaskLoader<List<Recipe>> {

    public RecipeListAsyncTaskLoader(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        //super.onStartLoading();
        // see if cached & return it, else forceLoad()
    }

    @Nullable
    @Override
    public List<Recipe> loadInBackground() {
        return null;
    }

    @Override
    public void deliverResult(@Nullable List<Recipe> data) {
        // cache data
        super.deliverResult(data);
    }
}

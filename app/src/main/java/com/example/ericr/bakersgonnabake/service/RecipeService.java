package com.example.ericr.bakersgonnabake.service;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RecipeService {

    public static final String RECIPE_LIST_CACHE_KEY = "recipe_list_cache_key";

    private static final String RECIPE_SOURCE_URL = "https://go.udacity.com/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(RECIPE_SOURCE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

}

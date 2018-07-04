package com.example.ericr.bakersgonnabake.service;

import com.example.ericr.bakersgonnabake.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UdacityBakingEndpoint {

    @GET("android-baking-app-json")
    Call<List<Recipe>> getRecipes();
}

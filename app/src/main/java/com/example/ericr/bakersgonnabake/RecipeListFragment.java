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

import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.service.RecipeService;
import com.example.ericr.bakersgonnabake.service.UdacityBakingEndpoint;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListFragment extends Fragment
        implements RecipeAdapter.RecipeAdapterOnClickHandler {

    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;
    private static final String TAG = MainActivity.class.getSimpleName();

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

        // get the data from the service via retrofit
        UdacityBakingEndpoint endpoint = RecipeService.getClient().create(UdacityBakingEndpoint.class);
        Call<List<Recipe>> recipeCall = endpoint.getRecipes();

        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> resultRecipes = response.body();
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

        return rootView;
    }

    @Override
    public void onClick(int movieId) {
        Toast.makeText(getActivity(), "You clicked a Recipe!", Toast.LENGTH_LONG).show();
    }
}

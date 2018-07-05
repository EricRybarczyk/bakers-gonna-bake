package com.example.ericr.bakersgonnabake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class MainActivity extends AppCompatActivity
        implements RecipeAdapter.RecipeAdapterOnClickHandler {

    private static final String TAG = MainActivity.class.getSimpleName();
    private boolean isTwoPaneLayout;
    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // TODO - change this to check for a particular view when the tablet layout is set up
        isTwoPaneLayout = false;

//        // wire up things to RecyclerView
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false);
//        recipeRecyclerView.setLayoutManager(layoutManager);
//        recipeRecyclerView.setHasFixedSize(true);
//
//        // get the data from the service via retrofit
//        UdacityBakingEndpoint endpoint = RecipeService.getClient().create(UdacityBakingEndpoint.class);
//        Call<List<Recipe>> recipeCall = endpoint.getRecipes();
//
//        recipeCall.enqueue(new Callback<List<Recipe>>() {
//            @Override
//            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
//                List<Recipe> resultRecipes = response.body();
//                RecipeAdapter recipeAdapter = new RecipeAdapter(resultRecipes, MainActivity.this::onClick);
//                recipeRecyclerView.setAdapter(recipeAdapter);
//                Log.i(TAG, "resultRecipes from Retrofit: " + resultRecipes.size() + " recipes returned!");
//            }
//
//            @Override
//            public void onFailure(Call<List<Recipe>> call, Throwable t) {
//                Log.e(TAG, t.getMessage());
//                Toast.makeText(getBaseContext(), "Retrofit onFailure", Toast.LENGTH_LONG).show();
//            }
//        });
    }

    @Override
    public void onClick(int movieId) {
        Toast.makeText(this, "You clicked a Recipe!", Toast.LENGTH_LONG).show();
    }
}

package com.example.ericr.bakersgonnabake;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeAdapterOnClickHandler {


    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // wire up things to RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setHasFixedSize(true);
        RecipeAdapter recipeAdapter = new RecipeAdapter();
        recipeRecyclerView.setAdapter(recipeAdapter);

        // get the data from the service via retrofit
        UdacityBakingEndpoint endpoint = RecipeService.getClient().create(UdacityBakingEndpoint.class);
        Call<List<Recipe>> recipeCall = endpoint.getRecipes();
        recipeCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> resultRecipes = response.body();
                Log.i(TAG, "resultRecipes from Retrofit: " + resultRecipes.size() + " recipes returned!");
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e(TAG, t.getMessage());
                Toast.makeText(getBaseContext(), "Retrofit onFailure", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onClick(int movieId) {

    }
}

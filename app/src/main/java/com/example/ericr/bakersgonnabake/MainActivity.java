package com.example.ericr.bakersgonnabake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
        //implements RecipeAdapter.RecipeAdapterOnClickHandler

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.rv_recipes) protected RecyclerView recipeRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

}

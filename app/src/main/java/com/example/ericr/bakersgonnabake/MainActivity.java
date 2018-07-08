package com.example.ericr.bakersgonnabake;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
        //implements RecipeAdapter.RecipeAdapterOnClickHandler

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

    }



//    @Override
//    public void onClick(int movieId) {
//        Toast.makeText(this, "You clicked a Recipe!", Toast.LENGTH_LONG).show();
//    }
}

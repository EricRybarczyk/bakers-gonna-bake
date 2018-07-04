package com.example.ericr.bakersgonnabake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericr.bakersgonnabake.model.Recipe;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    public interface RecipeAdapterOnClickHandler {
        void onClick(int recipeId);
    }

    private List<Recipe> recipeList;
    private RecipeAdapterOnClickHandler recipeItemClickHandler;

//    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler) {
//        this.recipeItemClickHandler = clickHandler;
//    }

    public RecipeAdapter(List<Recipe> recipeList, RecipeAdapterOnClickHandler clickHandler) {
        this.recipeList = recipeList;
        this.recipeItemClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_list_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        Recipe recipe = recipeList.get(position);
        holder.recipeNameText.setText(recipe.getName());
    }

    @Override
    public int getItemCount() {
        if (recipeList == null) {
            return 0;
        }
        return recipeList.size();
    }

    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_name_text) protected TextView recipeNameText;

        public RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        @OnClick
        public void onClick(View v) {;
            int recipeId = recipeList.get(getAdapterPosition()).getId();
            recipeItemClickHandler.onClick(recipeId);
        }
    }

}


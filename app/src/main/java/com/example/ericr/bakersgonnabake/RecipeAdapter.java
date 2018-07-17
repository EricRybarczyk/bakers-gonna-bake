package com.example.ericr.bakersgonnabake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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


    RecipeAdapter(List<Recipe> recipeList, RecipeAdapterOnClickHandler clickHandler) {
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
        // Real app would require a better approach than this switch
        switch (position) {
            case 0:
                holder.recipeImage.setImageResource(R.drawable.recipe1_1024x576);
                break;
            case 1:
                holder.recipeImage.setImageResource(R.drawable.recipe2_1024x576);
                break;
            case 2:
                holder.recipeImage.setImageResource(R.drawable.recipe3_1024x576);
                break;
            case 3:
                holder.recipeImage.setImageResource(R.drawable.recipe4_1024x576);
                break;
            default:
                holder.recipeImage.setImageResource(R.drawable.baking_clipart);
                break;
        }
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
        @BindView(R.id.recipe_image) protected ImageView recipeImage;


        RecipeHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        @OnClick
        public void onClick(View v) {
            int recipeId = recipeList.get(getAdapterPosition()).getId();
            recipeItemClickHandler.onClick(recipeId);
        }
    }

}


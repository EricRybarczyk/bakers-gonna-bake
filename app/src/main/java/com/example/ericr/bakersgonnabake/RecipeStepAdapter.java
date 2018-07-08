package com.example.ericr.bakersgonnabake;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepHolder> {

    private List<RecipeStepDisplayItem> stepDisplayItems;
    private RecipeStepListFragment.OnRecipeStepClickListener recipeStepClickListener;

    RecipeStepAdapter(Context context, Recipe recipe, RecipeStepListFragment.OnRecipeStepClickListener recipeStepClickListener) {
        stepDisplayItems = new ArrayList<>();
        this.recipeStepClickListener = recipeStepClickListener;
        prepareDisplayData(context, recipe);
    }

    private void prepareDisplayData(Context context, Recipe recipe) {
        // first item will be for ingredient list
        stepDisplayItems.add(new RecipeStepDisplayItem(RecipeAppConstants.INGREDIENT_STEP_INDICATOR, context.getString(R.string.recipe_step_ingredients_text)));
        // add each step
        for (int i = 0; i < recipe.getSteps().size(); i++) {
            stepDisplayItems.add(new RecipeStepDisplayItem(i, recipe.getSteps().get(i).getShortDescription()));
        }
    }

    @NonNull
    @Override
    public RecipeStepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_step_item, parent, false);
        return new RecipeStepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepHolder holder, int position) {
        RecipeStepDisplayItem item = stepDisplayItems.get(position);
        holder.recipeStepText.setText(item.getStepText());
    }

    @Override
    public int getItemCount() {
        if (stepDisplayItems == null) {
            return 0;
        }
        return stepDisplayItems.size();
    }

    public class RecipeStepHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.recipe_step_text) protected TextView recipeStepText;

        RecipeStepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        @OnClick
        public void onClick(View v) {
            int stepId = stepDisplayItems.get(getAdapterPosition()).getStepId();
            recipeStepClickListener.onRecipeStepClicked(stepId);
        }
    }

    private class RecipeStepDisplayItem {
        private static final int MAX_DISPLAY_LENGTH = 30;
        private static final String REMOVE_END_CHAR = ".";
        private int stepId;
        private String stepText;

        RecipeStepDisplayItem(int stepId, String stepText) {
            this.stepId = stepId;
            this.stepText = scrubDisplayText(stepText);
        }

        // tidy it up for better display
        private String scrubDisplayText(String text) {
            if (text.endsWith(REMOVE_END_CHAR)) {
                text = text.substring(0, (text.length() - 1));
            }
            if (text.length() > MAX_DISPLAY_LENGTH) {
                text = text.substring(0, MAX_DISPLAY_LENGTH) + "...";
            }
            return text;
        }

        int getStepId() {
            return stepId;
        }

        String getStepText() {
            return stepText;
        }
    }


}

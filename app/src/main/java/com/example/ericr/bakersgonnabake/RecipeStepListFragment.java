package com.example.ericr.bakersgonnabake;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepListFragment extends Fragment {

    OnRecipeStepClickListener clickListener;
    @BindView(R.id.recipe_steps_list) protected ListView recipeStepsList;

    // interface for callback to host activity when item is clicked
    public interface OnRecipeStepClickListener {
        void onRecipeStepClicked(int stepId);
    }

    // required default constructor
    public RecipeStepListFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // make sure the host activity has implemented the callback interface
        try {
            clickListener = (OnRecipeStepClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecipeStepClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);

        // TODO - figure out how the recipe data will be persisted.


        return super.onCreateView(inflater, container, savedInstanceState);
    }
}

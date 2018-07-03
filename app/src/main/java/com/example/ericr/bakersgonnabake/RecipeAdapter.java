package com.example.ericr.bakersgonnabake;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    public interface RecipeAdapterOnClickHandler {
        void onClick(int movieId);
    }

    public RecipeAdapter() {
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }



    public class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RecipeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }

}


package com.example.ericr.bakersgonnabake;

import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ericr.bakersgonnabake.data.RecipeDataStore;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.model.Step;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepDetailFragment extends Fragment implements RecipeDataStore.RecipeListLoaderCallbacks {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private int recipeId;
    private int stepId;
    private SimpleExoPlayer exoPlayer;
    @BindView(R.id.exo_player) protected SimpleExoPlayerView playerView;

    public RecipeStepDetailFragment() {
        //activeStepId = RecipeAppConstants.ERROR_STEP_ID;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RecipeStepDetail parentActivity = (RecipeStepDetail) getActivity();
        recipeId = parentActivity.getRecipeId();
        stepId = parentActivity.getStepId();
    }

    @Override
    public void onStart() {
        super.onStart();
        new RecipeDataStore(getActivity()).loadRecipeList(this);
    }

    @Override
    public void onStop() {
        releasePlayer();
        super.onStop();
    }

    @Override
    public void onLoadFinished(List<Recipe> recipeList) {
        Recipe activeRecipe = null;

        for (Recipe recipe : recipeList) {
            if (recipe.getId() == recipeId) {
                activeRecipe = recipe;
                break;
            }
        }

        if (activeRecipe != null) {

            if (stepId == RecipeAppConstants.INGREDIENT_STEP_INDICATOR) {
                // hide player because ingredient step has no video
                playerView.setVisibility(View.GONE);

                // TODO - set up the ingredients display only

            } else {
                // get step media info
                Step activeStep = activeRecipe.getStep(stepId);
                initializePlayer(Uri.parse(activeStep.getVideoURL()));
            }
        }
    }

    @Override
    public void onLoadError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }


    private void initializePlayer(Uri videoUri) {
        if (exoPlayer == null) {
            // prepare the ExoPlayer instance
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector, loadControl);
            playerView.setPlayer(exoPlayer);
            // prepare the MediaSource
            Context context = getActivity();
            String userAgent = Util.getUserAgent(context, context.getResources().getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(
                    videoUri,
                    new DefaultDataSourceFactory(context, userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null
            );
            exoPlayer.prepare(mediaSource);
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }
}

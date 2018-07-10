package com.example.ericr.bakersgonnabake;

import android.app.Fragment;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.ericr.bakersgonnabake.data.RecipeDataStore;
import com.example.ericr.bakersgonnabake.model.Ingredient;
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
    @BindView(R.id.step_description) protected TextView stepDescription;
    @BindView(R.id.frame_ingredients_header) protected FrameLayout ingredientsHeaderSection;
    @BindView(R.id.ingredients_label) protected TextView ingredientsLabel;

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
                ingredientsLabel.setText(getActivity().getResources().getString(R.string.recipe_step_ingredients_text));

                // build HTML list for better display
                StringBuilder sb = new StringBuilder();
                for (Ingredient ingredient : activeRecipe.getIngredients()) {
                    sb.append("&#8226; ");
                    sb.append(ingredient.getQuantity());
                    sb.append(" ");
                    sb.append(ingredient.getMeasurement());
                    sb.append(" ");
                    sb.append(ingredient.getIngredientName());
                    sb.append("<br>");
                }
                //stepDescription.setText(sb.toString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    stepDescription.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY));
                } else {
                    stepDescription.setText(Html.fromHtml(sb.toString()));
                }

            } else {
                ingredientsHeaderSection.setVisibility(View.GONE);
                // get step media info
                Step activeStep = activeRecipe.getStep(stepId);
                stepDescription.setText(activeStep.getDescription());

                playerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.baking_clipart));
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
            if (videoUri.toString().isEmpty()) {
                playerView.setUseController(false);
            } else {
                String userAgent = Util.getUserAgent(context, context.getResources().getString(R.string.app_name));
                MediaSource mediaSource = new ExtractorMediaSource(
                        videoUri,
                        new DefaultDataSourceFactory(context, userAgent),
                        new DefaultExtractorsFactory(),
                        null,
                        null
                );
                exoPlayer.prepare(mediaSource);
            }
            exoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}

package com.example.ericr.bakersgonnabake;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
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
import java.util.NoSuchElementException;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepDetailFragment extends Fragment implements RecipeDataStore.RecipeListLoaderCallbacks, View.OnClickListener {

    private static final String TAG = RecipeStepDetailFragment.class.getSimpleName();
    private boolean isTabletLayout;
    private int activeRecipeId;
    private Recipe activeRecipe;
    private int activeStepId;
    private SimpleExoPlayer exoPlayer;
    @BindView(R.id.exo_player) protected SimpleExoPlayerView playerView;
    @BindView(R.id.step_description) protected TextView stepDescription;
    @BindView(R.id.ingredients_label) protected TextView ingredientsLabel;
    @BindView(R.id.parent_constraint_layout) protected ConstraintLayout constraintLayout;
    @BindView(R.id.step_description_container) protected ScrollView descriptionScrollView;
    @BindView(R.id.button_nav_back) protected Button navBackButton;
    @BindView(R.id.button_nav_forward) protected Button navForwardButton;

    public RecipeStepDetailFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        ButterKnife.bind(this, rootView);
        navBackButton.setOnClickListener(this);
        navForwardButton.setOnClickListener(this);

        if (getString(R.string.screen_type).equals(RecipeAppConstants.SCREEN_TABLET)) {
            isTabletLayout = true;
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isTabletLayout) {
            activeRecipeId = getArguments().getInt(RecipeAppConstants.KEY_RECIPE_ID, RecipeAppConstants.ERROR_RECIPE_ID);
            activeStepId = getArguments().getInt(RecipeAppConstants.KEY_STEP_ID, RecipeAppConstants.ERROR_STEP_ID);
        } else {
            RecipeStepDetail parentActivity = (RecipeStepDetail) getActivity();
            activeRecipeId = parentActivity.getRecipeId();
            activeStepId = parentActivity.getStepId();

        }
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
        activeRecipe = null;
        for (Recipe recipe : recipeList) {
            if (recipe.getId() == activeRecipeId) {
                activeRecipe = recipe;
                break;
            }
        }

        if (activeRecipe != null) {

            StringBuilder sb = new StringBuilder(); // this will be filled with html string

            if (activeStepId == RecipeAppConstants.INGREDIENT_STEP_INDICATOR) {
                // hide player because ingredient step has no video
                playerView.setVisibility(View.GONE);
                ingredientsLabel.setVisibility(View.VISIBLE);
                ingredientsLabel.setText(getActivity().getResources().getString(R.string.recipe_step_ingredients_text));

                // adjust constraints because player view is now gone and other views constrain to it in XML
                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(
                        descriptionScrollView.getId(),
                        ConstraintSet.TOP,
                        ingredientsLabel.getId(),
                        ConstraintSet.BOTTOM
                        );
                constraintSet.connect(
                        ingredientsLabel.getId(),
                        ConstraintSet.TOP,
                        constraintLayout.getId(),
                        ConstraintSet.TOP
                );
                constraintSet.applyTo(constraintLayout);

                // build HTML list for better display
                for (Ingredient ingredient : activeRecipe.getIngredients()) {
                    sb.append("&#8226; ");
                    sb.append(ingredient.getQuantity());
                    sb.append(" ");
                    sb.append(ingredient.getMeasurement());
                    sb.append(" ");
                    sb.append(ingredient.getIngredientName());
                    sb.append("<br>");
                }

            } else {
                ingredientsLabel.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);

                ConstraintSet constraintSet = new ConstraintSet();
                constraintSet.clone(constraintLayout);
                constraintSet.connect(
                        ingredientsLabel.getId(),
                        ConstraintSet.TOP,
                        playerView.getId(),
                        ConstraintSet.BOTTOM
                );
                constraintSet.applyTo(constraintLayout);

                // get step media info
                Step activeStep = activeRecipe.getStep(activeStepId);

                // html paragraphs for better display
                if (!activeStep.getShortDescription().isEmpty()
                        && !activeStep.getShortDescription().equalsIgnoreCase(activeStep.getDescription())) {
                    sb.append("<p>");
                    sb.append(activeStep.getShortDescription());
                    sb.append("</p>");
                }
                sb.append("<p>");
                sb.append(activeStep.getDescription());
                sb.append("</p>");

                playerView.setDefaultArtwork(BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.baking_clipart));
                initializePlayer(Uri.parse(activeStep.getVideoURL()));
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                stepDescription.setText(Html.fromHtml(sb.toString(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                stepDescription.setText(Html.fromHtml(sb.toString()));
            }

            if (isTabletLayout) { // tablet does not need the nav buttons due to two-pane layout
                navForwardButton.setVisibility(View.GONE);
                navBackButton.setVisibility(View.GONE);
            } else {
                // hide forward button when this is the last step
                if (activeStepId == activeRecipe.getSteps().get(activeRecipe.getSteps().size() - 1).getId()) {
                    navForwardButton.setVisibility(View.GONE);
                }
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

    @Override
    public void onClick(View v) {
        Button button = (Button) v;
        Class destination;
        Intent intentToStart;
        // eval current position in step list and then launch intent to appropriate next/previous step
        switch (button.getId()) {
            case R.id.button_nav_forward:
                destination = RecipeStepDetail.class;
                intentToStart = new Intent(getActivity(), destination);
                intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, activeRecipeId);
                intentToStart.putExtra(RecipeAppConstants.KEY_STEP_ID, getNextStepId(activeStepId));
                startActivity(intentToStart);
                break;
            case R.id.button_nav_back:
                if (activeStepId == RecipeAppConstants.INGREDIENT_STEP_INDICATOR) {
                    // go to step list since Ingredients step is shown as first step
                    destination = RecipeStepsActivity.class;
                    intentToStart = new Intent(getActivity(), destination);
                    intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, activeRecipeId);
                    startActivity(intentToStart);
                } else {
                    // go to previous step in current recipe, special handling when on first step so we can go to Ingredients as step
                    int navigateBackToStepId = getPreviousStepId(activeStepId);
                    if (activeStepId == activeRecipe.getSteps().get(0).getId()) {
                        navigateBackToStepId = RecipeAppConstants.INGREDIENT_STEP_INDICATOR;
                    }
                    destination = RecipeStepDetail.class;
                    intentToStart = new Intent(getActivity(), destination);
                    intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, activeRecipeId);
                    intentToStart.putExtra(RecipeAppConstants.KEY_STEP_ID, navigateBackToStepId);
                    startActivity(intentToStart);
                }
                break;
            default:
                throw new NoSuchElementException("No handler defined for button " + button.getId());
        }
    }

    // this feels clunky, I probably need a better data structure to support back/forward list traversal in a more natural way
    // or maybe I need to rework how I'm navigating and make this another RecyclerView maybe with swipe navigation
    private int getNextStepId(int currentStepId) {
        Step result = activeRecipe.getSteps().get(0);
        int i = 0;
        for (Step s: activeRecipe.getSteps()) {
            i++; // this now indexes the next item (may not exist)
            if (s.getId() == currentStepId) {
                if (i < this.activeRecipe.getSteps().size()) {
                    result = activeRecipe.getSteps().get(i);
                }
                break;
            }
        }
        return result.getId(); // this is either the next step or is circles back to the first
    }

    private int getPreviousStepId(int currentStepId) {
        Step currentStep = activeRecipe.getStep(currentStepId);
        int currentIndex = activeRecipe.getSteps().indexOf(currentStep);
        if (currentIndex <= 0) { // indicates not found or currentStepId is index 0
            return currentStepId;
        }
        return activeRecipe.getSteps().get(currentIndex - 1).getId();
    }
}

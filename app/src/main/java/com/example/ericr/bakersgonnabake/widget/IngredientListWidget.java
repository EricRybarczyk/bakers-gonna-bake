package com.example.ericr.bakersgonnabake.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.example.ericr.bakersgonnabake.R;
import com.example.ericr.bakersgonnabake.RecipeStepDetail;
import com.example.ericr.bakersgonnabake.util.RecipeAppConstants;


/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link IngredientListWidgetConfigureActivity IngredientListWidgetConfigureActivity}
 */
public class IngredientListWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        CharSequence widgetText = IngredientListWidgetConfigureActivity.getContentPref(context, appWidgetId);
        int activeRecipeId = IngredientListWidgetConfigureActivity.getRecipeIdPref(context, appWidgetId);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredient_list_widget);
        views.setTextViewText(R.id.appwidget_text, widgetText);

        if (activeRecipeId != RecipeAppConstants.ERROR_RECIPE_ID) {
            // set up the PendingIntent for a click
            Intent intentToStart= new Intent(context, RecipeStepDetail.class);
            intentToStart.putExtra(RecipeAppConstants.KEY_RECIPE_ID, activeRecipeId);
            intentToStart.putExtra(RecipeAppConstants.KEY_STEP_ID, RecipeAppConstants.INGREDIENT_STEP_INDICATOR);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToStart, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.appwidget_text, pendingIntent);
        }


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            IngredientListWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}


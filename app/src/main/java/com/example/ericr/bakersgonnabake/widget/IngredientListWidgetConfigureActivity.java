package com.example.ericr.bakersgonnabake.widget;

import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ericr.bakersgonnabake.R;
import com.example.ericr.bakersgonnabake.data.RecipeDataStore;
import com.example.ericr.bakersgonnabake.model.Ingredient;
import com.example.ericr.bakersgonnabake.model.Recipe;
import com.example.ericr.bakersgonnabake.util.SimpleHtmlUtility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * The configuration screen for the {@link IngredientListWidget IngredientListWidget} AppWidget.
 */
public class IngredientListWidgetConfigureActivity extends Activity implements RecipeDataStore.RecipeListLoaderCallbacks {

    private static final String PREFS_NAME = "com.example.ericr.bakersgonnabake.widget.IngredientListWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String TAG = IngredientListWidgetConfigureActivity.class.getSimpleName();
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    private List<Recipe> recipeList;
    @BindView(R.id.appwidget_recipe_list) protected ListView recipeListView;

    ListView.OnItemClickListener mOnClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final Context context = IngredientListWidgetConfigureActivity.this;

            TextView clickedView = (TextView) view;
            String selectedText = clickedView.getText().toString();
            StringBuilder widgetText = new StringBuilder();

            // When the view is clicked, store the ingredients string locally
            for (Recipe r : recipeList) {
                if (r.getName().equalsIgnoreCase(selectedText)) {
                    for (Ingredient ingredient : r.getIngredients()) {
                        widgetText.append("* ");
                        widgetText.append(ingredient.getIngredientName());
                        widgetText.append("\n");
                    }
                }
            }

            saveTitlePref(context, mAppWidgetId, widgetText.toString());

            // It is the responsibility of the configuration activity to update the app widget
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            IngredientListWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    public IngredientListWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        setContentView(R.layout.ingredient_list_widget_configure);

        ButterKnife.bind(this);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        (new RecipeDataStore(this)).loadRecipeList(this);

    }

    @Override
    public void onLoadFinished(List<Recipe> recipeList) {
        this.recipeList = recipeList;
        List<String> recipeNames = new ArrayList<>();
        for (Recipe r: recipeList) {
            recipeNames.add(r.getName());
        }
        ArrayAdapter<String> itemsAdapter = new ArrayAdapter<String>(this, R.layout.widget_config_list_item, recipeNames);
        recipeListView.setAdapter(itemsAdapter);
        recipeListView.setOnItemClickListener(mOnClickListener);
    }

    @Override
    public void onLoadError(String errorMessage) {
        Log.e(TAG, errorMessage);
    }
}


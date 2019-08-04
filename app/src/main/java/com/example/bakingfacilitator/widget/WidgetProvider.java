package com.example.bakingfacilitator.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Ingredient;

import java.util.List;

import static com.example.bakingfacilitator.activity.MenuActivity.PARCELABLE_INGREDIENT_LIST;

public class WidgetProvider extends AppWidgetProvider {
    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int numWidgets = appWidgetIds.length;
        for (int i = 0; i < numWidgets; i++) {
            int appWidgetId = appWidgetIds[i];

            Intent serviceIntent = new Intent(context, WidgetService.class);

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients);
            views.setRemoteAdapter(R.id.lv_ingredients, serviceIntent);
            views.setEmptyView(R.id.lv_ingredients, R.id.tv_empty_view);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent != null && intent.hasExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS) &&
                intent.hasExtra(PARCELABLE_INGREDIENT_LIST)) {
            List<Ingredient> ingredients = intent.getParcelableArrayListExtra(PARCELABLE_INGREDIENT_LIST);
            WidgetFactory.refreshData(ingredients);

            int [] widgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);
            onUpdate(context, AppWidgetManager.getInstance(context), widgetIds);
        }
    }
}

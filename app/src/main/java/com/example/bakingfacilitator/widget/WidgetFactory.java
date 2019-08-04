package com.example.bakingfacilitator.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Ingredient;

import java.util.List;

import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_CUP;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_ENTIRE;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_GRAM;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_GRAM_KILO;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_OUNCE;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_TABLE_SPOON;
import static com.example.bakingfacilitator.adapt.LinearIngredientAdapter.JSON_TEA_SPOON;

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {
    private static List<Ingredient> mIngredients;

    private Context mContext;

    WidgetFactory(Context context, Intent intent) { mContext = context; }

    static void refreshData(List<Ingredient> ingredients) {
        if (ingredients != null) {
            mIngredients = ingredients;
        }
    }

    @Override
    public void onCreate() {}

    @Override
    public void onDataSetChanged() {}

    @Override
    public void onDestroy() {}

    @Override
    public int getCount() {
        return mIngredients == null ? 0 : mIngredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.item_widget);
        Ingredient ingredient = mIngredients.get(position);

        float amount = ingredient.getAmount();
        int stringId = R.string.format_widget_ingredient_unit;
        if (amount % 1.0 != 0) {
            stringId = R.string.format_widget_ingredient_portion;
        }

        String unit = ingredient.getUnit();
        boolean bAddSpace = true;
        if (JSON_ENTIRE.equals(unit)) {
            unit = mContext.getString(R.string.conversion_whole_entire);
            bAddSpace = false;
        } else if (JSON_GRAM_KILO.equals(unit)) {
            unit = mContext.getString(R.string.conversion_gram_kilo);
        } else if (JSON_GRAM.equals(unit)) {
            unit = mContext.getString(R.string.conversion_gram);
        } else if (JSON_CUP.equals(unit)) {
            unit = mContext.getString(R.string.conversion_cup);
        } else if (JSON_OUNCE.equals(unit)) {
            unit = mContext.getString(R.string.conversion_ounce);
        } else if (JSON_TABLE_SPOON.equals(unit)) {
            unit = mContext.getString(R.string.conversion_tablespoon);
        } else if (JSON_TEA_SPOON.equals(unit)) {
            unit = mContext.getString(R.string.conversion_teaspoon);
        }
        if (bAddSpace) {
            unit = " " + unit;
        }

        views.setTextViewText(
                R.id.tv_ingredient,
                mContext.getString(
                        stringId,
                        amount,
                        unit,
                        ingredient.getIngredientName()
                )
        );
        return views;
    }

    @Override
    public RemoteViews getLoadingView() { return null; }

    @Override
    public int getViewTypeCount() { return 1; }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public boolean hasStableIds() { return true; }
}

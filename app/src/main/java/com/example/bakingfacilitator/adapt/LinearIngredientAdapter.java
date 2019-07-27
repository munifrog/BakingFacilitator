package com.example.bakingfacilitator.adapt;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Ingredient;

import java.util.List;

public class LinearIngredientAdapter extends RecyclerView.Adapter<LinearIngredientAdapter.ViewHolder> {
    private static final String JSON_CUP                     = "CUP";
    private static final String JSON_TABLE_SPOON             = "TBLSP";
    private static final String JSON_TEA_SPOON               = "TSP";
    private static final String JSON_GRAM_KILO               = "K";
    private static final String JSON_GRAM                    = "G";
    private static final String JSON_OUNCE                   = "OZ";
    private static final String JSON_ENTIRE                  = "UNIT";

    private Listener mListener;
    private List<Ingredient> mIngredients;
    private boolean [] mChecks;

    public LinearIngredientAdapter(List<Ingredient> ingredients, boolean [] checks, Listener listener) {
        mIngredients = ingredients;
        mChecks = checks;
        mListener = listener;
    }

    public interface Listener {
        void onIngredientClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.item_ingredient;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutId, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        if (mIngredients == null) {
            return 0;
        }
        return mIngredients.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View mView;
        private Resources mResources;
        private CheckBox mCheckBox;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mView = view;
            mResources = mView.getResources();
        }

        void bind(int position) {
            Ingredient ingredient = mIngredients.get(position);
            mCheckBox = mView.findViewById(R.id.cb_ingredient);
            mCheckBox.setChecked(mChecks[position]);
            mCheckBox.setOnClickListener(this);

            float amount = ingredient.getAmount();
            int resId = R.string.format_ingredient_unit;
            // See https://stackoverflow.com/questions/703396/how-to-nicely-format-floating-numbers-to-string-without-unnecessary-decimal-0
            if (amount % 1.0 != 0) {
                resId = R.string.format_ingredient_portion;
            }

            String unit = ingredient.getUnit();
            if (JSON_ENTIRE.equals(unit)) {
                unit = mResources.getString(R.string.conversion_whole_entire);
            } else if (JSON_GRAM_KILO.equals(unit)) {
                unit = mResources.getString(R.string.conversion_gram_kilo);
            } else if (JSON_GRAM.equals(unit)) {
                unit = mResources.getString(R.string.conversion_gram);
            } else if (JSON_CUP.equals(unit)) {
                unit = mResources.getString(R.string.conversion_cup);
            } else if (JSON_OUNCE.equals(unit)) {
                unit = mResources.getString(R.string.conversion_ounce);
            } else if (JSON_TABLE_SPOON.equals(unit)) {
                unit = mResources.getString(R.string.conversion_tablespoon);
            } else if (JSON_TEA_SPOON.equals(unit)) {
                unit = mResources.getString(R.string.conversion_teaspoon);
            }

            mCheckBox.setText(mView.getResources().getString(
                    resId,
                    amount,
                    unit,
                    ingredient.getIngredientName()
            ));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mChecks[position] = !mChecks[position];
            mListener.onIngredientClick(position);
        }
    }
}

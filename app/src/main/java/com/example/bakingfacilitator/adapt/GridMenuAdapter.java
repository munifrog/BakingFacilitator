package com.example.bakingfacilitator.adapt;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Recipe;

import java.util.List;

public class GridMenuAdapter extends RecyclerView.Adapter<GridMenuAdapter.ViewHolder> {
    private Listener mListener;
    private List<Recipe> mRecipes;

    public GridMenuAdapter(Listener listener) {
        mListener = listener;
    }

    public interface Listener {
        void onClick(int position);
    }

    public void setRecipes(List<Recipe> mRecipes) {
        this.mRecipes = mRecipes;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.item_menu;
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
        if (mRecipes == null) {
            return 0;
        } else {
            return mRecipes.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        TextView tvName;
        TextView tvServings;
        TextView tvComplexity;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mView = view;
            tvName = view.findViewById(R.id.tv_menu_name);
            tvServings = view.findViewById(R.id.tv_menu_servings);
            tvComplexity = view.findViewById(R.id.tv_menu_complexity);
        }

        void bind(int position) {
            if (mRecipes != null) {
                Recipe recipe = mRecipes.get(position);
                tvName.setText(recipe.getRecipeName());
                tvServings.setText(
                        mView.getResources().getString(
                                R.string.format_servings,
                                recipe.getServings()
                        )
                );
                tvComplexity.setText(
                        mView.getResources().getString(
                                R.string.format_complexity,
                                recipe.getDirections().size()
                        )
                );
            }
        }

        @Override
        public void onClick(View v) {
            mListener.onClick(getAdapterPosition());
        }
    }
}

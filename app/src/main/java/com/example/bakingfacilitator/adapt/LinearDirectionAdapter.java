package com.example.bakingfacilitator.adapt;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;

import java.util.List;

public class LinearDirectionAdapter extends RecyclerView.Adapter<LinearDirectionAdapter.ViewHolder> {
    private List<Direction> mDirections;
    private Listener mListener;

    public LinearDirectionAdapter(List<Direction> directions, Listener listener) {
        mDirections = directions;
        mListener = listener;
    }

    public interface Listener {
        void onDirectionClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutId = R.layout.item_direction;
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
        if (mDirections == null) {
            return 0;
        }
        return mDirections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        View mView;
        Resources mResources;

        ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mView = view;
            mResources = view.getResources();
        }

        void bind(int position) {
            Direction direction = mDirections.get(position);

            TextView tvOrder = mView.findViewById(R.id.tv_direction_order);
            tvOrder.setText(mResources.getString(R.string.format_direction_order, position));

            TextView tvDescription = mView.findViewById(R.id.tv_direction_describe);
            tvDescription.setText(direction.getDescribeShort());
        }

        @Override
        public void onClick(View v) {
            mListener.onDirectionClick(getAdapterPosition());
        }
    }
}

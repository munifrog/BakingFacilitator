package com.example.bakingfacilitator.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bakingfacilitator.R;
import com.example.bakingfacilitator.model.Direction;

public class ViewerActivity extends AppCompatActivity {
    private static final String TAG = ViewerActivity.class.getSimpleName();

    public static final String PARCELABLE_DIRECTION = "one_direction";

    private Direction mDirection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer);

        Intent intent = getIntent();
        Log.v(TAG, "intent: \"" + intent + "\"");

        if (intent == null || !intent.hasExtra(PARCELABLE_DIRECTION)) {
            finish();
        } else {
            mDirection = intent.getParcelableExtra(PARCELABLE_DIRECTION);

            TextView tvDirection = findViewById(R.id.tv_description);
            tvDirection.setText(mDirection.getDescribeFull());
        }
    }

}

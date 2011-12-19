package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class ShapeFilterActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(R.string.none_filter);
        setContentView(view);
    }
}

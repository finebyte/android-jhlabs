package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import java.util.ArrayList;
import java.util.List;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.OilFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class OilFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Oil";
        
    private static final String LEVEL_STRING = "LEVEL:";
    private static final String RANGE_STRING = "RANGE:";
    private static final int MAX_VALUE = 10;
    
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    private static final int RANGE_SEEKBAR_RESID = 21866;
    
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private SeekBar mRangeSeekBar;
    private TextView mRangeTextView;
    
    private int mLevelValue;
    private int mRangeValue;
    
    private ProgressDialog mProgressDialog;
    private int[] mColors;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterSeekBarSetup(mMainLayout);
    }
    /**
     * filterButtonSetting
     * @param mainLayout
     */
    private void filterSeekBarSetup(LinearLayout mainLayout){
        mLevelTextView = new TextView(this);
        mLevelTextView.setText(LEVEL_STRING+mLevelValue);
        mLevelTextView.setTextSize(TITLE_TEXT_SIZE);
        mLevelTextView.setTextColor(Color.BLACK);
        mLevelTextView.setGravity(Gravity.CENTER);
        
        mLevelSeekBar = new SeekBar(this);
        mLevelSeekBar.setOnSeekBarChangeListener(this);
        mLevelSeekBar.setId(LEVEL_SEEKBAR_RESID);
        mLevelSeekBar.setMax(MAX_VALUE);
        
        mRangeTextView = new TextView(this);
        mRangeTextView.setText(RANGE_STRING+mRangeValue);
        mRangeTextView.setTextSize(TITLE_TEXT_SIZE);
        mRangeTextView.setTextColor(Color.BLACK);
        mRangeTextView.setGravity(Gravity.CENTER);
        
        mRangeSeekBar = new SeekBar(this);
        mRangeSeekBar.setOnSeekBarChangeListener(this);
        mRangeSeekBar.setId(RANGE_SEEKBAR_RESID);
        mRangeSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mLevelTextView);
        mainLayout.addView(mLevelSeekBar);
        mainLayout.addView(mRangeTextView);
        mainLayout.addView(mRangeSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case LEVEL_SEEKBAR_RESID:
            mLevelValue = progress;
            mLevelTextView.setText(LEVEL_STRING+mLevelValue);
            break;
        case RANGE_SEEKBAR_RESID:
            mRangeValue = progress;
            mRangeTextView.setText(RANGE_STRING+mRangeValue);
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = mOriginalImageView.getDrawable().getIntrinsicHeight();
        
        mColors = AndroidUtils.drawableToIntArray(mOriginalImageView.getDrawable());
        mProgressDialog = ProgressDialog.show(this, "", "Wait......");
        
        Thread thread = new Thread(){
            public void run() {
                OilFilter filter = new OilFilter();
                filter.setLevels(mLevelValue);
                filter.setRange(mRangeValue);
                mColors = filter.filter(mColors, width, height);
                OilFilterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setModifyView(mColors, width, height);
                    }
                });
                mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}

package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.StampFilter;
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

public class StampFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Stamp";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String THRESHOLD_STRING = "THRESHOLD:";
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String LOW_COLOR_STRING = "LOW_COLOR:";
    private static final String UPPER_COLOR_STRING = "UPPER_COLOR:";
    private static final int MAX_VALUE = 100;
    private static final int COLOR_MAX_VALUE = 0xFFFFFFFF - 0xFF000000;
    
    private static final int RADIUS_SEEKBAR_RESID = 21863;
    private static final int THRESHOLD_SEEKBAR_RESID = 21864;
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final int LOW_COLOR_SEEKBAR_RESID = 21866;
    private static final int UPPER_COLOR_SEEKBAR_RESID = 21867;
    
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mThresholdSeekBar;
    private TextView mThresholdTextView;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private SeekBar mLowColorSeekBar;
    private TextView mLowColorTextView;
    private SeekBar mUpperColorSeekBar;
    private TextView mUpperColorTextView;
    
    private int mRadiusValue;
    private int mThresholdValue;
    private int mSoftnessValue;
    private int mLowColorValue;
    private int mUpperColorValue;
    
    private ProgressDialog mProgressDialog;
    private int[] mColors;
    
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
        mRadiusTextView = new TextView(this);
        mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
        mRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mRadiusTextView.setTextColor(Color.BLACK);
        mRadiusTextView.setGravity(Gravity.CENTER);
        
        mRadiusSeekBar = new SeekBar(this);
        mRadiusSeekBar.setOnSeekBarChangeListener(this);
        mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        mRadiusSeekBar.setMax(MAX_VALUE);
        
        mThresholdTextView = new TextView(this);
        mThresholdTextView.setText(THRESHOLD_STRING+mThresholdValue);
        mThresholdTextView.setTextSize(TITLE_TEXT_SIZE);
        mThresholdTextView.setTextColor(Color.BLACK);
        mThresholdTextView.setGravity(Gravity.CENTER);
        
        mThresholdSeekBar = new SeekBar(this);
        mThresholdSeekBar.setOnSeekBarChangeListener(this);
        mThresholdSeekBar.setId(THRESHOLD_SEEKBAR_RESID);
        mThresholdSeekBar.setMax(MAX_VALUE);
        
        mSoftnessTextView = new TextView(this);
        mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
        mSoftnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mSoftnessTextView.setTextColor(Color.BLACK);
        mSoftnessTextView.setGravity(Gravity.CENTER);
        
        mSoftnessSeekBar = new SeekBar(this);
        mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        mSoftnessSeekBar.setMax(MAX_VALUE);
        
        mLowColorTextView = new TextView(this);
        mLowColorTextView.setText(LOW_COLOR_STRING+mLowColorValue);
        mLowColorTextView.setTextSize(TITLE_TEXT_SIZE);
        mLowColorTextView.setTextColor(Color.BLACK);
        mLowColorTextView.setGravity(Gravity.CENTER);
        
        mLowColorSeekBar = new SeekBar(this);
        mLowColorSeekBar.setOnSeekBarChangeListener(this);
        mLowColorSeekBar.setId(LOW_COLOR_SEEKBAR_RESID);
        mLowColorSeekBar.setMax(COLOR_MAX_VALUE);
        
        mUpperColorTextView = new TextView(this);
        mUpperColorTextView.setText(UPPER_COLOR_STRING+mUpperColorValue);
        mUpperColorTextView.setTextSize(TITLE_TEXT_SIZE);
        mUpperColorTextView.setTextColor(Color.BLACK);
        mUpperColorTextView.setGravity(Gravity.CENTER);
        
        mUpperColorSeekBar = new SeekBar(this);
        mUpperColorSeekBar.setOnSeekBarChangeListener(this);
        mUpperColorSeekBar.setId(UPPER_COLOR_SEEKBAR_RESID);
        mUpperColorSeekBar.setMax(COLOR_MAX_VALUE);
        mUpperColorSeekBar.setProgress(COLOR_MAX_VALUE);
        
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mThresholdTextView);
        mainLayout.addView(mThresholdSeekBar);
        mainLayout.addView(mSoftnessTextView);
        mainLayout.addView(mSoftnessSeekBar);
        mainLayout.addView(mLowColorTextView);
        mainLayout.addView(mLowColorSeekBar);
        mainLayout.addView(mUpperColorTextView);
        mainLayout.addView(mUpperColorSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case RADIUS_SEEKBAR_RESID:
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
            break;
        case THRESHOLD_SEEKBAR_RESID:
            mThresholdValue = progress;
            mThresholdTextView.setText(THRESHOLD_STRING+getValue(mThresholdValue));
            break;
        case SOFTNESS_SEEKBAR_RESID:
            mSoftnessValue = progress;
            mSoftnessTextView.setText(SOFTNESS_STRING+getValue(mSoftnessValue));
            break;
        case LOW_COLOR_SEEKBAR_RESID:
            mLowColorValue = progress;
            mLowColorTextView.setText(LOW_COLOR_STRING+getColorValue(mLowColorValue));
            break;
        case UPPER_COLOR_SEEKBAR_RESID:
            mUpperColorValue = progress;
            mUpperColorTextView.setText(UPPER_COLOR_STRING+getColorValue(mUpperColorValue));
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
                StampFilter filter = new StampFilter();
                filter.setRadius(mRadiusValue);
                filter.setThreshold(getValue(mThresholdValue));
                filter.setSoftness(getValue(mSoftnessValue));
                filter.setBlack(getColorValue(mLowColorValue));
                filter.setWhite(getColorValue(mUpperColorValue));
                mColors = filter.filter(mColors, width, height);

                StampFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getValue(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
    private int getColorValue(int value){
        int retValue = 0;
        retValue = value + 0xFF000000;
        return retValue;
    }
}

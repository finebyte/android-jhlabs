package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.effectsactivity.NoiseFilterActivity;
import com.jabistudio.androidjhlabs.filter.DissolveFilter;
import com.jabistudio.androidjhlabs.filter.NoiseFilter;
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

public class DissolveFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Dissolve";
    private static final String DENSITY_STRING = "DENSITY:";
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final int MAX_VALUE = 102;
    
    private static final int DENSITY_SEEKBAR_RESID = 21864;
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    
    private SeekBar mDensitySeekBar;
    private TextView mDensityTextView;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    
    private int mDensityValue;
    private int mSoftnessValue;
    
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
        mDensityTextView = new TextView(this);
        mDensityTextView.setText(DENSITY_STRING+mDensityValue);
        mDensityTextView.setTextSize(TITLE_TEXT_SIZE);
        mDensityTextView.setTextColor(Color.BLACK);
        mDensityTextView.setGravity(Gravity.CENTER);
        
        mDensitySeekBar = new SeekBar(this);
        mDensitySeekBar.setOnSeekBarChangeListener(this);
        mDensitySeekBar.setId(DENSITY_SEEKBAR_RESID);
        mDensitySeekBar.setMax(MAX_VALUE);
        
        mSoftnessTextView = new TextView(this);
        mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
        mSoftnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mSoftnessTextView.setTextColor(Color.BLACK);
        mSoftnessTextView.setGravity(Gravity.CENTER);
        
        mSoftnessSeekBar = new SeekBar(this);
        mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        mSoftnessSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mDensityTextView);
        mainLayout.addView(mDensitySeekBar);
        mainLayout.addView(mSoftnessTextView);
        mainLayout.addView(mSoftnessSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case DENSITY_SEEKBAR_RESID:
            mDensityValue = progress;
            mDensityTextView.setText(DENSITY_STRING+getValue(mDensityValue));
            break;
        case SOFTNESS_SEEKBAR_RESID:
            mSoftnessValue = progress;
            mSoftnessTextView.setText(SOFTNESS_STRING+getValue(mSoftnessValue));
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
                DissolveFilter filter = new DissolveFilter();
                filter.setDensity(getValue(mDensityValue));
                filter.setSoftness(getValue(mSoftnessValue));
                mColors = filter.filter(mColors, width, height);

                DissolveFilterActivity.this.runOnUiThread(new Runnable() {
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
}

package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.DespeckleFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.SmartBlurFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class SmartBlurFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "SmartBlur";
    
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String THRESHOLD_STRING = "THRESHOLD:";
    private static final int MAX_VALUE = 100;
    
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final int THRESHOLD_SEEKBAR_RESID = 21866;
    
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mThresHoldSeekBar;
    private TextView mThresHoldTextView;
    
    private int mRadiusValue;
    private int mThresHoldValue;
    
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
        
        mThresHoldTextView = new TextView(this);
        mThresHoldTextView.setText(THRESHOLD_STRING+mThresHoldValue);
        mThresHoldTextView.setTextSize(TITLE_TEXT_SIZE);
        mThresHoldTextView.setTextColor(Color.BLACK);
        mThresHoldTextView.setGravity(Gravity.CENTER);
        
        mThresHoldSeekBar = new SeekBar(this);
        mThresHoldSeekBar.setOnSeekBarChangeListener(this);
        mThresHoldSeekBar.setId(THRESHOLD_SEEKBAR_RESID);
        mThresHoldSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mThresHoldTextView);
        mainLayout.addView(mThresHoldSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case RADIUS_SEEKBAR_RESID:
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
            break;
        case THRESHOLD_SEEKBAR_RESID:
            mThresHoldValue = progress;
            mThresHoldTextView.setText(THRESHOLD_STRING+mThresHoldValue);
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
                SmartBlurFilter filter = new SmartBlurFilter();
                filter.setRadius(mRadiusValue);
                filter.setThreshold(mThresHoldValue);
                mColors = filter.filter(mColors, width, height);
                
                SmartBlurFilterActivity.this.runOnUiThread(new Runnable() {
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

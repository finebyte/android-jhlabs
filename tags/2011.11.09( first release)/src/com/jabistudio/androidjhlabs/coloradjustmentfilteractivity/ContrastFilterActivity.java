package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.ContrastFilter;
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

public class ContrastFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Contrast";
    private static final String BRIGHTNESS_STRING = "BRIGHTNESS:";
    private static final String CONTRAST_STRING = "CONTRAST:";
    private static final int MAX_VALUE = 200;
    
    private static final int BRIGHTNESS_SEEKBAR_RESID = 21863;
    private static final int CONTRAST_SEEKBAR_RESID = 21864;
    
    private SeekBar mBrightnessSeekBar;
    private TextView mBrightnessTextView;
    private SeekBar mContrastSeekBar;
    private TextView mContrastTextView;
    
    private int mBrightnessValue;
    private int mContrastValue;
    
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
        mBrightnessTextView = new TextView(this);
        mBrightnessTextView.setText(BRIGHTNESS_STRING+getValue(mBrightnessValue));
        mBrightnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mBrightnessTextView.setTextColor(Color.BLACK);
        mBrightnessTextView.setGravity(Gravity.CENTER);
        
        mBrightnessSeekBar = new SeekBar(this);
        mBrightnessSeekBar.setOnSeekBarChangeListener(this);
        mBrightnessSeekBar.setId(BRIGHTNESS_SEEKBAR_RESID);
        mBrightnessSeekBar.setMax(MAX_VALUE);
        mBrightnessSeekBar.setProgress(MAX_VALUE/2);
        
        mContrastTextView = new TextView(this);
        mContrastTextView.setText(CONTRAST_STRING+getValue(mContrastValue));
        mContrastTextView.setTextSize(TITLE_TEXT_SIZE);
        mContrastTextView.setTextColor(Color.BLACK);
        mContrastTextView.setGravity(Gravity.CENTER);
        
        mContrastSeekBar = new SeekBar(this);
        mContrastSeekBar.setOnSeekBarChangeListener(this);
        mContrastSeekBar.setId(CONTRAST_SEEKBAR_RESID);
        mContrastSeekBar.setMax(MAX_VALUE);
        mContrastSeekBar.setProgress(MAX_VALUE/2);
      
        mainLayout.addView(mBrightnessTextView);
        mainLayout.addView(mBrightnessSeekBar);
        mainLayout.addView(mContrastTextView);
        mainLayout.addView(mContrastSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case BRIGHTNESS_SEEKBAR_RESID:
            mBrightnessValue = progress;
            mBrightnessTextView.setText(BRIGHTNESS_STRING+mBrightnessValue);
            break;
        case CONTRAST_SEEKBAR_RESID:
            mContrastValue = progress;
            mContrastTextView.setText(CONTRAST_STRING+mContrastValue);
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
                ContrastFilter filter = new ContrastFilter();
                filter.setBrightness(getValue(mBrightnessValue));
                filter.setContrast(getValue(mContrastValue));
                mColors = filter.filter(mColors, width, height);

                ContrastFilterActivity.this.runOnUiThread(new Runnable() {
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

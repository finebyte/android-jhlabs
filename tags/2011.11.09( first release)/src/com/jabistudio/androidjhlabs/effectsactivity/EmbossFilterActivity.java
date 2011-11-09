package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.coloradjustmentfilteractivity.ChannelMixFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.EmbossFilter;
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

public class EmbossFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Emboss";
    private static final String DIRECTION_STRING = "DIRECTION:";
    private static final String ELEVATION_STRING = "ELEVATION:";
    private static final String BUMP_HEIGHT_STRING = "BUMP HEIGHT:";
    private static final int DIRECTION_MAX_VALUE = 624;
    private static final int MAX_VALUE = 100;
    
    private static final int DIRECTION_SEEKBAR_RESID = 21863;
    private static final int ELEVEATION_SEEKBAR_RESID = 21864;
    private static final int BUMP_HEIGHT_SEEKBAR_RESID = 21865;
    
    private SeekBar mDirectionSeekBar;
    private TextView mDirectionTextView;
    private SeekBar mElevationSeekBar;
    private TextView mElevationTextView;
    private SeekBar mBumpHeightSeekBar;
    private TextView mBumpHeightTextView;
    
    private int mDirectionValue;
    private int mElevationValue;
    private int mBumpHeightValue;
    
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
        mDirectionTextView = new TextView(this);
        mDirectionTextView.setText(DIRECTION_STRING+mDirectionValue);
        mDirectionTextView.setTextSize(TITLE_TEXT_SIZE);
        mDirectionTextView.setTextColor(Color.BLACK);
        mDirectionTextView.setGravity(Gravity.CENTER);
        
        mDirectionSeekBar = new SeekBar(this);
        mDirectionSeekBar.setOnSeekBarChangeListener(this);
        mDirectionSeekBar.setId(DIRECTION_SEEKBAR_RESID);
        mDirectionSeekBar.setMax(DIRECTION_MAX_VALUE);
        
        mElevationTextView = new TextView(this);
        mElevationTextView.setText(ELEVATION_STRING+mElevationValue);
        mElevationTextView.setTextSize(TITLE_TEXT_SIZE);
        mElevationTextView.setTextColor(Color.BLACK);
        mElevationTextView.setGravity(Gravity.CENTER);
        
        mElevationSeekBar = new SeekBar(this);
        mElevationSeekBar.setOnSeekBarChangeListener(this);
        mElevationSeekBar.setId(ELEVEATION_SEEKBAR_RESID);
        mElevationSeekBar.setMax(MAX_VALUE);
        
        mBumpHeightTextView = new TextView(this);
        mBumpHeightTextView.setText(BUMP_HEIGHT_STRING+mBumpHeightValue);
        mBumpHeightTextView.setTextSize(TITLE_TEXT_SIZE);
        mBumpHeightTextView.setTextColor(Color.BLACK);
        mBumpHeightTextView.setGravity(Gravity.CENTER);
        
        mBumpHeightSeekBar = new SeekBar(this);
        mBumpHeightSeekBar.setOnSeekBarChangeListener(this);
        mBumpHeightSeekBar.setId(BUMP_HEIGHT_SEEKBAR_RESID);
        mBumpHeightSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mDirectionTextView);
        mainLayout.addView(mDirectionSeekBar);
        mainLayout.addView(mElevationTextView);
        mainLayout.addView(mElevationSeekBar);
        mainLayout.addView(mBumpHeightTextView);
        mainLayout.addView(mBumpHeightSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case DIRECTION_SEEKBAR_RESID:
            mDirectionValue = progress;
            mDirectionTextView.setText(DIRECTION_STRING+getValue(mDirectionValue));
            break;
        case ELEVEATION_SEEKBAR_RESID:
            mElevationValue = progress;
            mElevationTextView.setText(ELEVATION_STRING+getValue(mElevationValue));
            break;
        case BUMP_HEIGHT_SEEKBAR_RESID:
            mBumpHeightValue = progress;
            mBumpHeightTextView.setText(BUMP_HEIGHT_STRING+getValue(mBumpHeightValue));
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
                EmbossFilter filter = new EmbossFilter();
                filter.setAzimuth(getValue(mDirectionValue));
                filter.setElevation(getValue(mElevationValue));
                filter.setBumpHeight(getValue(mBumpHeightValue));
                
                mColors = filter.filter(mColors, width, height);

                EmbossFilterActivity.this.runOnUiThread(new Runnable() {
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

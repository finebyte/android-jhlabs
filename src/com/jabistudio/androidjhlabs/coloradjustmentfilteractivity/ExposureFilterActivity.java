package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
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

public class ExposureFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Exposure";
    private static final String EXPOSURE_STRING = "EXPOSURE:";
    private static final int MAX_VALUE = 500;
    
    private static final int EXPOSURE_SEEKBAR_RESID = 21865;
    
    private SeekBar mExposureSeekBar;
    private TextView mExposureTextView;
    
    private int mExposureValue;
    
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
        mExposureTextView = new TextView(this);
        mExposureTextView.setText(EXPOSURE_STRING+mExposureValue);
        mExposureTextView.setTextSize(TITLE_TEXT_SIZE);
        mExposureTextView.setTextColor(Color.BLACK);
        mExposureTextView.setGravity(Gravity.CENTER);
        
        mExposureSeekBar = new SeekBar(this);
        mExposureSeekBar.setOnSeekBarChangeListener(this);
        mExposureSeekBar.setId(EXPOSURE_SEEKBAR_RESID);
        mExposureSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mExposureTextView);
        mainLayout.addView(mExposureSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case EXPOSURE_SEEKBAR_RESID:
            mExposureValue = progress;
            mExposureTextView.setText(EXPOSURE_STRING+getExposure(mExposureValue));
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
                ExposureFilter filter = new ExposureFilter();
                filter.setExposure(getExposure(mExposureValue));

                mColors = filter.filter(mColors, width, height);
                ExposureFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getExposure(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

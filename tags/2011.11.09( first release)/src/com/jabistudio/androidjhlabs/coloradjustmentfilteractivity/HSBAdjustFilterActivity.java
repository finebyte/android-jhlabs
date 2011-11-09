package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.HSBAdjustFilter;
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

public class HSBAdjustFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "HSBAdjust";
    private static final String HFACTOR_STRING = "HFACTOR:";
    private static final String SFACTOR_STRING = "SFACTOR:";
    private static final String BFACTOR_STRING = "BFACTOR:";
    private static final int MAX_VALUE = 200;
    
    private static final int HFACTOR_SEEKBAR_RESID = 21863;
    private static final int SFACTOR_SEEKBAR_RESID = 21864;
    private static final int BFACTOR_SEEKBAR_RESID = 21865;
    
    private SeekBar mHFactorSeekBar;
    private TextView mHFactorTextView;
    private SeekBar mSFactorSeekBar;
    private TextView mSFactorTextView;
    private SeekBar mBFactorSeekBar;
    private TextView mBFactorTextView;
    
    private int mHFactorValue;
    private int mSFactorValue;
    private int mBFactorValue;
    
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
        mHFactorTextView = new TextView(this);
        mHFactorTextView.setText(HFACTOR_STRING+mHFactorValue);
        mHFactorTextView.setTextSize(TITLE_TEXT_SIZE);
        mHFactorTextView.setTextColor(Color.BLACK);
        mHFactorTextView.setGravity(Gravity.CENTER);
        
        mHFactorSeekBar = new SeekBar(this);
        mHFactorSeekBar.setOnSeekBarChangeListener(this);
        mHFactorSeekBar.setId(HFACTOR_SEEKBAR_RESID);
        mHFactorSeekBar.setMax(MAX_VALUE);
        mHFactorSeekBar.setProgress(100);
        
        mSFactorTextView = new TextView(this);
        mSFactorTextView.setText(SFACTOR_STRING+mSFactorValue);
        mSFactorTextView.setTextSize(TITLE_TEXT_SIZE);
        mSFactorTextView.setTextColor(Color.BLACK);
        mSFactorTextView.setGravity(Gravity.CENTER);
        
        mSFactorSeekBar = new SeekBar(this);
        mSFactorSeekBar.setOnSeekBarChangeListener(this);
        mSFactorSeekBar.setId(SFACTOR_SEEKBAR_RESID);
        mSFactorSeekBar.setMax(MAX_VALUE);
        mSFactorSeekBar.setProgress(100);
        
        mBFactorTextView = new TextView(this);
        mBFactorTextView.setText(BFACTOR_STRING+mBFactorValue);
        mBFactorTextView.setTextSize(TITLE_TEXT_SIZE);
        mBFactorTextView.setTextColor(Color.BLACK);
        mBFactorTextView.setGravity(Gravity.CENTER);
        
        mBFactorSeekBar = new SeekBar(this);
        mBFactorSeekBar.setOnSeekBarChangeListener(this);
        mBFactorSeekBar.setId(BFACTOR_SEEKBAR_RESID);
        mBFactorSeekBar.setMax(MAX_VALUE);
        mBFactorSeekBar.setProgress(100);
        
        mainLayout.addView(mHFactorTextView);
        mainLayout.addView(mHFactorSeekBar);
        mainLayout.addView(mSFactorTextView);
        mainLayout.addView(mSFactorSeekBar);
        mainLayout.addView(mBFactorTextView);
        mainLayout.addView(mBFactorSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case HFACTOR_SEEKBAR_RESID:
            mHFactorValue = progress;
            mHFactorTextView.setText(HFACTOR_STRING+getHFactor(mHFactorValue));
            break;
        case SFACTOR_SEEKBAR_RESID:
            mSFactorValue = progress;
            mSFactorTextView.setText(SFACTOR_STRING+getSFactor(mSFactorValue));
            break;
        case BFACTOR_SEEKBAR_RESID:
            mBFactorValue = progress;
            mBFactorTextView.setText(BFACTOR_STRING+getBFactor(mBFactorValue));
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
                HSBAdjustFilter filter = new HSBAdjustFilter();
                filter.setHFactor(getHFactor(mHFactorValue));
                filter.setSFactor(getSFactor(mSFactorValue));
                filter.setBFactor(getBFactor(mBFactorValue));
                mColors = filter.filter(mColors, width, height);

                HSBAdjustFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getHFactor(int value){
        float retValue = 0;
        retValue = (float)((value - 100));
        return retValue;
    }
    private float getSFactor(int value){
        float retValue = 0;
        retValue = (float)((value - 100) / 100f);
        return retValue;
    }
    private float getBFactor(int value){
        float retValue = 0;
        retValue = (float)((value - 100) / 100f);
        return retValue;
    }
}

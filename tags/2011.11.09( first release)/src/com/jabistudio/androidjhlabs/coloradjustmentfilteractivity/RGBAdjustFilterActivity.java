package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.HSBAdjustFilter;
import com.jabistudio.androidjhlabs.filter.RGBAdjustFilter;
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

public class RGBAdjustFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "RGBAdjust";
    private static final String RFACTOR_STRING = "RFACTOR:";
    private static final String GFACTOR_STRING = "GFACTOR:";
    private static final String BFACTOR_STRING = "BFACTOR:";
    private static final int MAX_VALUE = 200;
    
    private static final int RFACTOR_SEEKBAR_RESID = 21863;
    private static final int GFACTOR_SEEKBAR_RESID = 21864;
    private static final int BFACTOR_SEEKBAR_RESID = 21865;
    
    private SeekBar mRFactorSeekBar;
    private TextView mRFactorTextView;
    private SeekBar mGFactorSeekBar;
    private TextView mGFactorTextView;
    private SeekBar mBFactorSeekBar;
    private TextView mBFactorTextView;
    
    private int mRFactorValue;
    private int mGFactorValue;
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
        mRFactorTextView = new TextView(this);
        mRFactorTextView.setText(RFACTOR_STRING+mRFactorValue);
        mRFactorTextView.setTextSize(TITLE_TEXT_SIZE);
        mRFactorTextView.setTextColor(Color.BLACK);
        mRFactorTextView.setGravity(Gravity.CENTER);
        
        mRFactorSeekBar = new SeekBar(this);
        mRFactorSeekBar.setOnSeekBarChangeListener(this);
        mRFactorSeekBar.setId(RFACTOR_SEEKBAR_RESID);
        mRFactorSeekBar.setMax(MAX_VALUE);
        mRFactorSeekBar.setProgress(100);
        
        mGFactorTextView = new TextView(this);
        mGFactorTextView.setText(GFACTOR_STRING+mGFactorValue);
        mGFactorTextView.setTextSize(TITLE_TEXT_SIZE);
        mGFactorTextView.setTextColor(Color.BLACK);
        mGFactorTextView.setGravity(Gravity.CENTER);
        
        mGFactorSeekBar = new SeekBar(this);
        mGFactorSeekBar.setOnSeekBarChangeListener(this);
        mGFactorSeekBar.setId(GFACTOR_SEEKBAR_RESID);
        mGFactorSeekBar.setMax(MAX_VALUE);
        mGFactorSeekBar.setProgress(100);
        
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
        
        mainLayout.addView(mRFactorTextView);
        mainLayout.addView(mRFactorSeekBar);
        mainLayout.addView(mGFactorTextView);
        mainLayout.addView(mGFactorSeekBar);
        mainLayout.addView(mBFactorTextView);
        mainLayout.addView(mBFactorSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case RFACTOR_SEEKBAR_RESID:
            mRFactorValue = progress;
            mRFactorTextView.setText(RFACTOR_STRING+getRFactor(mRFactorValue));
            break;
        case GFACTOR_SEEKBAR_RESID:
            mGFactorValue = progress;
            mGFactorTextView.setText(GFACTOR_STRING+getGFactor(mGFactorValue));
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
                RGBAdjustFilter filter = new RGBAdjustFilter();
                filter.setRFactor(getRFactor(mRFactorValue));
                filter.setGFactor(getGFactor(mGFactorValue));
                filter.setBFactor(getBFactor(mBFactorValue));
                mColors = filter.filter(mColors, width, height);

                RGBAdjustFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getRFactor(int value){
        float retValue = 0;
        retValue = (float)((value - 100) / 100f);
        return retValue;
    }
    private float getGFactor(int value){
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

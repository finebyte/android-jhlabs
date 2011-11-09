package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.SharpenFilter;
import com.jabistudio.androidjhlabs.filter.SmartBlurFilter;
import com.jabistudio.androidjhlabs.filter.UnsharpFilter;
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

/**
 * TODO Problem Filter
 * @author psk
 *
 */
public class UnsharpFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "UnSharpen";
        
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String THRESHOLD_STRING = "THRESHOLD:";
    private static final int MAX_VALUE = 100;
    
    private static final int AMOUNT_SEEKBAR_RESID = 21869;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    private static final int THRESHOLD_SEEKBAR_RESID = 21866;
    
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mThresHoldSeekBar;
    private TextView mThresHoldTextView;
    
    private int mAmountValue;
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
        mAmountTextView = new TextView(this);
        mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
        mAmountTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmountTextView.setTextColor(Color.BLACK);
        mAmountTextView.setGravity(Gravity.CENTER);
        
        mAmountSeekBar = new SeekBar(this);
        mAmountSeekBar.setOnSeekBarChangeListener(this);
        mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        mAmountSeekBar.setMax(MAX_VALUE);
        
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
        
        mainLayout.addView(mAmountTextView);
        mainLayout.addView(mAmountSeekBar);
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mThresHoldTextView);
        mainLayout.addView(mThresHoldSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case AMOUNT_SEEKBAR_RESID:
            mAmountValue = progress;
            mAmountTextView.setText(AMOUNT_STRING+getAmout(mAmountValue));
            break;
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
                UnsharpFilter filter = new UnsharpFilter();
                filter.setAmount(getAmout(mAmountValue));
                filter.setRadius(mRadiusValue);
                filter.setThreshold(mThresHoldValue);
                mColors = filter.filter(mColors, width, height);
                
                UnsharpFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getAmout(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

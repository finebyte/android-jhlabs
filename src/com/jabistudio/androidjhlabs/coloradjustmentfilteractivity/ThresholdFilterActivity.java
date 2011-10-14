package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ContrastFilter;
import com.jabistudio.androidjhlabs.filter.ThresholdFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class ThresholdFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Threshold";
    private static final String LOWER_STRING = "LOWER:";
    private static final String UPPER_STRING = "UPPER:";
    private static final int MAX_VALUE = 255;
    
    private static final int LOWER_SEEKBAR_RESID = 21863;
    private static final int UPPER_SEEKBAR_RESID = 21864;
    
    private SeekBar mLowerSeekBar;
    private TextView mLowerTextView;
    private SeekBar mUpperSeekBar;
    private TextView mUpperTextView;
    
    private int mLowerValue;
    private int mUpperValue;
    
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
        mLowerTextView = new TextView(this);
        mLowerTextView.setText(LOWER_STRING+mLowerValue);
        mLowerTextView.setTextSize(TITLE_TEXT_SIZE);
        mLowerTextView.setTextColor(Color.BLACK);
        mLowerTextView.setGravity(Gravity.CENTER);
        
        mLowerSeekBar = new SeekBar(this);
        mLowerSeekBar.setOnSeekBarChangeListener(this);
        mLowerSeekBar.setId(LOWER_SEEKBAR_RESID);
        mLowerSeekBar.setMax(MAX_VALUE);
        mLowerSeekBar.setProgress(MAX_VALUE/2);
        
        mUpperTextView = new TextView(this);
        mUpperTextView.setText(UPPER_STRING+mUpperValue);
        mUpperTextView.setTextSize(TITLE_TEXT_SIZE);
        mUpperTextView.setTextColor(Color.BLACK);
        mUpperTextView.setGravity(Gravity.CENTER);
        
        mUpperSeekBar = new SeekBar(this);
        mUpperSeekBar.setOnSeekBarChangeListener(this);
        mUpperSeekBar.setId(UPPER_SEEKBAR_RESID);
        mUpperSeekBar.setMax(MAX_VALUE);
        mUpperSeekBar.setProgress(MAX_VALUE/2);
      
        mainLayout.addView(mLowerTextView);
        mainLayout.addView(mLowerSeekBar);
        mainLayout.addView(mUpperTextView);
        mainLayout.addView(mUpperSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case LOWER_SEEKBAR_RESID:
            mLowerValue = progress;
            mLowerTextView.setText(LOWER_STRING+mLowerValue);
            break;
        case UPPER_SEEKBAR_RESID:
            mUpperValue = progress;
            mUpperTextView.setText(UPPER_STRING+mUpperValue);
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
                ThresholdFilter filter = new ThresholdFilter();
                filter.setLowerThreshold(mLowerValue);
                filter.setUpperThreshold(mUpperValue);
                
                mColors = filter.filter(mColors, width, height);

                ThresholdFilterActivity.this.runOnUiThread(new Runnable() {
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

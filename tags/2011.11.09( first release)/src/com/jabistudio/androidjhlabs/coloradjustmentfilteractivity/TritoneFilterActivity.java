package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ThresholdFilter;
import com.jabistudio.androidjhlabs.filter.TritoneFilter;
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

public class TritoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Tritone";
    private static final String HIGH_STRING = "HIGH:";
    private static final String MID_STRING = "MID:";
    private static final String SHADOW_STRING = "SHADOW";
    private static final int MAX_VALUE = 0xFFFFFFFF - 0xFF000000;
    
    private static final int HIGH_SEEKBAR_RESID = 21863;
    private static final int MID_SEEKBAR_RESID = 21864;
    private static final int SHADOW_SEEKBAR_RESID = 21865;
    
    private SeekBar mHighSeekBar;
    private TextView mHighTextView;
    private SeekBar mMidSeekBar;
    private TextView mMidTextView;
    private SeekBar mShadowSeekBar;
    private TextView mShadowTextView;
    
    private int mHighValue;
    private int mMidValue;
    private int mShadowValue;
    
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
        mHighTextView = new TextView(this);
        mHighTextView.setText(HIGH_STRING+mHighValue);
        mHighTextView.setTextSize(TITLE_TEXT_SIZE);
        mHighTextView.setTextColor(Color.BLACK);
        mHighTextView.setGravity(Gravity.CENTER);
        
        mHighSeekBar = new SeekBar(this);
        mHighSeekBar.setOnSeekBarChangeListener(this);
        mHighSeekBar.setId(HIGH_SEEKBAR_RESID);
        mHighSeekBar.setMax(MAX_VALUE);
        
        mMidTextView = new TextView(this);
        mMidTextView.setText(MID_STRING+mMidValue);
        mMidTextView.setTextSize(TITLE_TEXT_SIZE);
        mMidTextView.setTextColor(Color.BLACK);
        mMidTextView.setGravity(Gravity.CENTER);
        
        mMidSeekBar = new SeekBar(this);
        mMidSeekBar.setOnSeekBarChangeListener(this);
        mMidSeekBar.setId(MID_SEEKBAR_RESID);
        mMidSeekBar.setMax(MAX_VALUE);
        
        mShadowTextView = new TextView(this);
        mShadowTextView.setText(SHADOW_STRING+mShadowValue);
        mShadowTextView.setTextSize(TITLE_TEXT_SIZE);
        mShadowTextView.setTextColor(Color.BLACK);
        mShadowTextView.setGravity(Gravity.CENTER);
        
        mShadowSeekBar = new SeekBar(this);
        mShadowSeekBar.setOnSeekBarChangeListener(this);
        mShadowSeekBar.setId(SHADOW_SEEKBAR_RESID);
        mShadowSeekBar.setMax(MAX_VALUE);
      
        mainLayout.addView(mHighTextView);
        mainLayout.addView(mHighSeekBar);
        mainLayout.addView(mMidTextView);
        mainLayout.addView(mMidSeekBar);
        mainLayout.addView(mShadowTextView);
        mainLayout.addView(mShadowSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case HIGH_SEEKBAR_RESID:
            mHighValue = progress;
            mHighTextView.setText(HIGH_STRING+getValue(mHighValue));
            break;
        case MID_SEEKBAR_RESID:
            mMidValue = progress;
            mMidTextView.setText(MID_STRING+getValue(mMidValue));
            break;
        case SHADOW_SEEKBAR_RESID:
            mShadowValue = progress;
            mShadowTextView.setText(SHADOW_STRING+getValue(mShadowValue));
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
                TritoneFilter filter = new TritoneFilter();
                filter.setHighColor(getValue(mHighValue));
                filter.setMidColor(getValue(mMidValue));
                filter.setShadowColor(getValue(mShadowValue));
                
                mColors = filter.filter(mColors, width, height);

                TritoneFilterActivity.this.runOnUiThread(new Runnable() {
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
    private int getValue(int value){
        int retValue = 0;
        retValue = value + 0xFF000000;
        return retValue;
    }
}

package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.LevelsFilter;
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

public class LevelsFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Levels";
    private static final String LOWLEVEL_STRING = "LOWLEVEL:";
    private static final String HIGHLEVEL_STRING = "HIGHLEVEL:";
    private static final String LOWOUTPUT_STRING = "LOWOUTPUT:";
    private static final String HIGHOUTPUT_STRING = "HIGHOUTPUT:";
    private static final int MAX_VALUE = 100;
    
    private static final int LOWLEVEL_SEEKBAR_RESID = 21863;
    private static final int HIGHLEVEL_SEEKBAR_RESID = 21864;
    private static final int LOWOUTPUT_SEEKBAR_RESID = 21865;
    private static final int HIGHOUTPUT_SEEKBAR_RESID = 21866;
    
    private SeekBar mLowSeekBar;
    private TextView mLowTextView;
    private SeekBar mHighSeekBar;
    private TextView mHighTextView;
    private SeekBar mLowOutputSeekBar;
    private TextView mLowOutputTextView;
    private SeekBar mHighOutputSeekBar;
    private TextView mHighOutputTextView;
    
    private int mLowValue;
    private int mHighValue;
    private int mLowOutputValue;
    private int mHighOutputValue;
    
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
        mLowTextView = new TextView(this);
        mLowTextView.setText(LOWLEVEL_STRING+mLowValue);
        mLowTextView.setTextSize(TITLE_TEXT_SIZE);
        mLowTextView.setTextColor(Color.BLACK);
        mLowTextView.setGravity(Gravity.CENTER);
        
        mLowSeekBar = new SeekBar(this);
        mLowSeekBar.setOnSeekBarChangeListener(this);
        mLowSeekBar.setId(LOWLEVEL_SEEKBAR_RESID);
        mLowSeekBar.setMax(MAX_VALUE);
        
        mHighTextView = new TextView(this);
        mHighTextView.setText(HIGHLEVEL_STRING+mHighValue);
        mHighTextView.setTextSize(TITLE_TEXT_SIZE);
        mHighTextView.setTextColor(Color.BLACK);
        mHighTextView.setGravity(Gravity.CENTER);
        
        mHighSeekBar = new SeekBar(this);
        mHighSeekBar.setOnSeekBarChangeListener(this);
        mHighSeekBar.setId(HIGHLEVEL_SEEKBAR_RESID);
        mHighSeekBar.setMax(MAX_VALUE);
        mHighSeekBar.setProgress(MAX_VALUE);
        
        mLowOutputTextView = new TextView(this);
        mLowOutputTextView.setText(LOWOUTPUT_STRING+mLowOutputValue);
        mLowOutputTextView.setTextSize(TITLE_TEXT_SIZE);
        mLowOutputTextView.setTextColor(Color.BLACK);
        mLowOutputTextView.setGravity(Gravity.CENTER);
        
        mLowOutputSeekBar = new SeekBar(this);
        mLowOutputSeekBar.setOnSeekBarChangeListener(this);
        mLowOutputSeekBar.setId(LOWOUTPUT_SEEKBAR_RESID);
        mLowOutputSeekBar.setMax(MAX_VALUE);
        
        mHighOutputTextView = new TextView(this);
        mHighOutputTextView.setText(HIGHOUTPUT_STRING+mHighOutputValue);
        mHighOutputTextView.setTextSize(TITLE_TEXT_SIZE);
        mHighOutputTextView.setTextColor(Color.BLACK);
        mHighOutputTextView.setGravity(Gravity.CENTER);
        
        mHighOutputSeekBar = new SeekBar(this);
        mHighOutputSeekBar.setOnSeekBarChangeListener(this);
        mHighOutputSeekBar.setId(HIGHOUTPUT_SEEKBAR_RESID);
        mHighOutputSeekBar.setMax(MAX_VALUE);
        mHighOutputSeekBar.setProgress(MAX_VALUE);
        
        mainLayout.addView(mLowTextView);
        mainLayout.addView(mLowSeekBar);
        mainLayout.addView(mHighTextView);
        mainLayout.addView(mHighSeekBar);
        mainLayout.addView(mLowOutputTextView);
        mainLayout.addView(mLowOutputSeekBar);
        mainLayout.addView(mHighOutputTextView);
        mainLayout.addView(mHighOutputSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case LOWLEVEL_SEEKBAR_RESID:
            mLowValue = progress;
            mLowTextView.setText(LOWLEVEL_STRING+getValue(mLowValue));
            break;
        case HIGHLEVEL_SEEKBAR_RESID:
            mHighValue = progress;
            mHighTextView.setText(HIGHLEVEL_STRING+getValue(mHighValue));
            break;
        case LOWOUTPUT_SEEKBAR_RESID:
            mLowOutputValue = progress;
            mLowOutputTextView.setText(LOWOUTPUT_STRING+getValue(mLowOutputValue));
            break;
        case HIGHOUTPUT_SEEKBAR_RESID:
            mHighOutputValue = progress;
            mHighOutputTextView.setText(HIGHOUTPUT_STRING+getValue(mHighOutputValue));
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
                LevelsFilter filter = new LevelsFilter();
                filter.setLowLevel(getValue(mLowValue));
                filter.setHighLevel(getValue(mHighValue));
                filter.setLowOutputLevel(getValue(mLowOutputValue));
                filter.setHighOutputLevel(getValue(mHighOutputValue));
                mColors = filter.filter(mColors, width, height);

                LevelsFilterActivity.this.runOnUiThread(new Runnable() {
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

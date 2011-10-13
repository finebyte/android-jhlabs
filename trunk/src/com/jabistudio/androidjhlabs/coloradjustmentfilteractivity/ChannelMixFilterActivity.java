package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.BoxBlurFilterActivity;
import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
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

public class ChannelMixFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "ChannelMix";
    private static final String INTOR_STRING = "INTOR:";
    private static final String INTOG_STRING = "INTOG:";
    private static final String INTOB_STRING = "INTOB:";
    private static final int MAX_VALUE = 255;
    
    private static final int INTOR_SEEKBAR_RESID = 21863;
    private static final int INTOG_SEEKBAR_RESID = 21864;
    private static final int INTOB_SEEKBAR_RESID = 21865;
    
    private SeekBar mIntoRSeekBar;
    private TextView mIntoRTextView;
    private SeekBar mIntoGSeekBar;
    private TextView mIntoGTextView;
    private SeekBar mIntoBSeekBar;
    private TextView mIntoBTextView;
    
    private int mIntoRValue;
    private int mIntoGValue;
    private int mIntoBValue;
    
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
        mIntoRTextView = new TextView(this);
        mIntoRTextView.setText(INTOR_STRING+mIntoRValue);
        mIntoRTextView.setTextSize(TITLE_TEXT_SIZE);
        mIntoRTextView.setTextColor(Color.BLACK);
        mIntoRTextView.setGravity(Gravity.CENTER);
        
        mIntoRSeekBar = new SeekBar(this);
        mIntoRSeekBar.setOnSeekBarChangeListener(this);
        mIntoRSeekBar.setId(INTOR_SEEKBAR_RESID);
        mIntoRSeekBar.setMax(MAX_VALUE);
        
        mIntoGTextView = new TextView(this);
        mIntoGTextView.setText(INTOG_STRING+mIntoGValue);
        mIntoGTextView.setTextSize(TITLE_TEXT_SIZE);
        mIntoGTextView.setTextColor(Color.BLACK);
        mIntoGTextView.setGravity(Gravity.CENTER);
        
        mIntoGSeekBar = new SeekBar(this);
        mIntoGSeekBar.setOnSeekBarChangeListener(this);
        mIntoGSeekBar.setId(INTOG_SEEKBAR_RESID);
        mIntoGSeekBar.setMax(MAX_VALUE);
        
        mIntoBTextView = new TextView(this);
        mIntoBTextView.setText(INTOB_STRING+mIntoBValue);
        mIntoBTextView.setTextSize(TITLE_TEXT_SIZE);
        mIntoBTextView.setTextColor(Color.BLACK);
        mIntoBTextView.setGravity(Gravity.CENTER);
        
        mIntoBSeekBar = new SeekBar(this);
        mIntoBSeekBar.setOnSeekBarChangeListener(this);
        mIntoBSeekBar.setId(INTOB_SEEKBAR_RESID);
        mIntoBSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mIntoRTextView);
        mainLayout.addView(mIntoRSeekBar);
        mainLayout.addView(mIntoGTextView);
        mainLayout.addView(mIntoGSeekBar);
        mainLayout.addView(mIntoBTextView);
        mainLayout.addView(mIntoBSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case INTOR_SEEKBAR_RESID:
            mIntoRValue = progress;
            mIntoRTextView.setText(INTOR_STRING+mIntoRValue);
            break;
        case INTOG_SEEKBAR_RESID:
            mIntoGValue = progress;
            mIntoGTextView.setText(INTOG_STRING+mIntoGValue);
            break;
        case INTOB_SEEKBAR_RESID:
            mIntoBValue = progress;
            mIntoBTextView.setText(INTOB_STRING+mIntoBValue);
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
                ChannelMixFilter filter = new ChannelMixFilter();
                filter.setIntoR(mIntoRValue);
                filter.setIntoG(mIntoGValue);
                filter.setIntoB(mIntoBValue);
                mColors = filter.filter(mColors, width, height);

                ChannelMixFilterActivity.this.runOnUiThread(new Runnable() {
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

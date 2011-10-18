package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.CrystallizeFilter;
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

public class CrystallizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Crystallize";
    private static final String SIZE_STRING = "SIZE:";
    private static final String RANDOMNESS_STRING = "RANDOMNESS:";
    private static final String EDGE_STRING = "EDGE:";
    private static final int MAX_VALUE = 100;
    
    private static final int SIZE_SEEKBAR_RESID = 21865;
    private static final int RANDOMNESS_SEEKBAR_RESID = 21866;
    private static final int EDGE_SEEKBAR_RESID = 21867;
    
    private SeekBar mSizeSeekBar;
    private TextView mSizeTextView;
    private SeekBar mRandomnessSeekBar;
    private TextView mRandomnessTextView;
    private SeekBar mEdgeSeekBar;
    private TextView mEdgeTextView;
    
    private int mSizeValue;
    private int mRandomnessValue;
    private int mEdgeValue;
    
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
        mSizeTextView = new TextView(this);
        mSizeTextView.setText(SIZE_STRING+mSizeValue);
        mSizeTextView.setTextSize(TITLE_TEXT_SIZE);
        mSizeTextView.setTextColor(Color.BLACK);
        mSizeTextView.setGravity(Gravity.CENTER);
        
        mSizeSeekBar = new SeekBar(this);
        mSizeSeekBar.setOnSeekBarChangeListener(this);
        mSizeSeekBar.setId(SIZE_SEEKBAR_RESID);
        mSizeSeekBar.setMax(MAX_VALUE);
        
        mRandomnessTextView = new TextView(this);
        mRandomnessTextView.setText(RANDOMNESS_STRING+mRandomnessValue);
        mRandomnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mRandomnessTextView.setTextColor(Color.BLACK);
        mRandomnessTextView.setGravity(Gravity.CENTER);
        
        mRandomnessSeekBar = new SeekBar(this);
        mRandomnessSeekBar.setOnSeekBarChangeListener(this);
        mRandomnessSeekBar.setId(RANDOMNESS_SEEKBAR_RESID);
        mRandomnessSeekBar.setMax(MAX_VALUE);
        
        mEdgeTextView = new TextView(this);
        mEdgeTextView.setText(EDGE_STRING+mEdgeValue);
        mEdgeTextView.setTextSize(TITLE_TEXT_SIZE);
        mEdgeTextView.setTextColor(Color.BLACK);
        mEdgeTextView.setGravity(Gravity.CENTER);
        
        mEdgeSeekBar = new SeekBar(this);
        mEdgeSeekBar.setOnSeekBarChangeListener(this);
        mEdgeSeekBar.setId(EDGE_SEEKBAR_RESID);
        mEdgeSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mSizeTextView);
        mainLayout.addView(mSizeSeekBar);
        mainLayout.addView(mRandomnessTextView);
        mainLayout.addView(mRandomnessSeekBar);
        mainLayout.addView(mEdgeTextView);
        mainLayout.addView(mEdgeSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SIZE_SEEKBAR_RESID:
            mSizeValue = progress;
            mSizeTextView.setText(SIZE_STRING+getAmout(mSizeValue));
            break;
        case RANDOMNESS_SEEKBAR_RESID:
            mRandomnessValue = progress;
            mRandomnessTextView.setText(RANDOMNESS_STRING+mRandomnessValue);
            break;
        case EDGE_SEEKBAR_RESID:
            mEdgeValue = progress;
            mEdgeTextView.setText(EDGE_STRING+mEdgeValue);
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
                CrystallizeFilter filter = new CrystallizeFilter();
                filter.setEdgeColor(Color.BLACK);
                filter.setAmount(getAmout(mSizeValue));
                filter.setEdgeThickness(getAmout(mEdgeValue));
                filter.setRandomness(getAmout(mRandomnessValue));

                mColors = filter.filter(mColors, width, height);
                CrystallizeFilterActivity.this.runOnUiThread(new Runnable() {
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

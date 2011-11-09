package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.SwimFilter;
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

public class SwimFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Swim";
    private static final String SCALE_STRING = "SCALE:";
    private static final String ANGLE_STRING = "ANGLE:";
    private static final String STRETCH_STRING = "STRETCH:";
    private static final String TURBULENCE_STRING = "TURBULENCE:";
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String TIME_STRING = "TIME:";
    private static final int MAX_SCALE_VALUE = 300;
    private static final int MAX_ANGLE_VALUE = 624;
    private static final int MAX_STRETCH_VALUE = 50;
    private static final int MAX_TURBULENCE_VALUE = 10;
    private static final int MAX_VALUE = 100;
    
    private static final int SCALE_SEEKBAR_RESID = 21863;
    private static final int ANGLE_SEEKBAR_RESID = 21864;
    private static final int STRETCH_SEEKBAR_RESID = 21865;
    private static final int TURBULENCE_SEEKBAR_RESID = 21866;
    private static final int AMOUNT_SEEKBAR_RESID = 21867;
    private static final int TIME_SEEKBAR_RESID = 21868;
    
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private SeekBar mStretchSeekBar;
    private TextView mStretchTextView;
    private SeekBar mTurbulenceSeekBar;
    private TextView mTurbulenceTextView;
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private SeekBar mTimeSeekBar;
    private TextView mTimeTextView;
    
    private int mScaleValue;
    private int mAngleValue;
    private int mStretchValue;
    private int mTurbulenceValue;
    private int mAmountValue;
    private int mTimeValue;
    
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
        mScaleTextView = new TextView(this);
        mScaleTextView.setText(SCALE_STRING+mScaleValue);
        mScaleTextView.setTextSize(TITLE_TEXT_SIZE);
        mScaleTextView.setTextColor(Color.BLACK);
        mScaleTextView.setGravity(Gravity.CENTER);
        
        mScaleSeekBar = new SeekBar(this);
        mScaleSeekBar.setOnSeekBarChangeListener(this);
        mScaleSeekBar.setId(SCALE_SEEKBAR_RESID);
        mScaleSeekBar.setMax(MAX_SCALE_VALUE);
        
        mAngleTextView = new TextView(this);
        mAngleTextView.setText(ANGLE_STRING+mAngleValue);
        mAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mAngleTextView.setTextColor(Color.BLACK);
        mAngleTextView.setGravity(Gravity.CENTER);
        
        mAngleSeekBar = new SeekBar(this);
        mAngleSeekBar.setOnSeekBarChangeListener(this);
        mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        mAngleSeekBar.setMax(MAX_ANGLE_VALUE);
        
        mStretchTextView = new TextView(this);
        mStretchTextView.setText(STRETCH_STRING+mStretchValue);
        mStretchTextView.setTextSize(TITLE_TEXT_SIZE);
        mStretchTextView.setTextColor(Color.BLACK);
        mStretchTextView.setGravity(Gravity.CENTER);
        
        mStretchSeekBar = new SeekBar(this);
        mStretchSeekBar.setOnSeekBarChangeListener(this);
        mStretchSeekBar.setId(STRETCH_SEEKBAR_RESID);
        mStretchSeekBar.setMax(MAX_STRETCH_VALUE);
        
        mTurbulenceTextView = new TextView(this);
        mTurbulenceTextView.setText(TURBULENCE_STRING+mTurbulenceValue);
        mTurbulenceTextView.setTextSize(TITLE_TEXT_SIZE);
        mTurbulenceTextView.setTextColor(Color.BLACK);
        mTurbulenceTextView.setGravity(Gravity.CENTER);
        
        mTurbulenceSeekBar = new SeekBar(this);
        mTurbulenceSeekBar.setOnSeekBarChangeListener(this);
        mTurbulenceSeekBar.setId(TURBULENCE_SEEKBAR_RESID);
        mTurbulenceSeekBar.setMax(MAX_TURBULENCE_VALUE);
        
        mAmountTextView = new TextView(this);
        mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
        mAmountTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmountTextView.setTextColor(Color.BLACK);
        mAmountTextView.setGravity(Gravity.CENTER);
        
        mAmountSeekBar = new SeekBar(this);
        mAmountSeekBar.setOnSeekBarChangeListener(this);
        mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        mAmountSeekBar.setMax(MAX_VALUE);
        
        mTimeTextView = new TextView(this);
        mTimeTextView.setText(TIME_STRING+mTimeValue);
        mTimeTextView.setTextSize(TITLE_TEXT_SIZE);
        mTimeTextView.setTextColor(Color.BLACK);
        mTimeTextView.setGravity(Gravity.CENTER);
        
        mTimeSeekBar = new SeekBar(this);
        mTimeSeekBar.setOnSeekBarChangeListener(this);
        mTimeSeekBar.setId(TIME_SEEKBAR_RESID);
        mTimeSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mScaleTextView);
        mainLayout.addView(mScaleSeekBar);
        mainLayout.addView(mAngleTextView);
        mainLayout.addView(mAngleSeekBar);
        mainLayout.addView(mStretchTextView);
        mainLayout.addView(mStretchSeekBar);
        mainLayout.addView(mTurbulenceTextView);
        mainLayout.addView(mTurbulenceSeekBar);
        mainLayout.addView(mAmountTextView);
        mainLayout.addView(mAmountSeekBar);
        mainLayout.addView(mTimeTextView);
        mainLayout.addView(mTimeSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SCALE_SEEKBAR_RESID:
            mScaleValue = progress;
            mScaleTextView.setText(SCALE_STRING+mScaleValue);
            break;
        case ANGLE_SEEKBAR_RESID:
            mAngleValue = progress;
            mAngleTextView.setText(ANGLE_STRING+getValue(mAngleValue));
            break;
        case STRETCH_SEEKBAR_RESID:
            mStretchValue = progress;
            mStretchTextView.setText(STRETCH_STRING+mStretchValue);
            break;
        case TURBULENCE_SEEKBAR_RESID:
            mTurbulenceValue = progress;
            mTurbulenceTextView.setText(TURBULENCE_STRING+mTurbulenceValue);
            break;
        case AMOUNT_SEEKBAR_RESID:
            mAmountValue = progress;
            mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
            break;
        case TIME_SEEKBAR_RESID:
            mTimeValue = progress;
            mTimeTextView.setText(TIME_STRING+mTimeValue);
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
                SwimFilter filter = new SwimFilter();
                filter.setScale(mScaleValue + 1);
                filter.setAngle(getValue(mAngleValue));
                filter.setStretch(mStretchValue + 1);
                filter.setTurbulence(mTurbulenceValue + 1);
                filter.setAmount(mAmountValue);
                filter.setTime(mTimeValue);
                mColors = filter.filter(mColors, width, height);

                SwimFilterActivity.this.runOnUiThread(new Runnable() {
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

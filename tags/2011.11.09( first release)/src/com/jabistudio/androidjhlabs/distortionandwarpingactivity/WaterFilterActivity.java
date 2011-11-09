package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.WaterFilter;
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

public class WaterFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Water";
    private static final String CENTERX_STRING = "CENTERX:";
    private static final String CENTERY_STRING = "CENTERY:";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String WAVELENGTH_STRING = "WAVELENGTH:";
    private static final String AMPLITUTE_STRING = "AMPLITUTE:";
    private static final String PHASE_STRING = "PHASE:";
    private static final int MAX_VALUE = 100;
    private static final int MAX_RADIUS_VALUE = 400;
    private static final int MAX_WAVELENGTH_VALUE = 200;
    private static final int MAX_PHASE_VALUE = 624;
    
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final int WAVELENGTH_SEEKBAR_RESID = 21865;
    private static final int AMPLITUTE_SEEKBAR_RESID = 21866;
    private static final int PHASE_SEEKBAR_RESID = 21868;
    
    private SeekBar mCenterXSeekBar;
    private TextView mCenterXTextView;
    private SeekBar mCenterYSeekBar;
    private TextView mCenterYTextView;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mWavelengthSeekBar;
    private TextView mWavelengthTextView;
    private SeekBar mAmplituteSeekBar;
    private TextView mAmplituteTextView;
    private SeekBar mPhaseSeekBar;
    private TextView mPhaseTextView;
    
    private int mCenterXValue;
    private int mCenterYValue;
    private int mRadiusValue;
    private int mWavelengthValue;
    private int mAmplituteValue;
    private int mPhaseValue;
    
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
        mCenterXTextView = new TextView(this);
        mCenterXTextView.setText(CENTERX_STRING+mCenterXValue);
        mCenterXTextView.setTextSize(TITLE_TEXT_SIZE);
        mCenterXTextView.setTextColor(Color.BLACK);
        mCenterXTextView.setGravity(Gravity.CENTER);
        
        mCenterXSeekBar = new SeekBar(this);
        mCenterXSeekBar.setOnSeekBarChangeListener(this);
        mCenterXSeekBar.setId(CENTERX_SEEKBAR_RESID);
        mCenterXSeekBar.setMax(MAX_VALUE);
        mCenterXSeekBar.setProgress(MAX_VALUE/2);
        
        mCenterYTextView = new TextView(this);
        mCenterYTextView.setText(CENTERY_STRING+mCenterYValue);
        mCenterYTextView.setTextSize(TITLE_TEXT_SIZE);
        mCenterYTextView.setTextColor(Color.BLACK);
        mCenterYTextView.setGravity(Gravity.CENTER);
        
        mCenterYSeekBar = new SeekBar(this);
        mCenterYSeekBar.setOnSeekBarChangeListener(this);
        mCenterYSeekBar.setId(CENTERY_SEEKBAR_RESID);
        mCenterYSeekBar.setMax(MAX_VALUE);
        mCenterYSeekBar.setProgress(MAX_VALUE/2);
        
        mRadiusTextView = new TextView(this);
        mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
        mRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mRadiusTextView.setTextColor(Color.BLACK);
        mRadiusTextView.setGravity(Gravity.CENTER);
        
        mRadiusSeekBar = new SeekBar(this);
        mRadiusSeekBar.setOnSeekBarChangeListener(this);
        mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        mRadiusSeekBar.setMax(MAX_RADIUS_VALUE);
        
        mWavelengthTextView = new TextView(this);
        mWavelengthTextView.setText(WAVELENGTH_STRING+mWavelengthValue);
        mWavelengthTextView.setTextSize(TITLE_TEXT_SIZE);
        mWavelengthTextView.setTextColor(Color.BLACK);
        mWavelengthTextView.setGravity(Gravity.CENTER);
        
        mWavelengthSeekBar = new SeekBar(this);
        mWavelengthSeekBar.setOnSeekBarChangeListener(this);
        mWavelengthSeekBar.setId(WAVELENGTH_SEEKBAR_RESID);
        mWavelengthSeekBar.setMax(MAX_WAVELENGTH_VALUE);
        
        mAmplituteTextView = new TextView(this);
        mAmplituteTextView.setText(AMPLITUTE_STRING+mAmplituteValue);
        mAmplituteTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmplituteTextView.setTextColor(Color.BLACK);
        mAmplituteTextView.setGravity(Gravity.CENTER);
        
        mAmplituteSeekBar = new SeekBar(this);
        mAmplituteSeekBar.setOnSeekBarChangeListener(this);
        mAmplituteSeekBar.setId(AMPLITUTE_SEEKBAR_RESID);
        mAmplituteSeekBar.setMax(MAX_VALUE);
        
        mPhaseTextView = new TextView(this);
        mPhaseTextView.setText(PHASE_STRING+mPhaseValue);
        mPhaseTextView.setTextSize(TITLE_TEXT_SIZE);
        mPhaseTextView.setTextColor(Color.BLACK);
        mPhaseTextView.setGravity(Gravity.CENTER);
        
        mPhaseSeekBar = new SeekBar(this);
        mPhaseSeekBar.setOnSeekBarChangeListener(this);
        mPhaseSeekBar.setId(PHASE_SEEKBAR_RESID);
        mPhaseSeekBar.setMax(MAX_PHASE_VALUE);
        
        mainLayout.addView(mCenterXTextView);
        mainLayout.addView(mCenterXSeekBar);
        mainLayout.addView(mCenterYTextView);
        mainLayout.addView(mCenterYSeekBar);
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mWavelengthTextView);
        mainLayout.addView(mWavelengthSeekBar);
        mainLayout.addView(mAmplituteTextView);
        mainLayout.addView(mAmplituteSeekBar);
        mainLayout.addView(mPhaseTextView);
        mainLayout.addView(mPhaseSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case CENTERX_SEEKBAR_RESID:
            mCenterXValue = progress;
            mCenterXTextView.setText(CENTERX_STRING+getValue(mCenterXValue));
            break;
        case CENTERY_SEEKBAR_RESID:
            mCenterYValue = progress;
            mCenterYTextView.setText(CENTERY_STRING+getValue(mCenterYValue));
            break;
        case RADIUS_SEEKBAR_RESID:
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
            break;
        case WAVELENGTH_SEEKBAR_RESID:
            mWavelengthValue = progress;
            mWavelengthTextView.setText(WAVELENGTH_STRING+mWavelengthValue);
            break;
        case AMPLITUTE_SEEKBAR_RESID:
            mAmplituteValue = progress;
            mAmplituteTextView.setText(AMPLITUTE_STRING+getValue(mAmplituteValue));
            break;
        case PHASE_SEEKBAR_RESID:
            mPhaseValue = progress;
            mPhaseTextView.setText(PHASE_STRING+getValue(mPhaseValue));
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
                WaterFilter filter = new WaterFilter();
                filter.setCentreX(getValue(mCenterXValue));
                filter.setCentreY(getValue(mCenterYValue));
                filter.setRadius(mRadiusValue);
                filter.setWavelength(mWavelengthValue);
                filter.setAmplitude(getValue(mAmplituteValue));
                filter.setPhase(getValue(mPhaseValue));
                mColors = filter.filter(mColors, width, height);

                WaterFilterActivity.this.runOnUiThread(new Runnable() {
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

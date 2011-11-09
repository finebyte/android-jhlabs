package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.MarbleFilter;
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

public class KaleidoscopeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Kaleidoscope";
    private static final String CENTERX_STRING = "CENTERX:";
    private static final String CENTERY_STRING = "CENTERY:";
    private static final String ANGLE_STRING = "ANGLE:";
    private static final String ANGLE2_STRING = "ANGLE2:";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String SIDES_STRING = "SIDES:";
    private static final int MAX_VALUE = 100;
    private static final int MAX_ANGLE_VALUE = 314;
    private static final int SIDES_MAX_VALUE = 32;
    
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final int ANGLE_SEEKBAR_RESID = 21865;
    private static final int ANGLE2_SEEKBAR_RESID = 21866;
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final int SIDES_SEEKBAR_RESID = 21868;
    
    private SeekBar mCenterXSeekBar;
    private TextView mCenterXTextView;
    private SeekBar mCenterYSeekBar;
    private TextView mCenterYTextView;
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private SeekBar mAngle2SeekBar;
    private TextView mAngle2TextView;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mSidesSeekBar;
    private TextView mSidesTextView;
    
    private int mCenterXValue;
    private int mCenterYValue;
    private int mAngleValue;
    private int mAngle2Value;
    private int mRadiusValue;
    private int mSidesValue;
    
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
        
        mAngleTextView = new TextView(this);
        mAngleTextView.setText(ANGLE_STRING+mAngleValue);
        mAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mAngleTextView.setTextColor(Color.BLACK);
        mAngleTextView.setGravity(Gravity.CENTER);
        
        mAngleSeekBar = new SeekBar(this);
        mAngleSeekBar.setOnSeekBarChangeListener(this);
        mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        mAngleSeekBar.setMax(MAX_ANGLE_VALUE);
        mAngleSeekBar.setProgress(MAX_ANGLE_VALUE/2);
        
        mAngle2TextView = new TextView(this);
        mAngle2TextView.setText(ANGLE2_STRING+mAngle2Value);
        mAngle2TextView.setTextSize(TITLE_TEXT_SIZE);
        mAngle2TextView.setTextColor(Color.BLACK);
        mAngle2TextView.setGravity(Gravity.CENTER);
        
        mAngle2SeekBar = new SeekBar(this);
        mAngle2SeekBar.setOnSeekBarChangeListener(this);
        mAngle2SeekBar.setId(ANGLE2_SEEKBAR_RESID);
        mAngle2SeekBar.setMax(MAX_ANGLE_VALUE);
        mAngle2SeekBar.setProgress(MAX_ANGLE_VALUE/2);
        
        mRadiusTextView = new TextView(this);
        mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
        mRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mRadiusTextView.setTextColor(Color.BLACK);
        mRadiusTextView.setGravity(Gravity.CENTER);
        
        mRadiusSeekBar = new SeekBar(this);
        mRadiusSeekBar.setOnSeekBarChangeListener(this);
        mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        mRadiusSeekBar.setMax(MAX_VALUE);
        
        mSidesTextView = new TextView(this);
        mSidesTextView.setText(SIDES_STRING+mSidesValue);
        mSidesTextView.setTextSize(TITLE_TEXT_SIZE);
        mSidesTextView.setTextColor(Color.BLACK);
        mSidesTextView.setGravity(Gravity.CENTER);
        
        mSidesSeekBar = new SeekBar(this);
        mSidesSeekBar.setOnSeekBarChangeListener(this);
        mSidesSeekBar.setId(SIDES_SEEKBAR_RESID);
        mSidesSeekBar.setMax(SIDES_MAX_VALUE);
        
        mainLayout.addView(mCenterXTextView);
        mainLayout.addView(mCenterXSeekBar);
        mainLayout.addView(mCenterYTextView);
        mainLayout.addView(mCenterYSeekBar);
        mainLayout.addView(mAngleTextView);
        mainLayout.addView(mAngleSeekBar);
        mainLayout.addView(mAngle2TextView);
        mainLayout.addView(mAngle2SeekBar);
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mSidesTextView);
        mainLayout.addView(mSidesSeekBar);
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
        case ANGLE_SEEKBAR_RESID:
            mAngleValue = progress;
            mAngleTextView.setText(ANGLE_STRING+getAngle(mAngleValue));
            break;
        case ANGLE2_SEEKBAR_RESID:
            mAngle2Value = progress;
            mAngle2TextView.setText(ANGLE2_STRING+getAngle(mAngle2Value));
            break;
        case RADIUS_SEEKBAR_RESID:
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
            break;
        case SIDES_SEEKBAR_RESID:
            mSidesValue = progress;
            mSidesTextView.setText(SIDES_STRING+mSidesValue);
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
                KaleidoscopeFilter filter = new KaleidoscopeFilter();
                filter.setCentreX(getValue(mCenterXValue));
                filter.setCentreY(getValue(mCenterYValue));
                filter.setAngle(getAngle(mAngleValue));
                filter.setAngle2(getAngle(mAngle2Value));
                filter.setRadius(mRadiusValue);
                filter.setSides(mSidesValue);
                mColors = filter.filter(mColors, width, height);

                KaleidoscopeFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getAngle(int value){
        float retValue = 0;
        retValue = (float)((value - 157) / 100f);
        return retValue;
    }
    private float getValue(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

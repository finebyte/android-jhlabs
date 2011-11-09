package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.SphereFilter;
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

public class SphereFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Sphere";
    private static final String CENTERX_STRING = "CENTERX:";
    private static final String CENTERY_STRING = "CENTERY:";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String REFRACTION_STRING = "REFRACTION:";
    private static final int MAX_VALUE = 100;
    private static final int REFRACTION_MAX_VALUE = 200;
    
    private static final int CENTERX_SEEKBAR_RESID = 21863;
    private static final int CENTERY_SEEKBAR_RESID = 21864;
    private static final int RADIUS_SEEKBAR_RESID = 21867;
    private static final int REFRACTION_SEEKBAR_RESID = 21868;
    
    private SeekBar mCenterXSeekBar;
    private TextView mCenterXTextView;
    private SeekBar mCenterYSeekBar;
    private TextView mCenterYTextView;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mRefractionSeekBar;
    private TextView mRefractionTextView;
    
    private int mCenterXValue;
    private int mCenterYValue;
    private int mRadiusValue;
    private int mRefractionValue;
    
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
        mRadiusSeekBar.setMax(MAX_VALUE);
        
        mRefractionTextView = new TextView(this);
        mRefractionTextView.setText(REFRACTION_STRING+mRefractionValue);
        mRefractionTextView.setTextSize(TITLE_TEXT_SIZE);
        mRefractionTextView.setTextColor(Color.BLACK);
        mRefractionTextView.setGravity(Gravity.CENTER);
        
        mRefractionSeekBar = new SeekBar(this);
        mRefractionSeekBar.setOnSeekBarChangeListener(this);
        mRefractionSeekBar.setId(REFRACTION_SEEKBAR_RESID);
        mRefractionSeekBar.setMax(REFRACTION_MAX_VALUE);
        
        mainLayout.addView(mCenterXTextView);
        mainLayout.addView(mCenterXSeekBar);
        mainLayout.addView(mCenterYTextView);
        mainLayout.addView(mCenterYSeekBar);
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mRefractionTextView);
        mainLayout.addView(mRefractionSeekBar);
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
        case REFRACTION_SEEKBAR_RESID:
            mRefractionValue = progress;
            mRefractionTextView.setText(REFRACTION_STRING+getRefractionValue(mRefractionValue));
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
                SphereFilter filter = new SphereFilter();
                filter.setCentreX(getValue(mCenterXValue));
                filter.setCentreY(getValue(mCenterYValue));
                filter.setRadius(mRadiusValue);
                filter.setRefractionIndex(getRefractionValue(mRefractionValue));
                mColors = filter.filter(mColors, width, height);

                SphereFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getRefractionValue(int value){
        float retValue = 0;
        retValue = (float)((value + 100) / 100f);
        return retValue;
    }
    private float getValue(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

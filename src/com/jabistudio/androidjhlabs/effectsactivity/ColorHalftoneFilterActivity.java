package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.BoxBlurFilterActivity;
import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.ColorHalftoneFilter;
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

public class ColorHalftoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "ColorHalftone";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final String CYAN_ANGLE_STRING = "CYANANGLE:";
    private static final String MAGENTA_ANGLE_STRING = "MAGENTAANGLE:";
    private static final String YELLOW_ANGLE_STRING = "YELLOWANGLE:";
    private static final int MAX_VALUE = 30;
    private static final int ANGLE_VALUE = 360;
    
    private static final int RADIUS_SEEKBAR_RESID = 21862;
    private static final int CYAN_ANGLE_SEEKBAR_RESID = 21863;
    private static final int MAGENTA_ANGLE_SEEKBAR_RESID = 21864;
    private static final int YELLOW_ANGLE_SEEKBAR_RESID = 21865;
    
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    private SeekBar mCyanAngleSeekBar;
    private TextView mCyanAngleTextView;
    private SeekBar mMagentaAngleSeekBar;
    private TextView mMagentaAngleTextView;
    private SeekBar mYellowAngleSeekBar;
    private TextView mYellowAngleTextView;
    
    private int mRadiusValue;
    private int mCyanAngleValue;
    private int mMagentaAngleValue;
    private int mYellowAngleValue;
    
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
        mRadiusTextView = new TextView(this);
        mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
        mRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mRadiusTextView.setTextColor(Color.BLACK);
        mRadiusTextView.setGravity(Gravity.CENTER);
        
        mRadiusSeekBar = new SeekBar(this);
        mRadiusSeekBar.setOnSeekBarChangeListener(this);
        mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        mRadiusSeekBar.setMax(MAX_VALUE);
        
        mCyanAngleTextView = new TextView(this);
        mCyanAngleTextView.setText(CYAN_ANGLE_STRING+mCyanAngleValue);
        mCyanAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mCyanAngleTextView.setTextColor(Color.BLACK);
        mCyanAngleTextView.setGravity(Gravity.CENTER);
        
        mCyanAngleSeekBar = new SeekBar(this);
        mCyanAngleSeekBar.setOnSeekBarChangeListener(this);
        mCyanAngleSeekBar.setId(CYAN_ANGLE_SEEKBAR_RESID);
        mCyanAngleSeekBar.setMax(ANGLE_VALUE);
        
        mMagentaAngleTextView = new TextView(this);
        mMagentaAngleTextView.setText(MAGENTA_ANGLE_STRING+mMagentaAngleValue);
        mMagentaAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mMagentaAngleTextView.setTextColor(Color.BLACK);
        mMagentaAngleTextView.setGravity(Gravity.CENTER);
        
        mMagentaAngleSeekBar = new SeekBar(this);
        mMagentaAngleSeekBar.setOnSeekBarChangeListener(this);
        mMagentaAngleSeekBar.setId(MAGENTA_ANGLE_SEEKBAR_RESID);
        mMagentaAngleSeekBar.setMax(ANGLE_VALUE);
        
        mYellowAngleTextView = new TextView(this);
        mYellowAngleTextView.setText(YELLOW_ANGLE_STRING+mYellowAngleValue);
        mYellowAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mYellowAngleTextView.setTextColor(Color.BLACK);
        mYellowAngleTextView.setGravity(Gravity.CENTER);
        
        mYellowAngleSeekBar = new SeekBar(this);
        mYellowAngleSeekBar.setOnSeekBarChangeListener(this);
        mYellowAngleSeekBar.setId(YELLOW_ANGLE_SEEKBAR_RESID);
        mYellowAngleSeekBar.setMax(ANGLE_VALUE);
        
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
        mainLayout.addView(mCyanAngleTextView);
        mainLayout.addView(mCyanAngleSeekBar);
        mainLayout.addView(mMagentaAngleTextView);
        mainLayout.addView(mMagentaAngleSeekBar);
        mainLayout.addView(mYellowAngleTextView);
        mainLayout.addView(mYellowAngleSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case RADIUS_SEEKBAR_RESID:
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
            break;
        case CYAN_ANGLE_SEEKBAR_RESID:
            mCyanAngleValue = progress;
            mCyanAngleTextView.setText(CYAN_ANGLE_STRING+mCyanAngleValue);
            break;
        case MAGENTA_ANGLE_SEEKBAR_RESID:
            mMagentaAngleValue = progress;
            mMagentaAngleTextView.setText(MAGENTA_ANGLE_STRING+mMagentaAngleValue);
            break;
        case YELLOW_ANGLE_SEEKBAR_RESID:
            mYellowAngleValue = progress;
            mYellowAngleTextView.setText(YELLOW_ANGLE_STRING+mYellowAngleValue);
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
                ColorHalftoneFilter filter = new ColorHalftoneFilter();
                filter.setdotRadius(mRadiusValue);
                filter.setCyanScreenAngle(mCyanAngleValue);
                filter.setMagentaScreenAngle(mMagentaAngleValue);
                filter.setYellowScreenAngle(mYellowAngleValue);
               
                mColors = filter.filter(mColors, width, height);

                ColorHalftoneFilterActivity.this.runOnUiThread(new Runnable() {
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

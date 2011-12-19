package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.coloradjustmentfilteractivity.ChannelMixFilterActivity;
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

public class ShadowFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Shadow";
    private static final String ANGLE_STRING = "ANGLE:";
    private static final String DISTANCE_STRING = "DISTANCE:";
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String OPACITY_STRING = "OPACITY:";
    private static final int MAX_VALUE = 100;
    
    private static final int ANGLE_SEEKBAR_RESID = 21863;
    private static final int DISTANCE_SEEKBAR_RESID = 21864;
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final int OPACITY_SEEKBAR_RESID = 21866;
    
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    private SeekBar mDistanceSeekBar;
    private TextView mDistanceTextView;
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private SeekBar mOpacitySeekBar;
    private TextView mOpacityTextView;
    
    private int mAngleValue;
    private int mDistanceValue;
    private int mSoftnessValue;
    private int mOpacityValue;
    
    private ProgressDialog mProgressDialog;
    private int[] mColors;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        view.setText(R.string.none_filter);
        setContentView(view);
        /*
        setTitle(TITLE);
        filterSeekBarSetup(mMainLayout);
        */
    }
    
    /**
     * filterButtonSetting
     * @param mainLayout
     */
    private void filterSeekBarSetup(LinearLayout mainLayout){
        mAngleTextView = new TextView(this);
        mAngleTextView.setText(ANGLE_STRING+mAngleValue);
        mAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mAngleTextView.setTextColor(Color.BLACK);
        mAngleTextView.setGravity(Gravity.CENTER);
        
        mAngleSeekBar = new SeekBar(this);
        mAngleSeekBar.setOnSeekBarChangeListener(this);
        mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        mAngleSeekBar.setMax(MAX_VALUE);
        
        mDistanceTextView = new TextView(this);
        mDistanceTextView.setText(DISTANCE_STRING+mDistanceValue);
        mDistanceTextView.setTextSize(TITLE_TEXT_SIZE);
        mDistanceTextView.setTextColor(Color.BLACK);
        mDistanceTextView.setGravity(Gravity.CENTER);
        
        mDistanceSeekBar = new SeekBar(this);
        mDistanceSeekBar.setOnSeekBarChangeListener(this);
        mDistanceSeekBar.setId(DISTANCE_SEEKBAR_RESID);
        mDistanceSeekBar.setMax(MAX_VALUE);
        
        mSoftnessTextView = new TextView(this);
        mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
        mSoftnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mSoftnessTextView.setTextColor(Color.BLACK);
        mSoftnessTextView.setGravity(Gravity.CENTER);
        
        mSoftnessSeekBar = new SeekBar(this);
        mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        mSoftnessSeekBar.setMax(MAX_VALUE);
        
        mOpacityTextView = new TextView(this);
        mOpacityTextView.setText(OPACITY_STRING+mOpacityValue);
        mOpacityTextView.setTextSize(TITLE_TEXT_SIZE);
        mOpacityTextView.setTextColor(Color.BLACK);
        mOpacityTextView.setGravity(Gravity.CENTER);
        
        mOpacitySeekBar = new SeekBar(this);
        mOpacitySeekBar.setOnSeekBarChangeListener(this);
        mOpacitySeekBar.setId(OPACITY_SEEKBAR_RESID);
        mOpacitySeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mAngleTextView);
        mainLayout.addView(mAngleSeekBar);
        mainLayout.addView(mDistanceTextView);
        mainLayout.addView(mDistanceSeekBar);
        mainLayout.addView(mSoftnessTextView);
        mainLayout.addView(mSoftnessSeekBar);
        mainLayout.addView(mOpacityTextView);
        mainLayout.addView(mOpacitySeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case ANGLE_SEEKBAR_RESID:
            mAngleValue = progress;
            mAngleTextView.setText(ANGLE_STRING+mAngleValue);
            break;
        case DISTANCE_SEEKBAR_RESID:
            mDistanceValue = progress;
            mDistanceTextView.setText(DISTANCE_STRING+mDistanceValue);
            break;
        case SOFTNESS_SEEKBAR_RESID:
            mSoftnessValue = progress;
            mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
            break;
        case OPACITY_SEEKBAR_RESID:
            mOpacityValue = progress;
            mOpacityTextView.setText(OPACITY_STRING+mOpacityValue);
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
                /*
                ShadowFilter filter = new ShadowFilter();

                mColors = filter.filter(mColors, width, height);

                ShadowFilterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setModifyView(mColors, width, height);
                    }
                });
                mProgressDialog.dismiss();
                */
            }
        };
        thread.setDaemon(true);
        thread.start();
    }
}

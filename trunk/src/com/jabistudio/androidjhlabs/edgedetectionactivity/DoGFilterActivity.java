package com.jabistudio.androidjhlabs.edgedetectionactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.coloradjustmentfilteractivity.ChannelMixFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.DoGFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DoGFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener,OnCheckedChangeListener{
    private static final String TITLE = "DoG";
    private static final String RADIUS1_STRING = "RADIUS1:";
    private static final String RADIUS2_STRING = "RADIUS2:";
    private static final String INVERT_STRING = "INVERT:";
    private static final String NORMALIZE_STRING = "NORMALIZE:";
    private static final int MAX_VALUE = 1000;
    
    private static final int RADIUS1_SEEKBAR_RESID = 21863;
    private static final int RADIUS2_SEEKBAR_RESID = 21864;
    private static final int INVERT_CHECKBOX_RESID = 21865;
    private static final int NORMALIZE_CHECKBOX_RESID = 21866;
    
    private SeekBar mRadius1SeekBar;
    private TextView mRadius1TextView;
    private SeekBar mRadius2SeekBar;
    private TextView mRadius2TextView;
    private CheckBox mInvertCheckBox;
    private CheckBox mNormalizeCheckBox;
    
    private int mRadius1Value;
    private int mRadius2Value;
    private boolean mIsInvert;
    private boolean mIsNormalize;
    
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
        mRadius1TextView = new TextView(this);
        mRadius1TextView.setText(RADIUS1_STRING+mRadius1Value);
        mRadius1TextView.setTextSize(TITLE_TEXT_SIZE);
        mRadius1TextView.setTextColor(Color.BLACK);
        mRadius1TextView.setGravity(Gravity.CENTER);
        
        mRadius1SeekBar = new SeekBar(this);
        mRadius1SeekBar.setOnSeekBarChangeListener(this);
        mRadius1SeekBar.setId(RADIUS1_SEEKBAR_RESID);
        mRadius1SeekBar.setMax(MAX_VALUE);
        
        mRadius2TextView = new TextView(this);
        mRadius2TextView.setText(RADIUS2_STRING+mRadius2Value);
        mRadius2TextView.setTextSize(TITLE_TEXT_SIZE);
        mRadius2TextView.setTextColor(Color.BLACK);
        mRadius2TextView.setGravity(Gravity.CENTER);
        
        mRadius2SeekBar = new SeekBar(this);
        mRadius2SeekBar.setOnSeekBarChangeListener(this);
        mRadius2SeekBar.setId(RADIUS2_SEEKBAR_RESID);
        mRadius2SeekBar.setMax(MAX_VALUE);
        
        mInvertCheckBox = new CheckBox(this);
        mInvertCheckBox.setText(INVERT_STRING);
        mInvertCheckBox.setTextSize(TITLE_TEXT_SIZE);
        mInvertCheckBox.setTextColor(Color.BLACK);
        mInvertCheckBox.setGravity(Gravity.CENTER);
        mInvertCheckBox.setOnCheckedChangeListener(this);
        mInvertCheckBox.setId(INVERT_CHECKBOX_RESID);
        
        mNormalizeCheckBox = new CheckBox(this);
        mNormalizeCheckBox.setText(NORMALIZE_STRING);
        mNormalizeCheckBox.setTextSize(TITLE_TEXT_SIZE);
        mNormalizeCheckBox.setTextColor(Color.BLACK);
        mNormalizeCheckBox.setGravity(Gravity.CENTER);
        mNormalizeCheckBox.setOnCheckedChangeListener(this);
        mNormalizeCheckBox.setId(NORMALIZE_CHECKBOX_RESID);
        
        mainLayout.addView(mRadius1TextView);
        mainLayout.addView(mRadius1SeekBar);
        mainLayout.addView(mRadius2TextView);
        mainLayout.addView(mRadius2SeekBar);
        mainLayout.addView(mInvertCheckBox);
        mainLayout.addView(mNormalizeCheckBox);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case RADIUS1_SEEKBAR_RESID:
            mRadius1Value = progress;
            mRadius1TextView.setText(RADIUS1_STRING+getAmout(mRadius1Value));
            break;
        case RADIUS2_SEEKBAR_RESID:
            mRadius2Value = progress;
            mRadius2TextView.setText(RADIUS2_STRING+getAmout(mRadius2Value));
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        applyFilter();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
        case INVERT_CHECKBOX_RESID:
            mIsInvert = isChecked;
            applyFilter();
            break;
        case NORMALIZE_CHECKBOX_RESID:
            mIsNormalize = isChecked;
            applyFilter();
            break;
        }
    }
    private void applyFilter(){
        final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = mOriginalImageView.getDrawable().getIntrinsicHeight();
        
        mColors = AndroidUtils.drawableToIntArray(mOriginalImageView.getDrawable());
        mProgressDialog = ProgressDialog.show(this, "", "Wait......");
        
        Thread thread = new Thread(){
            public void run() {
                DoGFilter filter = new DoGFilter();
                filter.setInvert(mIsInvert);
                filter.setNormalize(mIsNormalize);
                filter.setRadius1(getAmout(mRadius1Value));
                filter.setRadius2(getAmout(mRadius2Value));

                mColors = filter.filter(mColors, width, height);

                DoGFilterActivity.this.runOnUiThread(new Runnable() {
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

package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.HalftoneFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class HalftoneFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener,OnCheckedChangeListener{
    private static final String TITLE = "Halftone";
    private static final String SOFTNESS_STRING = "SOFTNESS:";
    private static final String INVERT_STRING = "INVERT:";
    private static final String MONOCHROME_STRING = "MONOCHROME:";
    private static final int MAX_VALUE = 100;
    
    private static final int SOFTNESS_SEEKBAR_RESID = 21865;
    private static final int INVERT_CHECKBOX_RESID = 21866;
    private static final int MONOCHROME_CHECKBOX_RESID = 21867;
    
    private SeekBar mSoftnessSeekBar;
    private TextView mSoftnessTextView;
    private CheckBox mInvertCheckBox;
    private CheckBox mMonochromeCheckBox;
    
    private int mSoftnessValue;
    private boolean mInvertValue;
    private boolean mMonochromeValue;
    
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
        mSoftnessTextView = new TextView(this);
        mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
        mSoftnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mSoftnessTextView.setTextColor(Color.BLACK);
        mSoftnessTextView.setGravity(Gravity.CENTER);
        
        mSoftnessSeekBar = new SeekBar(this);
        mSoftnessSeekBar.setOnSeekBarChangeListener(this);
        mSoftnessSeekBar.setId(SOFTNESS_SEEKBAR_RESID);
        mSoftnessSeekBar.setMax(MAX_VALUE);
        mSoftnessSeekBar.setProgress(MAX_VALUE/2);
        
        mInvertCheckBox = new CheckBox(this);
        mInvertCheckBox.setText(INVERT_STRING);
        mInvertCheckBox.setTextSize(TITLE_TEXT_SIZE);
        mInvertCheckBox.setTextColor(Color.BLACK);
        mInvertCheckBox.setGravity(Gravity.CENTER);
        mInvertCheckBox.setOnCheckedChangeListener(this);
        mInvertCheckBox.setId(INVERT_CHECKBOX_RESID);
        
        mMonochromeCheckBox = new CheckBox(this);
        mMonochromeCheckBox.setText(MONOCHROME_STRING);
        mMonochromeCheckBox.setTextSize(TITLE_TEXT_SIZE);
        mMonochromeCheckBox.setTextColor(Color.BLACK);
        mMonochromeCheckBox.setGravity(Gravity.CENTER);
        mMonochromeCheckBox.setOnCheckedChangeListener(this);
        mMonochromeCheckBox.setId(MONOCHROME_CHECKBOX_RESID);
        
        mainLayout.addView(mSoftnessTextView);
        mainLayout.addView(mSoftnessSeekBar);
        mainLayout.addView(mInvertCheckBox);
        mainLayout.addView(mMonochromeCheckBox);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SOFTNESS_SEEKBAR_RESID:
            mSoftnessValue = progress;
            mSoftnessTextView.setText(SOFTNESS_STRING+mSoftnessValue);
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
    
    private void applyFilter(){
        final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = mOriginalImageView.getDrawable().getIntrinsicHeight();
        
        mColors = AndroidUtils.drawableToIntArray(mOriginalImageView.getDrawable());
        mProgressDialog = ProgressDialog.show(this, "", "Wait......");
        
        Thread thread = new Thread(){
            public void run() {
                HalftoneFilter filter = new HalftoneFilter();
                Drawable d = HalftoneFilterActivity.this.getResources().getDrawable(R.drawable.halftone1);
                filter.setMask(AndroidUtils.drawableToIntArray(d));
                filter.setMaskWidth(220);
                filter.setMaskHeight(220);
                filter.setSoftness(getValue(mSoftnessValue));
                filter.setInvert(mInvertValue);
                filter.setMonochrome(mMonochromeValue);

                mColors = filter.filter(mColors, width, height);
                HalftoneFilterActivity.this.runOnUiThread(new Runnable() {
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
        retValue = (float)((value) / 100f);
        return retValue;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch(buttonView.getId()){
        case INVERT_CHECKBOX_RESID:
            mInvertValue = isChecked;
            applyFilter();
            break;
        case MONOCHROME_CHECKBOX_RESID:
            mMonochromeValue = isChecked;
            applyFilter();
            break;
        }
    }
}

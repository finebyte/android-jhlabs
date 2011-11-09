package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.BicubicScaleFilter;
import com.jabistudio.androidjhlabs.filter.RotateFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class BicubicScaleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "BicubicScale";
    private static final String WIDTH_STRING = "WIDTH:";
    private static final String HEIGHT_STRING = "HEIGHT:";
    
    private static final int WIDTH_SEEKBAR_RESID = 21865;
    private static final int HEIGHT_SEEKBAR_RESID = 21867;
    
    private SeekBar mWidthSeekBar;
    private TextView mWidthTextView;
    private SeekBar mHeightSeekBar;
    private TextView mHeightTextView;
    
    private int mMaxWidth;
    private int mMaxHeight;
    
    private int mWidthValue;
    private int mHeightValue;
    
    private ProgressDialog mProgressDialog;
    private int[] mColors;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        mMaxWidth = AndroidUtils.getBitmapOfWidth(getResources(), R.drawable.image);
        mMaxHeight = AndroidUtils.getBitmapOfHeight(getResources(), R.drawable.image);
        filterSeekBarSetup(mMainLayout);
    }

    /**
     * filterButtonSetting
     * @param mainLayout
     */
    private void filterSeekBarSetup(LinearLayout mainLayout){
        mWidthTextView = new TextView(this);
        mWidthTextView.setText(WIDTH_STRING+mWidthValue);
        mWidthTextView.setTextSize(TITLE_TEXT_SIZE);
        mWidthTextView.setTextColor(Color.BLACK);
        mWidthTextView.setGravity(Gravity.CENTER);
        
        mWidthSeekBar = new SeekBar(this);
        mWidthSeekBar.setOnSeekBarChangeListener(this);
        mWidthSeekBar.setId(WIDTH_SEEKBAR_RESID);
        mWidthSeekBar.setMax(mMaxWidth * 2);
        mWidthSeekBar.setProgress(mMaxWidth);
        
        mHeightTextView = new TextView(this);
        mHeightTextView.setText(HEIGHT_STRING+mHeightValue);
        mHeightTextView.setTextSize(TITLE_TEXT_SIZE);
        mHeightTextView.setTextColor(Color.BLACK);
        mHeightTextView.setGravity(Gravity.CENTER);
        
        mHeightSeekBar = new SeekBar(this);
        mHeightSeekBar.setOnSeekBarChangeListener(this);
        mHeightSeekBar.setId(HEIGHT_SEEKBAR_RESID);
        mHeightSeekBar.setMax(mMaxHeight * 2);
        mHeightSeekBar.setProgress(mMaxHeight);
        
        mainLayout.addView(mWidthTextView);
        mainLayout.addView(mWidthSeekBar);
        mainLayout.addView(mHeightTextView);
        mainLayout.addView(mHeightSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case WIDTH_SEEKBAR_RESID:
            mWidthValue = progress;
            mWidthTextView.setText(WIDTH_STRING+getValue(mWidthValue));
            break;
        case HEIGHT_SEEKBAR_RESID:
            mHeightValue = progress;
            mHeightTextView.setText(HEIGHT_STRING+getValue(mHeightValue));
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
                BicubicScaleFilter filter = new BicubicScaleFilter(getValue(mWidthValue), getValue(mHeightValue));
                mColors = filter.filter(mColors, width, height);
                BicubicScaleFilterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setModifyView(mColors, getValue(mWidthValue), getValue(mHeightValue));
                    }
                });
                mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();        
    }
    private int getValue(int value){
        if(value <= 0){
            value = 1;
        }
        return value;
    }
}

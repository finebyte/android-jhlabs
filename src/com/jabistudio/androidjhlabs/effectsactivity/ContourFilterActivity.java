package com.jabistudio.androidjhlabs.effectsactivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ContourFilter;
import com.jabistudio.androidjhlabs.filter.CrystallizeFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class ContourFilterActivity  extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Contour";
    private static final String LEVEL_STRING = "LEVEL:";
    private static final String OFFSET_STRING = "OFFSET:";
    private static final String SCALE_STRING = "SCALE:";
    private static final int LEVEL_MAX_VALUE = 30;
    private static final int MAX_VALUE = 100;
    
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    private static final int OFFSET_SEEKBAR_RESID = 21866;
    private static final int SCALE_SEEKBAR_RESID = 21867;
    
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    private SeekBar mOffsetSeekBar;
    private TextView mOffsetTextView;
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    
    private int mLevelValue;
    private int mOffsetValue;
    private int mScaleValue;
    
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
        mLevelTextView = new TextView(this);
        mLevelTextView.setText(LEVEL_STRING+mLevelValue);
        mLevelTextView.setTextSize(TITLE_TEXT_SIZE);
        mLevelTextView.setTextColor(Color.BLACK);
        mLevelTextView.setGravity(Gravity.CENTER);
        
        mLevelSeekBar = new SeekBar(this);
        mLevelSeekBar.setOnSeekBarChangeListener(this);
        mLevelSeekBar.setId(LEVEL_SEEKBAR_RESID);
        mLevelSeekBar.setMax(LEVEL_MAX_VALUE);
        
        mOffsetTextView = new TextView(this);
        mOffsetTextView.setText(OFFSET_STRING+mOffsetValue);
        mOffsetTextView.setTextSize(TITLE_TEXT_SIZE);
        mOffsetTextView.setTextColor(Color.BLACK);
        mOffsetTextView.setGravity(Gravity.CENTER);
        
        mOffsetSeekBar = new SeekBar(this);
        mOffsetSeekBar.setOnSeekBarChangeListener(this);
        mOffsetSeekBar.setId(OFFSET_SEEKBAR_RESID);
        mOffsetSeekBar.setMax(MAX_VALUE);
        
        mScaleTextView = new TextView(this);
        mScaleTextView.setText(SCALE_STRING+mScaleValue);
        mScaleTextView.setTextSize(TITLE_TEXT_SIZE);
        mScaleTextView.setTextColor(Color.BLACK);
        mScaleTextView.setGravity(Gravity.CENTER);
        
        mScaleSeekBar = new SeekBar(this);
        mScaleSeekBar.setOnSeekBarChangeListener(this);
        mScaleSeekBar.setId(SCALE_SEEKBAR_RESID);
        mScaleSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mLevelTextView);
        mainLayout.addView(mLevelSeekBar);
        mainLayout.addView(mOffsetTextView);
        mainLayout.addView(mOffsetSeekBar);
        mainLayout.addView(mScaleTextView);
        mainLayout.addView(mScaleSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case LEVEL_SEEKBAR_RESID:
            mLevelValue = progress;
            mLevelTextView.setText(LEVEL_STRING+mLevelValue);
            break;
        case OFFSET_SEEKBAR_RESID:
            mOffsetValue = progress;
            mOffsetTextView.setText(OFFSET_STRING+getValue(mOffsetValue));
            break;
        case SCALE_SEEKBAR_RESID:
            mScaleValue = progress;
            mScaleTextView.setText(SCALE_STRING+getValue(mScaleValue));
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
                ContourFilter filter = new ContourFilter();
                filter.setLevels(mLevelValue);
                filter.setOffset(getValue(mOffsetValue));
                filter.setScale(getValue(mScaleValue));

                mColors = filter.filter(mColors, width, height);
                ContourFilterActivity.this.runOnUiThread(new Runnable() {
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

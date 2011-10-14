package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.RescaleFilter;
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

public class RescaleFilterActivity  extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Rescale";
    private static final String SCALE_STRING = "SCALE:";
    private static final int MAX_VALUE = 500;
    
    private static final int SCALE_SEEKBAR_RESID = 21865;
    
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    
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
        mScaleTextView = new TextView(this);
        mScaleTextView.setText(SCALE_STRING+mScaleValue);
        mScaleTextView.setTextSize(TITLE_TEXT_SIZE);
        mScaleTextView.setTextColor(Color.BLACK);
        mScaleTextView.setGravity(Gravity.CENTER);
        
        mScaleSeekBar = new SeekBar(this);
        mScaleSeekBar.setOnSeekBarChangeListener(this);
        mScaleSeekBar.setId(SCALE_SEEKBAR_RESID);
        mScaleSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mScaleTextView);
        mainLayout.addView(mScaleSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SCALE_SEEKBAR_RESID:
            mScaleValue = progress;
            mScaleTextView.setText(SCALE_STRING+getScale(mScaleValue));
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
                RescaleFilter filter = new RescaleFilter();
                filter.setScale(getScale(mScaleValue));

                mColors = filter.filter(mColors, width, height);
                RescaleFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getScale(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

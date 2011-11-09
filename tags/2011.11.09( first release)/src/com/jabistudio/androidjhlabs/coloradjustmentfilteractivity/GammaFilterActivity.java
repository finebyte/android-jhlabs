package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.jabistudio.androidjhlabs.filter.GammaFilter;
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

public class GammaFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Gamma";
    private static final String GAMMA_STRING = "GAMMA:";
    private static final int MAX_VALUE = 500;
    
    private static final int GAMMA_SEEKBAR_RESID = 21865;
    
    private SeekBar mGammaSeekBar;
    private TextView mGammaTextView;
    
    private int mGammaValue;
    
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
        mGammaTextView = new TextView(this);
        mGammaTextView.setText(GAMMA_STRING+mGammaValue);
        mGammaTextView.setTextSize(TITLE_TEXT_SIZE);
        mGammaTextView.setTextColor(Color.BLACK);
        mGammaTextView.setGravity(Gravity.CENTER);
        
        mGammaSeekBar = new SeekBar(this);
        mGammaSeekBar.setOnSeekBarChangeListener(this);
        mGammaSeekBar.setId(GAMMA_SEEKBAR_RESID);
        mGammaSeekBar.setMax(MAX_VALUE);
        mGammaSeekBar.setProgress(100);
        
        mainLayout.addView(mGammaTextView);
        mainLayout.addView(mGammaSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case GAMMA_SEEKBAR_RESID:
            mGammaValue = progress;
            mGammaTextView.setText(GAMMA_STRING+getGamma(mGammaValue));
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
                GammaFilter filter = new GammaFilter();
                filter.setGamma(getGamma(mGammaValue));

                mColors = filter.filter(mColors, width, height);
                GammaFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getGamma(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

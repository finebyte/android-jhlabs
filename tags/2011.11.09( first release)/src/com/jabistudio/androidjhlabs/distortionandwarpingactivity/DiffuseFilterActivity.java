package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.effectsactivity.NoiseFilterActivity;
import com.jabistudio.androidjhlabs.filter.DiffuseFilter;
import com.jabistudio.androidjhlabs.filter.NoiseFilter;
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

public class DiffuseFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Diffuse";
    private static final String SCALE_STRING = "SCALE:";
    private static final int MAX_VALUE = 100;
    
    private static final int SCALE_SEEKBAR_RESID = 21863;
    
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    
    private int mScaleValue;
    
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
            mScaleTextView.setText(SCALE_STRING+mScaleValue);
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
                DiffuseFilter filter = new DiffuseFilter();
                filter.setScale(mScaleValue);
                mColors = filter.filter(mColors, width, height);

                DiffuseFilterActivity.this.runOnUiThread(new Runnable() {
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

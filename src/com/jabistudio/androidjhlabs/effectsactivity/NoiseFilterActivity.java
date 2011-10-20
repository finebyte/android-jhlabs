package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.NoiseFilter;
import com.jabistudio.androidjhlabs.filter.StampFilter;
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

public class NoiseFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Noise";
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String DENSITY_STRING = "DENSITY:";
    private static final int MAX_VALUE = 100;
    
    private static final int AMOUNT_SEEKBAR_RESID = 21863;
    private static final int DENSITY_SEEKBAR_RESID = 21864;
    
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private SeekBar mDensitySeekBar;
    private TextView mDensityTextView;
    
    private int mAmountValue;
    private int mDensityValue;
    
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
        mAmountTextView = new TextView(this);
        mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
        mAmountTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmountTextView.setTextColor(Color.BLACK);
        mAmountTextView.setGravity(Gravity.CENTER);
        
        mAmountSeekBar = new SeekBar(this);
        mAmountSeekBar.setOnSeekBarChangeListener(this);
        mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        mAmountSeekBar.setMax(MAX_VALUE);
        
        mDensityTextView = new TextView(this);
        mDensityTextView.setText(DENSITY_STRING+mDensityValue);
        mDensityTextView.setTextSize(TITLE_TEXT_SIZE);
        mDensityTextView.setTextColor(Color.BLACK);
        mDensityTextView.setGravity(Gravity.CENTER);
        
        mDensitySeekBar = new SeekBar(this);
        mDensitySeekBar.setOnSeekBarChangeListener(this);
        mDensitySeekBar.setId(DENSITY_SEEKBAR_RESID);
        mDensitySeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mAmountTextView);
        mainLayout.addView(mAmountSeekBar);
        mainLayout.addView(mDensityTextView);
        mainLayout.addView(mDensitySeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case AMOUNT_SEEKBAR_RESID:
            mAmountValue = progress;
            mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
            break;
        case DENSITY_SEEKBAR_RESID:
            mDensityValue = progress;
            mDensityTextView.setText(DENSITY_STRING+mDensityValue);
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
                NoiseFilter filter = new NoiseFilter();
                filter.setAmount(mAmountValue);
                filter.setDensity(mDensityValue);
                mColors = filter.filter(mColors, width, height);

                NoiseFilterActivity.this.runOnUiThread(new Runnable() {
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

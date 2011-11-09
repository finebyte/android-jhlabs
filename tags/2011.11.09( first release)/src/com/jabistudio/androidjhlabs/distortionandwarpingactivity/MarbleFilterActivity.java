package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.BoxBlurFilterActivity;
import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.MarbleFilter;
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

public class MarbleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Marble";
    private static final String SCALE_STRING = "SCALE:";
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String TURBULENCE_STRING = "TURBULENCE:";
    private static final int MAX_VALUE = 100;
    
    private static final int SCALE_SEEKBAR_RESID = 21863;
    private static final int AMOUNT_SEEKBAR_RESID = 21864;
    private static final int TURBULENCE_SEEKBAR_RESID = 21865;
    
    private SeekBar mScaleSeekBar;
    private TextView mScaleTextView;
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private SeekBar mTurbulenceSeekBar;
    private TextView mTurbulenceTextView;
    
    private int mScaleValue;
    private int mAmountValue;
    private int mTurbulenceValue;
    
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
        
        mAmountTextView = new TextView(this);
        mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
        mAmountTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmountTextView.setTextColor(Color.BLACK);
        mAmountTextView.setGravity(Gravity.CENTER);
        
        mAmountSeekBar = new SeekBar(this);
        mAmountSeekBar.setOnSeekBarChangeListener(this);
        mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        mAmountSeekBar.setMax(MAX_VALUE);
        
        mTurbulenceTextView = new TextView(this);
        mTurbulenceTextView.setText(TURBULENCE_STRING+mTurbulenceValue);
        mTurbulenceTextView.setTextSize(TITLE_TEXT_SIZE);
        mTurbulenceTextView.setTextColor(Color.BLACK);
        mTurbulenceTextView.setGravity(Gravity.CENTER);
        
        mTurbulenceSeekBar = new SeekBar(this);
        mTurbulenceSeekBar.setOnSeekBarChangeListener(this);
        mTurbulenceSeekBar.setId(TURBULENCE_SEEKBAR_RESID);
        mTurbulenceSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mScaleTextView);
        mainLayout.addView(mScaleSeekBar);
        mainLayout.addView(mAmountTextView);
        mainLayout.addView(mAmountSeekBar);
        mainLayout.addView(mTurbulenceTextView);
        mainLayout.addView(mTurbulenceSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SCALE_SEEKBAR_RESID:
            mScaleValue = progress;
            mScaleTextView.setText(SCALE_STRING+mScaleValue);
            break;
        case AMOUNT_SEEKBAR_RESID:
            mAmountValue = progress;
            mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
            break;
        case TURBULENCE_SEEKBAR_RESID:
            mTurbulenceValue = progress;
            mTurbulenceTextView.setText(TURBULENCE_STRING+mTurbulenceValue);
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
                MarbleFilter filter = new MarbleFilter();
                filter.setXScale(mScaleValue);
                filter.setYScale(mScaleValue);
                filter.setAmount(mAmountValue);
                filter.setTurbulence(mTurbulenceValue);
                mColors = filter.filter(mColors, width, height);

                MarbleFilterActivity.this.runOnUiThread(new Runnable() {
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

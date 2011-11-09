package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ChannelMixFilter;
import com.jabistudio.androidjhlabs.filter.Curve;
import com.jabistudio.androidjhlabs.filter.CurvesFilter;
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

public class CurvesFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Curves";
    private static final String KX_STRING = "KX:";
    private static final String KY_STRING = "KY:";
    private static final int MAX_VALUE = 100;
    
    private static final int KX_SEEKBAR_RESID = 21863;
    private static final int KY_SEEKBAR_RESID = 21864;
    
    private SeekBar mKXSeekBar;
    private TextView mKXTextView;
    private SeekBar mKYSeekBar;
    private TextView mKYTextView;
    
    private int mKXValue;
    private int mKYValue;
    
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
        mKXTextView = new TextView(this);
        mKXTextView.setText(KX_STRING+mKXValue);
        mKXTextView.setTextSize(TITLE_TEXT_SIZE);
        mKXTextView.setTextColor(Color.BLACK);
        mKXTextView.setGravity(Gravity.CENTER);
        
        mKXSeekBar = new SeekBar(this);
        mKXSeekBar.setOnSeekBarChangeListener(this);
        mKXSeekBar.setId(KX_SEEKBAR_RESID);
        mKXSeekBar.setMax(MAX_VALUE);
        
        mKYTextView = new TextView(this);
        mKYTextView.setText(KY_STRING+mKYValue);
        mKYTextView.setTextSize(TITLE_TEXT_SIZE);
        mKYTextView.setTextColor(Color.BLACK);
        mKYTextView.setGravity(Gravity.CENTER);
        
        mKYSeekBar = new SeekBar(this);
        mKYSeekBar.setOnSeekBarChangeListener(this);
        mKYSeekBar.setId(KY_SEEKBAR_RESID);
        mKYSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mKXTextView);
        mainLayout.addView(mKXSeekBar);
        mainLayout.addView(mKYTextView);
        mainLayout.addView(mKYSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case KX_SEEKBAR_RESID:
            mKXValue = progress;
            mKXTextView.setText(KX_STRING+getValue(mKXValue));
            break;
        case KY_SEEKBAR_RESID:
            mKYValue = progress;
            mKYTextView.setText(KY_STRING+getValue(mKYValue));
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
                CurvesFilter filter = new CurvesFilter();
                Curve curve = new Curve();
                curve.addKnot(getValue(mKXValue), getValue(mKYValue));
                filter.setCurve(curve);
                mColors = filter.filter(mColors, width, height);

                CurvesFilterActivity.this.runOnUiThread(new Runnable() {
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

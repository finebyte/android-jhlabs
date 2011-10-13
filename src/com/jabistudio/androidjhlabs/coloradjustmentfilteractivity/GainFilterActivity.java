package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.ExposureFilter;
import com.jabistudio.androidjhlabs.filter.GainFilter;
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

public class GainFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Gain";
    private static final String GAIN_STRING = "GAIN:";
    private static final String BAIS_STRING = "BAIS:";
    private static final int MAX_VALUE = 100;
    
    private static final int GAIN_SEEKBAR_RESID = 21865;
    private static final int BAIS_SEEKBAR_RESID = 21866;
    
    private SeekBar mGainSeekBar;
    private TextView mGainTextView;
    private SeekBar mBaisSeekBar;
    private TextView mBaisTextView;
    
    private int mGainValue;
    private int mBaisValue;
    
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
        mGainTextView = new TextView(this);
        mGainTextView.setText(GAIN_STRING+getValue(mGainValue));
        mGainTextView.setTextSize(TITLE_TEXT_SIZE);
        mGainTextView.setTextColor(Color.BLACK);
        mGainTextView.setGravity(Gravity.CENTER);
        
        mGainSeekBar = new SeekBar(this);
        mGainSeekBar.setOnSeekBarChangeListener(this);
        mGainSeekBar.setId(GAIN_SEEKBAR_RESID);
        mGainSeekBar.setMax(MAX_VALUE);
        
        mBaisTextView = new TextView(this);
        mBaisTextView.setText(BAIS_STRING+getValue(mBaisValue));
        mBaisTextView.setTextSize(TITLE_TEXT_SIZE);
        mBaisTextView.setTextColor(Color.BLACK);
        mBaisTextView.setGravity(Gravity.CENTER);
        
        mBaisSeekBar = new SeekBar(this);
        mBaisSeekBar.setOnSeekBarChangeListener(this);
        mBaisSeekBar.setId(BAIS_SEEKBAR_RESID);
        mBaisSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mGainTextView);
        mainLayout.addView(mGainSeekBar);
        mainLayout.addView(mBaisTextView);
        mainLayout.addView(mBaisSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case GAIN_SEEKBAR_RESID:
            mGainValue = progress;
            mGainTextView.setText(GAIN_STRING+getValue(mGainValue));
            break;
        case BAIS_SEEKBAR_RESID:
            mBaisValue = progress;
            mBaisTextView.setText(BAIS_STRING+getValue(mBaisValue));
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
                GainFilter filter = new GainFilter();
                filter.setGain(getValue(mGainValue));
                filter.setBias(getValue(mBaisValue));
                mColors = filter.filter(mColors, width, height);
                GainFilterActivity.this.runOnUiThread(new Runnable() {
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

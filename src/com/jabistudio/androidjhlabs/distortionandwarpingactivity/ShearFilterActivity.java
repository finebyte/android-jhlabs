package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.OffsetFilter;
import com.jabistudio.androidjhlabs.filter.ShearFilter;
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

public class ShearFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Shear";
    private static final String XANGLE_STRING = "XANGLE:";
    private static final String YANGLE_STRING = "YANGLE:";
    private static final int MAX_VALUE = 104;
    
    private static final int XANGLE_SEEKBAR_RESID = 21863;
    private static final int YANGLE_SEEKBAR_RESID = 21864;
    
    private SeekBar mXAngleSeekBar;
    private TextView mXAngleTextView;
    private SeekBar mYAngleSeekBar;
    private TextView mYAngleTextView;
    
    private int mXAngleValue;
    private int mYAngleValue;
    
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
        mXAngleTextView = new TextView(this);
        mXAngleTextView.setText(XANGLE_STRING+mXAngleValue);
        mXAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mXAngleTextView.setTextColor(Color.BLACK);
        mXAngleTextView.setGravity(Gravity.CENTER);
        
        mXAngleSeekBar = new SeekBar(this);
        mXAngleSeekBar.setOnSeekBarChangeListener(this);
        mXAngleSeekBar.setId(XANGLE_SEEKBAR_RESID);
        mXAngleSeekBar.setMax(MAX_VALUE);
        mXAngleSeekBar.setProgress(MAX_VALUE/2);
        
        mYAngleTextView = new TextView(this);
        mYAngleTextView.setText(YANGLE_STRING+mYAngleValue);
        mYAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mYAngleTextView.setTextColor(Color.BLACK);
        mYAngleTextView.setGravity(Gravity.CENTER);
        
        mYAngleSeekBar = new SeekBar(this);
        mYAngleSeekBar.setOnSeekBarChangeListener(this);
        mYAngleSeekBar.setId(YANGLE_SEEKBAR_RESID);
        mYAngleSeekBar.setMax(MAX_VALUE);
        mYAngleSeekBar.setProgress(MAX_VALUE/2);
        
        mainLayout.addView(mXAngleTextView);
        mainLayout.addView(mXAngleSeekBar);
        mainLayout.addView(mYAngleTextView);
        mainLayout.addView(mYAngleSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case XANGLE_SEEKBAR_RESID:
            mXAngleValue = progress;
            mXAngleTextView.setText(XANGLE_STRING+getValue(mXAngleValue));
            break;
        case YANGLE_SEEKBAR_RESID:
            mYAngleValue = progress;
            mYAngleTextView.setText(YANGLE_STRING+getValue(mYAngleValue));
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
                ShearFilter filter = new ShearFilter();
                filter.setXAngle(getValue(mXAngleValue));
                filter.setYAngle(getValue(mYAngleValue));
                mColors = filter.filter(mColors, width, height);

                ShearFilterActivity.this.runOnUiThread(new Runnable() {
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
        retValue = (float)((value - (MAX_VALUE/2)) / 100f);
        return retValue;
    }
}

package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.RotateFilter;
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

public class RotateFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Rotate";
    private static final String ANGLE_STRING = "ANGLE:";
    private static final int MAX_VALUE = 624;
    
    private static final int ANGLE_SEEKBAR_RESID = 21865;
    
    private SeekBar mAngleSeekBar;
    private TextView mAngleTextView;
    
    private int mAngleValue;
    
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
        mAngleTextView = new TextView(this);
        mAngleTextView.setText(ANGLE_STRING+mAngleValue);
        mAngleTextView.setTextSize(TITLE_TEXT_SIZE);
        mAngleTextView.setTextColor(Color.BLACK);
        mAngleTextView.setGravity(Gravity.CENTER);
        
        mAngleSeekBar = new SeekBar(this);
        mAngleSeekBar.setOnSeekBarChangeListener(this);
        mAngleSeekBar.setId(ANGLE_SEEKBAR_RESID);
        mAngleSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mAngleTextView);
        mainLayout.addView(mAngleSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case ANGLE_SEEKBAR_RESID:
            mAngleValue = progress;
            mAngleTextView.setText(ANGLE_STRING+getAngle(mAngleValue));
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
                RotateFilter filter = new RotateFilter();
                filter.setAngle(getAngle(mAngleValue));

                mColors = filter.filter(mColors, width, height);
                RotateFilterActivity.this.runOnUiThread(new Runnable() {
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
    private float getAngle(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
}

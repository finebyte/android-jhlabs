package com.jabistudio.androidjhlabs.coloradjustmentfilteractivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.PosterizeFilter;
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

public class PosterizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Posterize";
    private static final String LEVEL_STRING = "LEVEL:";
    private static final int MAX_VALUE = 16;
    
    private static final int LEVEL_SEEKBAR_RESID = 21865;
    
    private SeekBar mLevelSeekBar;
    private TextView mLevelTextView;
    
    private int mLevelValue;
    
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
        mLevelSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mLevelTextView);
        mainLayout.addView(mLevelSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case LEVEL_SEEKBAR_RESID:
            mLevelValue = progress;
            mLevelTextView.setText(LEVEL_STRING+mLevelValue);
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
                PosterizeFilter filter = new PosterizeFilter();
                filter.setNumLevels(mLevelValue);

                mColors = filter.filter(mColors, width, height);
                PosterizeFilterActivity.this.runOnUiThread(new Runnable() {
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

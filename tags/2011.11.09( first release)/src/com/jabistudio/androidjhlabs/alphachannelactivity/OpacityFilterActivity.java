package com.jabistudio.androidjhlabs.alphachannelactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.OpacityFilter;
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

public class OpacityFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Opacity";
    private static final String OPACITY_STRING = "OPACITY:";
    private static final int MAX_VALUE = 255;
    
    private static final int OPACITY_SEEKBAR_RESID = 21865;
    
    private SeekBar mOpacitySeekBar;
    private TextView mOpacityTextView;
    
    private int mOpacityValue;
    
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
        mOpacityTextView = new TextView(this);
        mOpacityTextView.setText(OPACITY_STRING+mOpacityValue);
        mOpacityTextView.setTextSize(TITLE_TEXT_SIZE);
        mOpacityTextView.setTextColor(Color.BLACK);
        mOpacityTextView.setGravity(Gravity.CENTER);
        
        mOpacitySeekBar = new SeekBar(this);
        mOpacitySeekBar.setOnSeekBarChangeListener(this);
        mOpacitySeekBar.setId(OPACITY_SEEKBAR_RESID);
        mOpacitySeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mOpacityTextView);
        mainLayout.addView(mOpacitySeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case OPACITY_SEEKBAR_RESID:
            mOpacityValue = progress;
            mOpacityTextView.setText(OPACITY_STRING+mOpacityValue);
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
                OpacityFilter filter = new OpacityFilter();
                filter.setOpacity(mOpacityValue);

                mColors = filter.filter(mColors, width, height);
                OpacityFilterActivity.this.runOnUiThread(new Runnable() {
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

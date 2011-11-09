package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.StampFilter;
import com.jabistudio.androidjhlabs.filter.WeaveFilter;
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

public class WeaveFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Weave";
    private static final String XWIDTH_STRING = "XWIDTH:";
    private static final String YWIDTH_STRING = "YWIDTH:";
    private static final String XGAP_STRING = "XGAP:";
    private static final String YGAP_STRING = "YGAP:";
    private static final int MAX_VALUE = 150;
    
    private static final int XWIDTH_SEEKBAR_RESID = 21863;
    private static final int YWIDTH_SEEKBAR_RESID = 21864;
    private static final int XGAP_SEEKBAR_RESID = 21865;
    private static final int YGAP_SEEKBAR_RESID = 21866;
    
    private SeekBar mXWidthSeekBar;
    private TextView mXWidthTextView;
    private SeekBar mYWidthSeekBar;
    private TextView mYWidthTextView;
    private SeekBar mXGapSeekBar;
    private TextView mXGapTextView;
    private SeekBar mYGapSeekBar;
    private TextView mYGapTextView;
    
    private int mXWidthValue;
    private int mYWidthValue;
    private int mXGapValue;
    private int mYGapValue;
    
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
        mXWidthTextView = new TextView(this);
        mXWidthTextView.setText(XWIDTH_STRING+mXWidthValue);
        mXWidthTextView.setTextSize(TITLE_TEXT_SIZE);
        mXWidthTextView.setTextColor(Color.BLACK);
        mXWidthTextView.setGravity(Gravity.CENTER);
        
        mXWidthSeekBar = new SeekBar(this);
        mXWidthSeekBar.setOnSeekBarChangeListener(this);
        mXWidthSeekBar.setId(XWIDTH_SEEKBAR_RESID);
        mXWidthSeekBar.setMax(MAX_VALUE);
        
        mYWidthTextView = new TextView(this);
        mYWidthTextView.setText(YWIDTH_STRING+mYWidthValue);
        mYWidthTextView.setTextSize(TITLE_TEXT_SIZE);
        mYWidthTextView.setTextColor(Color.BLACK);
        mYWidthTextView.setGravity(Gravity.CENTER);
        
        mYWidthSeekBar = new SeekBar(this);
        mYWidthSeekBar.setOnSeekBarChangeListener(this);
        mYWidthSeekBar.setId(YWIDTH_SEEKBAR_RESID);
        mYWidthSeekBar.setMax(MAX_VALUE);
        
        mXGapTextView = new TextView(this);
        mXGapTextView.setText(XGAP_STRING+mXGapValue);
        mXGapTextView.setTextSize(TITLE_TEXT_SIZE);
        mXGapTextView.setTextColor(Color.BLACK);
        mXGapTextView.setGravity(Gravity.CENTER);
        
        mXGapSeekBar = new SeekBar(this);
        mXGapSeekBar.setOnSeekBarChangeListener(this);
        mXGapSeekBar.setId(XGAP_SEEKBAR_RESID);
        mXGapSeekBar.setMax(MAX_VALUE);
        
        mYGapTextView = new TextView(this);
        mYGapTextView.setText(YGAP_STRING+mYGapValue);
        mYGapTextView.setTextSize(TITLE_TEXT_SIZE);
        mYGapTextView.setTextColor(Color.BLACK);
        mYGapTextView.setGravity(Gravity.CENTER);
        
        mYGapSeekBar = new SeekBar(this);
        mYGapSeekBar.setOnSeekBarChangeListener(this);
        mYGapSeekBar.setId(YGAP_SEEKBAR_RESID);
        mYGapSeekBar.setMax(MAX_VALUE);

        mainLayout.addView(mXWidthTextView);
        mainLayout.addView(mXWidthSeekBar);
        mainLayout.addView(mYWidthTextView);
        mainLayout.addView(mYWidthSeekBar);
        mainLayout.addView(mXGapTextView);
        mainLayout.addView(mXGapSeekBar);
        mainLayout.addView(mYGapTextView);
        mainLayout.addView(mYGapSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case XWIDTH_SEEKBAR_RESID:
            mXWidthValue = progress;
            mXWidthTextView.setText(XWIDTH_STRING+mXWidthValue);
            break;
        case YWIDTH_SEEKBAR_RESID:
            mYWidthValue = progress;
            mYWidthTextView.setText(YWIDTH_STRING+mYWidthValue);
            break;
        case XGAP_SEEKBAR_RESID:
            mXGapValue = progress;
            mXGapTextView.setText(XGAP_STRING+mXGapValue);
            break;
        case YGAP_SEEKBAR_RESID:
            mYGapValue = progress;
            mYGapTextView.setText(YGAP_STRING+mYGapValue);
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
                WeaveFilter filter = new WeaveFilter();
                filter.setXWidth(mXWidthValue);
                filter.setYWidth(mYWidthValue);
                filter.setXGap(mXGapValue);
                filter.setYGap(mYGapValue);
                mColors = filter.filter(mColors, width, height);

                WeaveFilterActivity.this.runOnUiThread(new Runnable() {
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

package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.filter.BlurFilter;
import com.jabistudio.androidjhlabs.filter.BoxBlurFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BoxBlurFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "BoxBlur";
    private static final String HRADIUS_STRING = "HRADIUS:";
    private static final String VRADIUS_STRING = "VRADIUS:";
    private static final String RADIUS_STRING = "RADIUS:";
    private static final int MAX_VALUE = 50;
    
    private static final int HRADIUS_SEEKBAR_RESID = 21863;
    private static final int VRADIUS_SEEKBAR_RESID = 21864;
    private static final int RADIUS_SEEKBAR_RESID = 21865;
    
    private SeekBar mHRadiusSeekBar;
    private TextView mHRadiusTextView;
    private SeekBar mVRadiusSeekBar;
    private TextView mVRadiusTextView;
    private SeekBar mRadiusSeekBar;
    private TextView mRadiusTextView;
    
    private int mHRadiusValue;
    private int mVRadiusValue;
    private int mRadiusValue;
    
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
        mHRadiusTextView = new TextView(this);
        mHRadiusTextView.setText(HRADIUS_STRING+mHRadiusValue);
        mHRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mHRadiusTextView.setTextColor(Color.BLACK);
        mHRadiusTextView.setGravity(Gravity.CENTER);
        
        mHRadiusSeekBar = new SeekBar(this);
        mHRadiusSeekBar.setOnSeekBarChangeListener(this);
        mHRadiusSeekBar.setId(HRADIUS_SEEKBAR_RESID);
        mHRadiusSeekBar.setMax(MAX_VALUE);
        
        mVRadiusTextView = new TextView(this);
        mVRadiusTextView.setText(VRADIUS_STRING+mVRadiusValue);
        mVRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mVRadiusTextView.setTextColor(Color.BLACK);
        mVRadiusTextView.setGravity(Gravity.CENTER);
        
        mVRadiusSeekBar = new SeekBar(this);
        mVRadiusSeekBar.setOnSeekBarChangeListener(this);
        mVRadiusSeekBar.setId(VRADIUS_SEEKBAR_RESID);
        mVRadiusSeekBar.setMax(MAX_VALUE);
        
        mRadiusTextView = new TextView(this);
        mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
        mRadiusTextView.setTextSize(TITLE_TEXT_SIZE);
        mRadiusTextView.setTextColor(Color.BLACK);
        mRadiusTextView.setGravity(Gravity.CENTER);
        
        mRadiusSeekBar = new SeekBar(this);
        mRadiusSeekBar.setOnSeekBarChangeListener(this);
        mRadiusSeekBar.setId(RADIUS_SEEKBAR_RESID);
        mRadiusSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mHRadiusTextView);
        mainLayout.addView(mHRadiusSeekBar);
        mainLayout.addView(mVRadiusTextView);
        mainLayout.addView(mVRadiusSeekBar);
        mainLayout.addView(mRadiusTextView);
        mainLayout.addView(mRadiusSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case HRADIUS_SEEKBAR_RESID:
            mHRadiusValue = progress;
            mHRadiusTextView.setText(HRADIUS_STRING+mHRadiusValue);
            break;
        case VRADIUS_SEEKBAR_RESID:
            mVRadiusValue = progress;
            mVRadiusTextView.setText(VRADIUS_STRING+mVRadiusValue);
            break;
        case RADIUS_SEEKBAR_RESID:
            mHRadiusValue = 0;
            mVRadiusValue = 0;
            mHRadiusSeekBar.setProgress(mHRadiusValue);
            mVRadiusSeekBar.setProgress(mVRadiusValue);
            mRadiusValue = progress;
            mRadiusTextView.setText(RADIUS_STRING+mRadiusValue);
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
                BoxBlurFilter filter = new BoxBlurFilter();
                filter.setHRadius(mHRadiusValue);
                filter.setVRadius(mVRadiusValue);
                if(mHRadiusValue == 0 && mVRadiusValue == 0){
                    filter.setRadius(mRadiusValue);
                }
                mColors = filter.filter(mColors, width, height);

                BoxBlurFilterActivity.this.runOnUiThread(new Runnable() {
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

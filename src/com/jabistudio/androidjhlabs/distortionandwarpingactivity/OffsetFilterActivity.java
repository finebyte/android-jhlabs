package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.OffsetFilter;
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

public class OffsetFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Offset";
    private static final String OFFSETX_STRING = "OFFSETX:";
    private static final String OFFSETY_STRING = "OFFSETY:";
    private static final int MAX_VALUE = 200;
    
    private static final int OFFSETX_SEEKBAR_RESID = 21863;
    private static final int OFFSETY_SEEKBAR_RESID = 21864;
    
    private SeekBar mOffsetXSeekBar;
    private TextView mOffsetXTextView;
    private SeekBar mOffsetYSeekBar;
    private TextView mOffsetYTextView;
    
    private int mOffsetXValue;
    private int mOffsetYValue;
    
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
        mOffsetXTextView = new TextView(this);
        mOffsetXTextView.setText(OFFSETX_STRING+mOffsetXValue);
        mOffsetXTextView.setTextSize(TITLE_TEXT_SIZE);
        mOffsetXTextView.setTextColor(Color.BLACK);
        mOffsetXTextView.setGravity(Gravity.CENTER);
        
        mOffsetXSeekBar = new SeekBar(this);
        mOffsetXSeekBar.setOnSeekBarChangeListener(this);
        mOffsetXSeekBar.setId(OFFSETX_SEEKBAR_RESID);
        mOffsetXSeekBar.setMax(MAX_VALUE);
        
        mOffsetYTextView = new TextView(this);
        mOffsetYTextView.setText(OFFSETY_STRING+mOffsetYValue);
        mOffsetYTextView.setTextSize(TITLE_TEXT_SIZE);
        mOffsetYTextView.setTextColor(Color.BLACK);
        mOffsetYTextView.setGravity(Gravity.CENTER);
        
        mOffsetYSeekBar = new SeekBar(this);
        mOffsetYSeekBar.setOnSeekBarChangeListener(this);
        mOffsetYSeekBar.setId(OFFSETY_SEEKBAR_RESID);
        mOffsetYSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mOffsetXTextView);
        mainLayout.addView(mOffsetXSeekBar);
        mainLayout.addView(mOffsetYTextView);
        mainLayout.addView(mOffsetYSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case OFFSETX_SEEKBAR_RESID:
            mOffsetXValue = progress;
            mOffsetXTextView.setText(OFFSETX_STRING+mOffsetXValue);
            break;
        case OFFSETY_SEEKBAR_RESID:
            mOffsetYValue = progress;
            mOffsetYTextView.setText(OFFSETY_STRING+mOffsetYValue);
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
                OffsetFilter filter = new OffsetFilter();
                filter.setXOffset(mOffsetXValue);
                filter.setYOffset(mOffsetYValue);
                mColors = filter.filter(mColors, width, height);

                OffsetFilterActivity.this.runOnUiThread(new Runnable() {
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

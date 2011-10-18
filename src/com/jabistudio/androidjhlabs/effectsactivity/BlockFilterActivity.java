package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.BlockFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
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

public class BlockFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Block";
    private static final String BLOCKSIZE_STRING = "BLOCKSIZE:";
    private static final int MAX_VALUE = 100;
    
    private static final int BLOCKSIZE_SEEKBAR_RESID = 21865;
    
    private SeekBar mBlockSeekBar;
    private TextView mBlockTextView;
    
    private int mBlockValue;
    
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
        mBlockTextView = new TextView(this);
        mBlockTextView.setText(BLOCKSIZE_STRING+mBlockValue);
        mBlockTextView.setTextSize(TITLE_TEXT_SIZE);
        mBlockTextView.setTextColor(Color.BLACK);
        mBlockTextView.setGravity(Gravity.CENTER);
        
        mBlockSeekBar = new SeekBar(this);
        mBlockSeekBar.setOnSeekBarChangeListener(this);
        mBlockSeekBar.setId(BLOCKSIZE_SEEKBAR_RESID);
        mBlockSeekBar.setMax(MAX_VALUE);
        
        mainLayout.addView(mBlockTextView);
        mainLayout.addView(mBlockSeekBar);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case BLOCKSIZE_SEEKBAR_RESID:
            mBlockValue = progress;
            mBlockTextView.setText(BLOCKSIZE_STRING+mBlockValue);
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
                BlockFilter filter = new BlockFilter();
                if(mBlockValue == 0){
                    mBlockValue = 1;
                }
                filter.setBlockSize(mBlockValue);

                mColors = filter.filter(mColors, width, height);
                BlockFilterActivity.this.runOnUiThread(new Runnable() {
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

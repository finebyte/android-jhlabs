package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.CropFilter;
import com.jabistudio.androidjhlabs.filter.RippleFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class CropFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener{
    private static final String TITLE = "Ripple";
    private static final String X_STRING = "X:";
    private static final String Y_STRING = "Y:";
    private static final String WIDTH_STRING = "WIDTH:";
    private static final String HEIGHT_STRING = "HEIGHT:";
    
    private static final int X_SEEKBAR_RESID = 21863;
    private static final int Y_SEEKBAR_RESID = 21864;
    private static final int WIDTH_SEEKBAR_RESID = 21865;
    private static final int HEIGHT_SEEKBAR_RESID = 21866;
    
    private SeekBar mXSeekBar;
    private TextView mXTextView;
    private SeekBar mYSeekBar;
    private TextView mYTextView;
    private SeekBar mWidthSeekBar;
    private TextView mWidthTextView;
    private SeekBar mHeightSeekBar;
    private TextView mHeightView;
    
    private int mXValue;
    private int mYValue;
    private int mWidthValue;
    private int mHeightValue;
    
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
        mXTextView = new TextView(this);
        mXTextView.setText(X_STRING+mXValue);
        mXTextView.setTextSize(TITLE_TEXT_SIZE);
        mXTextView.setTextColor(Color.BLACK);
        mXTextView.setGravity(Gravity.CENTER);
        
        mXSeekBar = new SeekBar(this);
        mXSeekBar.setOnSeekBarChangeListener(this);
        mXSeekBar.setId(X_SEEKBAR_RESID);
        mXSeekBar.setMax(AndroidUtils.getBitmapOfWidth(getResources(),R.drawable.image));
        
        mYTextView = new TextView(this);
        mYTextView.setText(Y_STRING+mYValue);
        mYTextView.setTextSize(TITLE_TEXT_SIZE);
        mYTextView.setTextColor(Color.BLACK);
        mYTextView.setGravity(Gravity.CENTER);
        
        mYSeekBar = new SeekBar(this);
        mYSeekBar.setOnSeekBarChangeListener(this);
        mYSeekBar.setId(Y_SEEKBAR_RESID);
        mYSeekBar.setMax(AndroidUtils.getBitmapOfHeight(getResources(),R.drawable.image));
        
        mWidthTextView = new TextView(this);
        mWidthTextView.setText(WIDTH_STRING+mWidthValue);
        mWidthTextView.setTextSize(TITLE_TEXT_SIZE);
        mWidthTextView.setTextColor(Color.BLACK);
        mWidthTextView.setGravity(Gravity.CENTER);
        
        mWidthSeekBar = new SeekBar(this);
        mWidthSeekBar.setOnSeekBarChangeListener(this);
        mWidthSeekBar.setId(WIDTH_SEEKBAR_RESID);
        mWidthSeekBar.setMax(AndroidUtils.getBitmapOfWidth(getResources(),R.drawable.image));
        mWidthSeekBar.setProgress(AndroidUtils.getBitmapOfWidth(getResources(),R.drawable.image));
        
        mHeightView = new TextView(this);
        mHeightView.setText(HEIGHT_STRING+mHeightValue);
        mHeightView.setTextSize(TITLE_TEXT_SIZE);
        mHeightView.setTextColor(Color.BLACK);
        mHeightView.setGravity(Gravity.CENTER);
        
        mHeightSeekBar = new SeekBar(this);
        mHeightSeekBar.setOnSeekBarChangeListener(this);
        mHeightSeekBar.setId(HEIGHT_SEEKBAR_RESID);
        mHeightSeekBar.setMax(AndroidUtils.getBitmapOfHeight(getResources(),R.drawable.image));
        mHeightSeekBar.setProgress(AndroidUtils.getBitmapOfHeight(getResources(),R.drawable.image));
        
        mainLayout.addView(mXTextView);
        mainLayout.addView(mXSeekBar);
        mainLayout.addView(mYTextView);
        mainLayout.addView(mYSeekBar);
        mainLayout.addView(mWidthTextView);
        mainLayout.addView(mWidthSeekBar);
        mainLayout.addView(mHeightView);
        mainLayout.addView(mHeightSeekBar);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case X_SEEKBAR_RESID:
            mXValue = progress;
            mXTextView.setText(X_STRING+mXValue);
            break;
        case Y_SEEKBAR_RESID:
            mYValue = progress;
            mYTextView.setText(Y_STRING+mYValue);
            break;
        case WIDTH_SEEKBAR_RESID:
            mWidthValue = progress;
            mWidthTextView.setText(WIDTH_STRING+mWidthValue);
            break;
        case HEIGHT_SEEKBAR_RESID:
            mHeightValue = progress;
            mHeightView.setText(HEIGHT_STRING+mHeightValue);
            break;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        applyFilter();
    }
    private void applyFilter(){
        final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = mOriginalImageView.getDrawable().getIntrinsicHeight();
        
        mColors = AndroidUtils.drawableToIntArray(mOriginalImageView.getDrawable());
        mProgressDialog = ProgressDialog.show(this, "", "Wait......");
        
        Thread thread = new Thread(){
            public void run() {
                CropFilter filter = new CropFilter();
                filter.setX(mXValue);
                filter.setY(mYValue);
                filter.setWidth(mWidthValue);
                filter.setHeight(mHeightValue);
                mColors = filter.filter(mColors, width, height);

                CropFilterActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setModifyView(mColors, mWidthValue, mHeightValue);
                    }
                });
                mProgressDialog.dismiss();
            }
        };
        thread.setDaemon(true);
        thread.start();  
    }
}

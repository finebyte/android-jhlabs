package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.KaleidoscopeFilter;
import com.jabistudio.androidjhlabs.filter.RippleFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class RippleFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener,OnItemSelectedListener{
    private static final String TITLE = "Ripple";
    private static final String XAMPLITUTE_STRING = "XAMPLITUTE:";
    private static final String XWAVELENGTH_STRING = "XWAVELENGTH:";
    private static final String YAMPLITUTE_STRING = "YAMPLITUTE:";
    private static final String YWAVELENGTH_STRING = "YWAVELENGTH:";
    private static final String SHAPE_TYPE1_STRING = "Sine";
    private static final String SHAPE_TYPE2_STRING = "Sawtooth";
    private static final String SHAPE_TYPE3_STRING = "Triangle";
    private static final String SHAPE_TYPE4_STRING = "Noise";
    
    private static final int MAX_VALUE = 100;
    
    private static final int XAMPLITUTE_SEEKBAR_RESID = 21863;
    private static final int XWAVELENGTH_SEEKBAR_RESID = 21864;
    private static final int YAMPLITUTE_SEEKBAR_RESID = 21865;
    private static final int YWAVELENGTH_SEEKBAR_RESID = 21866;
    
    private SeekBar mXAmplituteSeekBar;
    private TextView mXAmplituteTextView;
    private SeekBar mXWavelengthSeekBar;
    private TextView mXWavelengthTextView;
    private SeekBar mYAmplituteSeekBar;
    private TextView mYAmplituteTextView;
    private SeekBar mYWavelengthSeekBar;
    private TextView mYWavelengthTextView;
    private Spinner mShapeSpinner;
    
    private int mXAmplituteValue;
    private int mXWavelengthValue;
    private int mYAmplituteValue;
    private int mYWavelengthValue;
    
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
        mXAmplituteTextView = new TextView(this);
        mXAmplituteTextView.setText(XAMPLITUTE_STRING+mXAmplituteValue);
        mXAmplituteTextView.setTextSize(TITLE_TEXT_SIZE);
        mXAmplituteTextView.setTextColor(Color.BLACK);
        mXAmplituteTextView.setGravity(Gravity.CENTER);
        
        mXAmplituteSeekBar = new SeekBar(this);
        mXAmplituteSeekBar.setOnSeekBarChangeListener(this);
        mXAmplituteSeekBar.setId(XAMPLITUTE_SEEKBAR_RESID);
        mXAmplituteSeekBar.setMax(MAX_VALUE);
        
        mXWavelengthTextView = new TextView(this);
        mXWavelengthTextView.setText(XWAVELENGTH_STRING+mXWavelengthValue);
        mXWavelengthTextView.setTextSize(TITLE_TEXT_SIZE);
        mXWavelengthTextView.setTextColor(Color.BLACK);
        mXWavelengthTextView.setGravity(Gravity.CENTER);
        
        mXWavelengthSeekBar = new SeekBar(this);
        mXWavelengthSeekBar.setOnSeekBarChangeListener(this);
        mXWavelengthSeekBar.setId(XWAVELENGTH_SEEKBAR_RESID);
        mXWavelengthSeekBar.setMax(MAX_VALUE);
        
        mYAmplituteTextView = new TextView(this);
        mYAmplituteTextView.setText(YAMPLITUTE_STRING+mYAmplituteValue);
        mYAmplituteTextView.setTextSize(TITLE_TEXT_SIZE);
        mYAmplituteTextView.setTextColor(Color.BLACK);
        mYAmplituteTextView.setGravity(Gravity.CENTER);
        
        mYAmplituteSeekBar = new SeekBar(this);
        mYAmplituteSeekBar.setOnSeekBarChangeListener(this);
        mYAmplituteSeekBar.setId(YAMPLITUTE_SEEKBAR_RESID);
        mYAmplituteSeekBar.setMax(MAX_VALUE);
        
        mYWavelengthTextView = new TextView(this);
        mYWavelengthTextView.setText(YWAVELENGTH_STRING+mYWavelengthValue);
        mYWavelengthTextView.setTextSize(TITLE_TEXT_SIZE);
        mYWavelengthTextView.setTextColor(Color.BLACK);
        mYWavelengthTextView.setGravity(Gravity.CENTER);
        
        mYWavelengthSeekBar = new SeekBar(this);
        mYWavelengthSeekBar.setOnSeekBarChangeListener(this);
        mYWavelengthSeekBar.setId(YWAVELENGTH_SEEKBAR_RESID);
        mYWavelengthSeekBar.setMax(MAX_VALUE);
        
        mShapeSpinner = new Spinner(this);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        shapeAdapter.add(SHAPE_TYPE1_STRING);
        shapeAdapter.add(SHAPE_TYPE2_STRING);
        shapeAdapter.add(SHAPE_TYPE3_STRING);
        shapeAdapter.add(SHAPE_TYPE4_STRING);
        mShapeSpinner.setAdapter(shapeAdapter);
        mShapeSpinner.setOnItemSelectedListener(this);
        
        mainLayout.addView(mXAmplituteTextView);
        mainLayout.addView(mXAmplituteSeekBar);
        mainLayout.addView(mXWavelengthTextView);
        mainLayout.addView(mXWavelengthSeekBar);
        mainLayout.addView(mYAmplituteTextView);
        mainLayout.addView(mYAmplituteSeekBar);
        mainLayout.addView(mYWavelengthTextView);
        mainLayout.addView(mYWavelengthSeekBar);
        mainLayout.addView(mShapeSpinner);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case XAMPLITUTE_SEEKBAR_RESID:
            mXAmplituteValue = progress;
            mXAmplituteTextView.setText(XAMPLITUTE_STRING+mXAmplituteValue);
            break;
        case XWAVELENGTH_SEEKBAR_RESID:
            mXWavelengthValue = progress;
            mXWavelengthTextView.setText(XWAVELENGTH_STRING+mXWavelengthValue);
            break;
        case YAMPLITUTE_SEEKBAR_RESID:
            mYAmplituteValue = progress;
            mYAmplituteTextView.setText(YAMPLITUTE_STRING+mYAmplituteValue);
            break;
        case YWAVELENGTH_SEEKBAR_RESID:
            mYWavelengthValue = progress;
            mYWavelengthTextView.setText(YWAVELENGTH_STRING+mYWavelengthValue);
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
                RippleFilter filter = new RippleFilter();
                filter.setXAmplitude(mXAmplituteValue);
                filter.setXWavelength(mXWavelengthValue);
                filter.setYAmplitude(mYAmplituteValue);
                filter.setYWavelength(mYWavelengthValue);
                filter.setWaveType(getSelectType());
                mColors = filter.filter(mColors, width, height);

                RippleFilterActivity.this.runOnUiThread(new Runnable() {
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
    private int getSelectType(){
        if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE1_STRING)){
            return RippleFilter.SINE;
        }else if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE2_STRING)){
            return RippleFilter.SAWTOOTH;
        }else if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE3_STRING)){
            return RippleFilter.TRIANGLE;
        }else{
            return RippleFilter.NOISE;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        applyFilter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

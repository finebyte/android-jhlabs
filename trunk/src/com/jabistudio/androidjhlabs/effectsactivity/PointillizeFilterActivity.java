package com.jabistudio.androidjhlabs.effectsactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.CrystallizeFilter;
import com.jabistudio.androidjhlabs.filter.DisplaceFilter;
import com.jabistudio.androidjhlabs.filter.PointillizeFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
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

public class PointillizeFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener, OnItemSelectedListener{
    private static final String TITLE = "Pointillize";
    private static final String SIZE_STRING = "SIZE:";
    private static final String RANDOMNESS_STRING = "RANDOMNESS:";
    private static final String FUZZINESS_STRING = "FUZZINESS:";
    
    private static final String SHAPE_TYPE1_STRING = "Squares";
    private static final String SHAPE_TYPE2_STRING = "Hexagons";
    private static final String SHAPE_TYPE3_STRING = "Octagon";
    private static final String SHAPE_TYPE4_STRING = "Triangle";
    
    private static final int MAX_VALUE = 100;
    
    private static final int SIZE_SEEKBAR_RESID = 21865;
    private static final int RANDOMNESS_SEEKBAR_RESID = 21866;
    private static final int FUZZINESS_SEEKBAR_RESID = 21868;
    
    private SeekBar mSizeSeekBar;
    private TextView mSizeTextView;
    private SeekBar mRandomnessSeekBar;
    private TextView mRandomnessTextView;
    private SeekBar mFuzzinessSeekBar;
    private TextView mFuzzinessTextView;
    private Spinner mShapeSpinner;
    
    private int mSizeValue;
    private int mRandomnessValue;
    private int mFuzzinessValue;
    
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
        mSizeTextView = new TextView(this);
        mSizeTextView.setText(SIZE_STRING+mSizeValue);
        mSizeTextView.setTextSize(TITLE_TEXT_SIZE);
        mSizeTextView.setTextColor(Color.BLACK);
        mSizeTextView.setGravity(Gravity.CENTER);
        
        mSizeSeekBar = new SeekBar(this);
        mSizeSeekBar.setOnSeekBarChangeListener(this);
        mSizeSeekBar.setId(SIZE_SEEKBAR_RESID);
        mSizeSeekBar.setMax(MAX_VALUE);
        
        mRandomnessTextView = new TextView(this);
        mRandomnessTextView.setText(RANDOMNESS_STRING+mRandomnessValue);
        mRandomnessTextView.setTextSize(TITLE_TEXT_SIZE);
        mRandomnessTextView.setTextColor(Color.BLACK);
        mRandomnessTextView.setGravity(Gravity.CENTER);
        
        mRandomnessSeekBar = new SeekBar(this);
        mRandomnessSeekBar.setOnSeekBarChangeListener(this);
        mRandomnessSeekBar.setId(RANDOMNESS_SEEKBAR_RESID);
        mRandomnessSeekBar.setMax(MAX_VALUE);
        
        mFuzzinessTextView = new TextView(this);
        mFuzzinessTextView.setText(FUZZINESS_STRING+mFuzzinessValue);
        mFuzzinessTextView.setTextSize(TITLE_TEXT_SIZE);
        mFuzzinessTextView.setTextColor(Color.BLACK);
        mFuzzinessTextView.setGravity(Gravity.CENTER);
        
        mFuzzinessSeekBar = new SeekBar(this);
        mFuzzinessSeekBar.setOnSeekBarChangeListener(this);
        mFuzzinessSeekBar.setId(FUZZINESS_SEEKBAR_RESID);
        mFuzzinessSeekBar.setMax(MAX_VALUE);
        
        mShapeSpinner = new Spinner(this);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        shapeAdapter.add(SHAPE_TYPE1_STRING);
        shapeAdapter.add(SHAPE_TYPE2_STRING);
        shapeAdapter.add(SHAPE_TYPE3_STRING);
        shapeAdapter.add(SHAPE_TYPE4_STRING);
        mShapeSpinner.setAdapter(shapeAdapter);
        mShapeSpinner.setOnItemSelectedListener(this);
        
        mainLayout.addView(mSizeTextView);
        mainLayout.addView(mSizeSeekBar);
        mainLayout.addView(mRandomnessTextView);
        mainLayout.addView(mRandomnessSeekBar);
        mainLayout.addView(mFuzzinessTextView);
        mainLayout.addView(mFuzzinessSeekBar);
        mainLayout.addView(mShapeSpinner);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case SIZE_SEEKBAR_RESID:
            mSizeValue = progress;
            mSizeTextView.setText(SIZE_STRING+mSizeValue);
            break;
        case RANDOMNESS_SEEKBAR_RESID:
            mRandomnessValue = progress;
            mRandomnessTextView.setText(RANDOMNESS_STRING+getAmout(mRandomnessValue));
            break;
        case FUZZINESS_SEEKBAR_RESID:
            mFuzzinessValue = progress;
            mFuzzinessTextView.setText(FUZZINESS_STRING+getAmout(mFuzzinessValue));
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
                PointillizeFilter filter = new PointillizeFilter();
                filter.setEdgeColor(Color.BLACK);
                filter.setScale(mSizeValue);
                filter.setRandomness(getAmout(mRandomnessValue));
                filter.setAmount(0);
                filter.setFuzziness(getAmout(mFuzzinessValue));
                filter.setGridType(getSelectType());
                mColors = filter.filter(mColors, width, height);
                PointillizeFilterActivity.this.runOnUiThread(new Runnable() {
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
    
    private float getAmout(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
    
    private int getSelectType(){
        if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE1_STRING)){
            return PointillizeFilter.SQUARE;
        }else if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE2_STRING)){
            return PointillizeFilter.HEXAGONAL;
        }else if(mShapeSpinner.getSelectedItem().equals(SHAPE_TYPE3_STRING)){
            return PointillizeFilter.OCTAGONAL;
        }else {
            return PointillizeFilter.TRIANGULAR;
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
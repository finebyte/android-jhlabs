package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.blurringandsharpeningactivity.GaussianFilterActivity;
import com.jabistudio.androidjhlabs.filter.DisplaceFilter;
import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.RippleFilter;
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

public class DisplaceFilterActivity extends SuperFilterActivity implements OnSeekBarChangeListener,OnItemSelectedListener{
    private static final String TITLE = "Displace";
    private static final String AMOUNT_STRING = "AMOUNT:";
    private static final String EDGES_TYPE1_STRING = "Transparent";
    private static final String EDGES_TYPE2_STRING = "Clamp";
    private static final String EDGES_TYPE3_STRING = "Wrap";
    private static final int MAX_VALUE = 100;
    
    private static final int AMOUNT_SEEKBAR_RESID = 21865;
    
    private SeekBar mAmountSeekBar;
    private TextView mAmountTextView;
    private Spinner mEdgesSpinner;
    
    private int mAmountValue;
    
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
        mAmountTextView = new TextView(this);
        mAmountTextView.setText(AMOUNT_STRING+mAmountValue);
        mAmountTextView.setTextSize(TITLE_TEXT_SIZE);
        mAmountTextView.setTextColor(Color.BLACK);
        mAmountTextView.setGravity(Gravity.CENTER);
        
        mAmountSeekBar = new SeekBar(this);
        mAmountSeekBar.setOnSeekBarChangeListener(this);
        mAmountSeekBar.setId(AMOUNT_SEEKBAR_RESID);
        mAmountSeekBar.setMax(MAX_VALUE);
        
        mEdgesSpinner = new Spinner(this);
        ArrayAdapter<String> shapeAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        shapeAdapter.add(EDGES_TYPE1_STRING);
        shapeAdapter.add(EDGES_TYPE2_STRING);
        shapeAdapter.add(EDGES_TYPE3_STRING);
        mEdgesSpinner.setAdapter(shapeAdapter);
        mEdgesSpinner.setOnItemSelectedListener(this);
        
        mainLayout.addView(mAmountTextView);
        mainLayout.addView(mAmountSeekBar);
        mainLayout.addView(mEdgesSpinner);
    }
    
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        switch(seekBar.getId()){
        case AMOUNT_SEEKBAR_RESID:
            mAmountValue = progress;
            mAmountTextView.setText(AMOUNT_STRING+getValue(mAmountValue));
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
                DisplaceFilter filter = new DisplaceFilter();
                filter.setAmount(getValue(mAmountValue));
                filter.setEdgeAction(getSelectType());
                mColors = filter.filter(mColors, width, height);
                DisplaceFilterActivity.this.runOnUiThread(new Runnable() {
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
        if(mEdgesSpinner.getSelectedItem().equals(EDGES_TYPE1_STRING)){
            return DisplaceFilter.ZERO;
        }else if(mEdgesSpinner.getSelectedItem().equals(EDGES_TYPE2_STRING)){
            return DisplaceFilter.CLAMP;
        }else{
            return DisplaceFilter.WRAP;
        }
    }
    
    private float getValue(int value){
        float retValue = 0;
        retValue = (float)(value / 100f);
        return retValue;
    }
    
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        applyFilter();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        
    }
}

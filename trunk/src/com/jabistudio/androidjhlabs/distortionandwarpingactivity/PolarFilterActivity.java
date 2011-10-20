package com.jabistudio.androidjhlabs.distortionandwarpingactivity;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.filter.PolarFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

public class PolarFilterActivity  extends SuperFilterActivity implements OnCheckedChangeListener,OnClickListener{
    private static final String TITLE = "Polar";
    private static final String APPLY_STRING = "Apply";
    private static final String RECT_TO_POLAR_STRING = "RECT TO POLAR";
    private static final String POLAR_TO_RECT_STRING = "POLAR TO RECT";
    private static final String INVERT_IN_CIRCLE_STRING = "INVERT IN CIRCLE";
    
    private static final int RECT_TO_POLAR_CHECKBOX_RESID = 21864;
    private static final int POLAR_TO_RECT_CHECKBOX_RESID = 21865;
    private static final int INVERT_IN_CIRCLE_CHECKBOX_RESID = 21866;
    
    private RadioGroup mRadioGroup;
    private RadioButton mRectToPolarRadioButton;
    private RadioButton mPolarToRectRadioButton;
    private RadioButton mInvertInCircleRadioButton;
    
    private boolean mIsRectToPolar = false;
    private boolean mIsPolarToRect = false;
    private boolean mIsInvertInCircle = false;
    
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
        mRadioGroup = new RadioGroup(this);
       
        mRectToPolarRadioButton = new RadioButton(this);
        mRectToPolarRadioButton.setText(RECT_TO_POLAR_STRING);
        mRectToPolarRadioButton.setTextSize(TITLE_TEXT_SIZE);
        mRectToPolarRadioButton.setTextColor(Color.BLACK);
        mRectToPolarRadioButton.setGravity(Gravity.CENTER);
        mRectToPolarRadioButton.setOnCheckedChangeListener(this);
        mRectToPolarRadioButton.setId(RECT_TO_POLAR_CHECKBOX_RESID);
        
        mPolarToRectRadioButton = new RadioButton(this);
        mPolarToRectRadioButton.setText(POLAR_TO_RECT_STRING);
        mPolarToRectRadioButton.setTextSize(TITLE_TEXT_SIZE);
        mPolarToRectRadioButton.setTextColor(Color.BLACK);
        mPolarToRectRadioButton.setGravity(Gravity.CENTER);
        mPolarToRectRadioButton.setOnCheckedChangeListener(this);
        mPolarToRectRadioButton.setId(POLAR_TO_RECT_CHECKBOX_RESID);
        
        mInvertInCircleRadioButton = new RadioButton(this);
        mInvertInCircleRadioButton.setText(INVERT_IN_CIRCLE_STRING);
        mInvertInCircleRadioButton.setTextSize(TITLE_TEXT_SIZE);
        mInvertInCircleRadioButton.setTextColor(Color.BLACK);
        mInvertInCircleRadioButton.setGravity(Gravity.CENTER);
        mInvertInCircleRadioButton.setOnCheckedChangeListener(this);
        mInvertInCircleRadioButton.setId(INVERT_IN_CIRCLE_CHECKBOX_RESID);
      
        mRadioGroup.addView(mRectToPolarRadioButton);
        mRadioGroup.addView(mPolarToRectRadioButton);
        mRadioGroup.addView(mInvertInCircleRadioButton);
        
        Button button = new Button(this);
        button.setText(APPLY_STRING);
        button.setOnClickListener(this);
        
        mainLayout.addView(mRadioGroup);
        mainLayout.addView(button);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundbutton, boolean isChecked) {
        switch(compoundbutton.getId()){
        case RECT_TO_POLAR_CHECKBOX_RESID:
            mIsRectToPolar = isChecked;
            break;
        case POLAR_TO_RECT_CHECKBOX_RESID:
            mIsPolarToRect = isChecked;
            break;
        case INVERT_IN_CIRCLE_CHECKBOX_RESID:
            mIsInvertInCircle = isChecked;
            break;
        }
    }
    
    private void applyFilter(){
        final int width = mOriginalImageView.getDrawable().getIntrinsicWidth();
        final int height = mOriginalImageView.getDrawable().getIntrinsicHeight();
        
        mColors = AndroidUtils.drawableToIntArray(mOriginalImageView.getDrawable());
        mProgressDialog = ProgressDialog.show(this, "", "Wait......");
        
        Thread thread = new Thread(){
            public void run() {
                PolarFilter filter = new PolarFilter();
                if(mIsRectToPolar == true){
                    filter.setType(PolarFilter.RECT_TO_POLAR);
                }else if(mIsPolarToRect == true){
                    filter.setType(PolarFilter.POLAR_TO_RECT);
                }else if(mIsInvertInCircle == true){
                    filter.setType(PolarFilter.INVERT_IN_CIRCLE);
                }
                mColors = filter.filter(mColors, width, height);

                PolarFilterActivity.this.runOnUiThread(new Runnable() {
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

    @Override
    public void onClick(View v) {
        applyFilter();
    }
}

package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import com.jabistudio.androidjhlabs.SuperFilterActivity;
import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.filter.BlurFilter;
import com.jabistudio.androidjhlabs.filter.ConvolveFilter;
import com.jabistudio.androidjhlabs.filter.Kernel;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class BlurFilterActivity extends SuperFilterActivity implements OnClickListener{
    private static final String TITLE = "Blur";
    
    private static final String APPLY_STRING = "Apply:";
    private int mApplyCount = 0;
    
    private TextView mApplyText;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE);
        filterButtonSetup(mMainLayout);
    }
    
    /**
     * filterButtonSetting
     * @param mainLayout
     */
    private void filterButtonSetup(LinearLayout mainLayout){
        mApplyText = new TextView(this);
        mApplyText.setText(APPLY_STRING + mApplyCount);
        mApplyText.setTextSize(TITLE_TEXT_SIZE);
        mApplyText.setTextColor(Color.BLACK);
        mApplyText.setGravity(Gravity.CENTER);
        
        Button button = new Button(this);
        button.setText(APPLY_STRING);
        button.setOnClickListener(this);
         
        mainLayout.addView(mApplyText);
        mainLayout.addView(button);
    }
    
    @Override
    public void onClick(View v) {
        final int width = mModifyImageView.getDrawable().getIntrinsicWidth();
        final int height = mModifyImageView.getDrawable().getIntrinsicHeight();
        
        int[] colors = AndroidUtils.drawableToIntArray(mModifyImageView.getDrawable());
        
        BlurFilter filter = new BlurFilter();
        colors = filter.filter(colors, width, height);
        
        setModifyView(colors, width, height);
        
        mApplyCount++;
        mApplyText.setText(APPLY_STRING + mApplyCount);
    }
   
}

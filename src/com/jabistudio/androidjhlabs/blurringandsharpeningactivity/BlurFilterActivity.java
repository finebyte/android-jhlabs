package com.jabistudio.androidjhlabs.blurringandsharpeningactivity;

import com.jabistudio.androidjhlabs.R;
import com.jabistudio.androidjhlabs.filter.BlurFilter;
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

public class BlurFilterActivity extends Activity implements OnClickListener{
    private static final String TITLE_NAME = "Blur";
    
    private static final int TITLE_TEXT_SIZE = 22;
    
    private LinearLayout mMainLayout;
    private ImageView mModifyImageView;
    private Bitmap mFilterBitmap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(TITLE_NAME);
        
        mMainLayout = new LinearLayout(this);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        mMainLayout.setBackgroundColor(Color.WHITE);
        
        orignalLayoutSetup(mMainLayout);
        modifyLayoutSetup(mMainLayout);
        filterButtonSetup(mMainLayout);
        
        setContentView(mMainLayout);
    }
    /**
     * originalLayoutSetting
     * @param mainLayout
     */
    private void orignalLayoutSetup(LinearLayout mainLayout){
        LinearLayout originalLayout = new LinearLayout(this);
        originalLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView originalTitleTextVeiw = new TextView(this);
        originalTitleTextVeiw.setText(R.string.original_image);
        originalTitleTextVeiw.setTextSize(TITLE_TEXT_SIZE);
        originalTitleTextVeiw.setGravity(Gravity.CENTER);
        
        ImageView originalImageView = new ImageView(this);
        originalImageView.setImageResource(R.drawable.image);
        
        originalLayout.addView(originalTitleTextVeiw);
        originalLayout.addView(originalImageView);
        
        mainLayout.addView(originalLayout);
    }
    /**
     * modifyLayoutSetting
     * @param mainLayout
     */
    private void modifyLayoutSetup(LinearLayout mainLayout){
        LinearLayout modifyLayout = new LinearLayout(this);
        modifyLayout.setOrientation(LinearLayout.VERTICAL);
        
        TextView modifyTitleTextVeiw = new TextView(this);
        modifyTitleTextVeiw.setText(R.string.modify_image);
        modifyTitleTextVeiw.setTextSize(TITLE_TEXT_SIZE);
        modifyTitleTextVeiw.setGravity(Gravity.CENTER);
        
        mModifyImageView = new ImageView(this);
        mModifyImageView.setImageResource(R.drawable.image);
        
        modifyLayout.addView(modifyTitleTextVeiw);
        modifyLayout.addView(mModifyImageView);
        
        mainLayout.addView(modifyLayout);
    }
    /**
     * filterButtonSetting
     * @param mainLayout
     */
    private void filterButtonSetup(LinearLayout mainLayout){
        Button button = new Button(this);
        button.setText(R.string.apply);
        button.setOnClickListener(this);
         
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
    }
    /**
     * ModifyView set
     */
    private void setModifyView(int[] colors, int width, int height){
        mModifyImageView.setWillNotDraw(true);
        
        if(mFilterBitmap != null){
            mFilterBitmap.recycle();
            mFilterBitmap = null;
        }
        
        mFilterBitmap = Bitmap.createBitmap(colors, 0, width, width, height, Bitmap.Config.ARGB_8888);
        mModifyImageView.setImageBitmap(mFilterBitmap);
        
        mModifyImageView.setWillNotDraw(false);
        mModifyImageView.postInvalidate();
    }
    
}

package com.jabistudio.androidjhlabs;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class SuperFilterActivity extends Activity{
    
    protected static final int TITLE_TEXT_SIZE = 22;
    
    private ScrollView mScrollView;
    
    protected LinearLayout mMainLayout;
    protected ImageView mOriginalImageView;
    protected ImageView mModifyImageView;
    protected Bitmap mFilterBitmap;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScrollView = new ScrollView(this);
        
        mMainLayout = new LinearLayout(this);
        mMainLayout.setOrientation(LinearLayout.VERTICAL);
        
        orignalLayoutSetup(mMainLayout);
        modifyLayoutSetup(mMainLayout);
        
        mScrollView.addView(mMainLayout);
        
        setContentView(mScrollView);
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
        originalTitleTextVeiw.setTextColor(Color.BLACK);
        originalTitleTextVeiw.setGravity(Gravity.CENTER);
        
        mOriginalImageView = new ImageView(this);
        mOriginalImageView.setImageResource(R.drawable.image);
        
        originalLayout.addView(originalTitleTextVeiw);
        originalLayout.addView(mOriginalImageView);
        
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
        modifyTitleTextVeiw.setTextColor(Color.BLACK);
        modifyTitleTextVeiw.setGravity(Gravity.CENTER);
        
        mModifyImageView = new ImageView(this);
        mModifyImageView.setImageResource(R.drawable.image);
        
        modifyLayout.addView(modifyTitleTextVeiw);
        modifyLayout.addView(mModifyImageView);
        
        mainLayout.addView(modifyLayout);
    }
    /**
     * ModifyView set
     */
    protected void setModifyView(int[] colors, int width, int height){
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
    @Override
    protected void onDestroy() {
        if(mFilterBitmap != null){
            mFilterBitmap.recycle();
            mFilterBitmap = null;
        }
        super.onDestroy();
    }
    
}

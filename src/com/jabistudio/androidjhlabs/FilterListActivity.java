package com.jabistudio.androidjhlabs;

import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class FilterListActivity extends Activity implements OnClickListener{
    
    private static final String ACTIVITY = "Activity";
    private static final String DOT = ".";
    
    private static final float FILTER_TEXT_SIZE = 14.6f;
    
    private ScrollView mMainScrollView;
    
    private int mDispalyHeight;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = ((WindowManager)getApplicationContext().getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
        mDispalyHeight = display.getHeight();
        
        int filterPackageNameId = getIntent().getIntExtra(AndroidjhlabsActivity.INTENT_PACKAGENAME_ID, -1);
        int filterNameArrayId = getIntent().getIntExtra(AndroidjhlabsActivity.INTENT_FILTERNAME_ARRAY_ID, -1);
        
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(getListLayout(filterPackageNameId, filterNameArrayId));
        
        mMainScrollView = new ScrollView(this);
        mMainScrollView.addView(linearLayout);
        setContentView(mMainScrollView);
    }
    
    private LinearLayout getListLayout(int activityNameId, int stringArrayId){
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        String[] filterNameArray = getResources().getStringArray(stringArrayId); 
        
        for(int i=0;i<filterNameArray.length;++i){
            TextView textView = new TextView(this);
            textView.setText(filterNameArray[i]);
            textView.setTextColor(Color.WHITE);
            textView.setTextSize(AndroidUtils.dipTopx(FILTER_TEXT_SIZE,this));
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setBackgroundResource(android.R.drawable.list_selector_background);
            textView.setClickable(true);
            textView.setOnClickListener(this);
            final String activitypackageName = getResources().getString(activityNameId);
            textView.setTag(DOT + activitypackageName + DOT + filterNameArray[i] + ACTIVITY);
            View saparator = new View(this);
            saparator.setBackgroundColor(Color.GRAY);
            
            linearLayout.addView(textView);
            linearLayout.addView(saparator);
            
            LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
            textViewParams.width = LayoutParams.FILL_PARENT;
            textViewParams.height = (int)(mDispalyHeight/7.5f);
            textView.setLayoutParams(textViewParams);
            
            LinearLayout.LayoutParams saparatorParams = (LinearLayout.LayoutParams) saparator.getLayoutParams();
            saparatorParams.width = LayoutParams.FILL_PARENT;
            saparatorParams.height = 1;
            saparator.setLayoutParams(saparatorParams);
        }
        return linearLayout;
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), getPackageName() + (String)(v.getTag()));
        startActivity(intent);
    }
}

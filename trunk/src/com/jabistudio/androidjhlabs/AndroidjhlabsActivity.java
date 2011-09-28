package com.jabistudio.androidjhlabs;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

public class AndroidjhlabsActivity extends Activity implements OnClickListener{
	
	private ScrollView mMainScrollView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = ((WindowManager)getApplicationContext().getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
        
        final int dispalyHeight = display.getHeight();
        
        mMainScrollView = new ScrollView(this);
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        String[] filterNameArray = getResources().getStringArray(R.array.coloradjustmentfiltername); 
        TextView filterTitle = new TextView(this);
        filterTitle.setTextSize(20);
        filterTitle.setTextColor(Color.WHITE);
        filterTitle.setBackgroundResource(R.drawable.title_bar);
        filterTitle.setText(R.string.color_adjustment_filter);
        
        View titleSaparator = new View(this);
        titleSaparator.setBackgroundColor(Color.GRAY);
        
        linearLayout.addView(filterTitle);
        linearLayout.addView(titleSaparator);
        
        LinearLayout.LayoutParams titleSaparatorParams = (LinearLayout.LayoutParams) titleSaparator.getLayoutParams();
        titleSaparatorParams.width = LayoutParams.FILL_PARENT;
        titleSaparatorParams.height = 1;
    	titleSaparator.setLayoutParams(titleSaparatorParams);
    	
        for(int i=0;i<filterNameArray.length;++i){
        	TextView textView = new TextView(this);
        	textView.setText(filterNameArray[i]);
        	textView.setTextColor(Color.WHITE);
        	textView.setTextSize(22);
        	textView.setGravity(Gravity.CENTER_VERTICAL);
        	textView.setBackgroundResource(android.R.drawable.list_selector_background);
        	textView.setClickable(true);
        	View saparator = new View(this);
        	saparator.setBackgroundColor(Color.GRAY);
        	
        	linearLayout.addView(textView);
        	linearLayout.addView(saparator);
        	
        	LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) textView.getLayoutParams();
        	textViewParams.width = LayoutParams.FILL_PARENT;
        	textViewParams.height = (int)(dispalyHeight/7.5f);
        	textView.setLayoutParams(textViewParams);
        	
        	LinearLayout.LayoutParams saparatorParams = (LinearLayout.LayoutParams) saparator.getLayoutParams();
        	saparatorParams.width = LayoutParams.FILL_PARENT;
        	saparatorParams.height = 1;
        	saparator.setLayoutParams(saparatorParams);
        }
        
        mMainScrollView.addView(linearLayout);
        setContentView(mMainScrollView);
    }

	@Override
	public void onClick(View v) {
		
	}
}
package com.jabistudio.androidjhlabs;

import net.daum.adam.publisher.AdView;
import net.daum.adam.publisher.AdView.AnimationType;
import net.daum.adam.publisher.AdView.OnAdClickedListener;

import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class AndroidjhlabsActivity extends Activity implements OnClickListener{
    public static final String INTENT_PACKAGENAME_ID = "packagenameid";
    public static final String INTENT_FILTERNAME_ARRAY_ID = "filternamearrayid";
    
    public static final int[] FILTER_PACKAGE_NAME_ARRAY = {R.string.coloradjustmentfilteractivity,
        R.string.distortionandwarpingactivity, R.string.effectsactivity, R.string.blurringandsharpeningactivity,
        R.string.edgedetectionactivity, R.string.alphachannelactivity};
    public static final int[] FILTER_NAME_ARRAY = {R.array.color_adjustment_filtername,
        R.array.distortion_and_warping_filtername, R.array.effects_filtername, R.array.blurring_and_sharpening_filtername, 
        R.array.edge_detection_filtername, R.array.alpha_channel_filtername};
	
	private static final float TEXT_SIZE = 14.6f;
	
	private RelativeLayout mMainLayout;
	private ScrollView mMainScrollView;
	private AdView mAdView = null;
	
	private int mDispalyHeight;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Display display = ((WindowManager)getApplicationContext().getSystemService(Activity.WINDOW_SERVICE)).getDefaultDisplay();
        mDispalyHeight = display.getHeight();
        
        mMainLayout = new RelativeLayout(this);
        
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        
        String[] titleNameArray = getResources().getStringArray(R.array.filter_title_name); 
        
        for(int i=0;i<titleNameArray.length;++i){
            setListLayout(linearLayout, titleNameArray[i],i);
        }

        mMainScrollView = new ScrollView(this);
        mMainScrollView.addView(linearLayout);
        mMainLayout.addView(mMainScrollView);
        
        setContentView(mMainLayout);
        
        adamViewSetting();
    }
    
    private void adamViewSetting(){
        // Ad@m 광고 뷰 생성 및 설정
        mAdView = new AdView(this);
        // 할당 받은 clientId 설정
        mAdView.setClientId("294bZ0yT136e2424a27");
        // 광고 갱싞 시간 : 기본 60초
        mAdView.setRequestInterval(12);
        // Animation 효과 : 기본 값은 AnimationType.NONE
        mAdView.setAnimationType(AnimationType.FLIP_HORIZONTAL);
        mAdView.setVisibility(View.VISIBLE);
        
        mMainLayout.addView(mAdView);
        
        // XML상에 android:layout_alignParentBottom="true" 와
        // 같은 역할을 함
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(mAdView.getLayoutParams());
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        // 앞에서 만든 params 레이아웃을 광고 뷰에 적용함.
        mAdView.setLayoutParams(params);
        
        
    }
    
    private void setListLayout(LinearLayout linearLayout, String titleString, int index){
        TextView filterTitle = new TextView(this);
        filterTitle.setTextSize(AndroidUtils.dipTopx(TEXT_SIZE,this));
        filterTitle.setTextColor(Color.WHITE);
        filterTitle.setGravity(Gravity.CENTER_VERTICAL);
        filterTitle.setText(titleString);
        filterTitle.setOnClickListener(this);
        filterTitle.setBackgroundResource(android.R.drawable.list_selector_background);
        filterTitle.setClickable(true);
        filterTitle.setTag(index);
        
        View titleSaparator = new View(this);
        titleSaparator.setBackgroundColor(Color.GRAY);
        
        linearLayout.addView(filterTitle);
        linearLayout.addView(titleSaparator);
        
        LinearLayout.LayoutParams textViewParams = (LinearLayout.LayoutParams) filterTitle.getLayoutParams();
        textViewParams.width = LayoutParams.FILL_PARENT;
        textViewParams.height = (int)(mDispalyHeight/7.5f);
        filterTitle.setLayoutParams(textViewParams);
        
        LinearLayout.LayoutParams titleSaparatorParams = (LinearLayout.LayoutParams) titleSaparator.getLayoutParams();
        titleSaparatorParams.width = LayoutParams.FILL_PARENT;
        titleSaparatorParams.height = 1;
        titleSaparator.setLayoutParams(titleSaparatorParams);

    }
	@Override
	public void onClick(View v) {
		int index = (Integer) v.getTag();
	    Intent intent = new Intent(AndroidjhlabsActivity.this, FilterListActivity.class);
		intent.putExtra(INTENT_PACKAGENAME_ID, FILTER_PACKAGE_NAME_ARRAY[index]);
		intent.putExtra(INTENT_FILTERNAME_ARRAY_ID, FILTER_NAME_ARRAY[index]);
		startActivity(intent);
	}

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mAdView != null){
            mAdView.destroy();
            mAdView = null;
        }
    }
	
}
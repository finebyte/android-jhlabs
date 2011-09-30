package com.jabistudio.androidjhlabs.filter.util;

import android.content.Context;
import android.util.Log;
import android.util.TypedValue;

public class AndroidUtils {
	/**
	 * dp를 px로
	 * @param dip
	 * @param context
	 * @return
	 */
	public static int dipTopx(float dip, Context context) {
	    int num = (int) TypedValue.applyDimension(
		    TypedValue.COMPLEX_UNIT_DIP, dip, 
		    context.getResources().getDisplayMetrics());
	    return num;
	}
	/**
	 * px를 dp로
	 * @param px
	 * @param context
	 * @return
	 */
	public static float pxTodip(int px, Context context) {
		float num = px / context.getResources().getDisplayMetrics().density;
	    return num;
	}
}

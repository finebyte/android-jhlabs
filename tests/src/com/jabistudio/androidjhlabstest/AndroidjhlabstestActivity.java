package com.jabistudio.androidjhlabstest;

import com.jabistudio.androidjhlabs.filter.GaussianFilter;
import com.jabistudio.androidjhlabs.filter.util.AndroidUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class AndroidjhlabstestActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageView view = new ImageView(this);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        //Find the bitmap's width height
        int width = AndroidUtils.getBitmapOfWidth(getResources(), R.drawable.ic_launcher);
        int height = AndroidUtils.getBitmapOfHeight(getResources(), R.drawable.ic_launcher);
        //Create a filter object.
        GaussianFilter filter = new GaussianFilter();
        //set???? function to specify the various settings.
        filter.setRadius(8.5f);
        //Change int Array into a bitmap
        int[] src = AndroidUtils.bitmapToIntArray(bitmap);
        //Applies a filter.
        filter.filter(src, width, height);
        //Change the Bitmap int Array (Supports only ARGB_8888)
        Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
        //Apply the image view
        view.setImageBitmap(dstBitmap);
        setContentView(view);
    }
}
## Introduction ##

Tells you how to use filters.

## Details ##
  * Project Settings
    1. libs in your project folder, create folders and files are imported androidjhlabs.jar.
    1. Androidjhlabs.jar files in the project properties to add.
  * How to code
    * GaussianFilter
```
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
```
    * Contrast
```
Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
//Find the bitmap's width height
int width = AndroidUtils.getBitmapOfWidth(getResources(), R.drawable.ic_launcher);
int height = AndroidUtils.getBitmapOfHeight(getResources(), R.drawable.ic_launcher);
ContrastFilter filter = new ContrastFilter();
filter.setBrightness(brightnessValue));
filter.setContrast(contrastValue));
//Change int Array into a bitmap
int[] src = AndroidUtils.bitmapToIntArray(bitmap);
//Applies a filter.
filter.filter(src, width, height);
//Change the Bitmap int Array (Supports only ARGB_8888)
Bitmap dstBitmap = Bitmap.createBitmap(src, width, height, Config.ARGB_8888);
```
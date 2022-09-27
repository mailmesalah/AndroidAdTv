package com.lychee.sely.adstv.extras;

/**
 * Created by Sely on 26-Dec-16.
 */

/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import android.widget.Toast;

/**
 * A collection of utility methods, all static.
 */
public class Utils {

    /*
     * Making sure public utility methods remain static
     */
    private Utils() {
    }

    /**
     * Returns the screen/display size
     */
    public static Point getDisplaySize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /**
     * Shows a (long) toast
     */
    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Shows a (long) toast.
     */
    public static void showToast(Context context, int resourceId) {
        Toast.makeText(context, context.getString(resourceId), Toast.LENGTH_LONG).show();
    }

    public static int convertDpToPixel(Context ctx, int dp) {
        float density = ctx.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * Formats time in milliseconds to hh:mm:ss string format.
     */
    public static String formatMillis(int millis) {
        String result = "";
        int hr = millis / 3600000;
        millis %= 3600000;
        int min = millis / 60000;
        millis %= 60000;
        int sec = millis / 1000;
        if (hr > 0) {
            result += hr + ":";
        }
        if (min >= 0) {
            if (min > 9) {
                result += min + ":";
            } else {
                result += "0" + min + ":";
            }
        }
        if (sec > 9) {
            result += sec;
        } else {
            result += "0" + sec;
        }
        return result;
    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static Rect getRectScaleToImageSize(Rect rFrame,Rect rImage){
        double bWidth=rImage.width();
        double bHeight=rImage.height();
        double rWidth=rFrame.width();
        double rHeight=rFrame.height();

        double scaledX=0;
        double scaledY=0;
        double scaledWidth=0;
        double scaledHeight=0;

        if(bWidth>rWidth||bHeight>rHeight){
            //Log.d("Canvas Check","Image is bigger");
            //Image is bigger
            if(((bWidth-rWidth)/bWidth)>((bHeight-rHeight)/bHeight)){
                //x2 has bigger size
                scaledWidth = rWidth;
                double val = (bWidth-rWidth)/bWidth;
                scaledHeight= (bHeight-(bHeight*val));

            }else{
                //y2 is bigger or same size
                scaledHeight = rHeight;
                double val = (bHeight-rHeight)/bHeight;
                scaledWidth= (bWidth-(bWidth*val));
            }
        }else{
            //Log.d("Canvas Check","Border is bigger");
            //Log.d("Canvas Check","Gap "+(rWidth-bWidth)+" "+(rHeight-bHeight));
            //Log.d("Canvas Check","Ratio Gap "+((rWidth-bWidth)/bWidth)+" "+((rHeight-bHeight)/bHeight));
            //image is equal sized or smaller
            if(((rWidth-bWidth)/bWidth)<(((rHeight-bHeight)/bHeight))){
                //rect width has less gap with image width
                scaledWidth = rWidth;
                double val = (rWidth-bWidth)/bWidth;
                scaledHeight= (bHeight+(bHeight*val));
                //Log.d("Canvas Check","Width less gap "+rWidth+" "+rHeight+" "+bWidth+" "+bHeight+" "+scaledHeight);

            }else{
                //rect height has less gap with image width
                scaledHeight = rHeight;
                double val = (rHeight-bHeight)/bHeight;
                scaledWidth= (bWidth+(bWidth*val));
                //Log.d("Canvas Check","Height less gap "+rWidth+" "+rHeight+" "+bWidth+" "+bHeight+" "+scaledWidth);

            }
        }

        scaledX= rFrame.left;
        scaledY=rFrame.top;
        scaledWidth+=rFrame.left;
        scaledHeight+=rFrame.top;

        return new Rect(((int)scaledX), ((int)scaledY), ((int)scaledWidth), ((int)scaledHeight));
    }

}
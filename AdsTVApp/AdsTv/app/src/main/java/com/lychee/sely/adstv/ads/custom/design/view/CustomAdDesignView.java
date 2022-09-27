package com.lychee.sely.adstv.ads.custom.design.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lychee.sely.adstv.extras.FileOperation;

import java.util.HashMap;
import java.util.Map;

public class CustomAdDesignView extends View {

    private Map<Integer,CustomAdView> ads = new HashMap<>();
    int currentAd=0;
    Paint p=new Paint();

    public CustomAdDesignView(Context context) {
        super(context);
    }

    public CustomAdDesignView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.setBackgroundColor(Color.TRANSPARENT);
        p.setColor(Color.BLUE);

        //Highlight the selected Ad
        int thr =5;
        if(currentAd!=0) {
            CustomAdView customAdView = ads.get(currentAd);
            if(customAdView!=null) {
                Rect rH = new Rect(customAdView.getX1()-thr, customAdView.getY1()-thr, customAdView.getX2()+thr, customAdView.getY2()+thr);
                p.setStrokeWidth(5);
                p.setStyle(Paint.Style.STROKE);
                canvas.drawRect(rH, p);
                //Log.d("view","working1"+currentAd+" "+customAdView.getX1()+" "+ customAdView.getY1()+" "+ customAdView.getX2()+" "+ customAdView.getY2());
            }
        }

        //Log.d("view","working"+currentAd+" "+ads.size());

    }

    public void updateAdPositions(Map<Integer,CustomAdView> ads, int currentAd){
        this.ads=ads;
        this.currentAd=currentAd;
        invalidate();
    }

}

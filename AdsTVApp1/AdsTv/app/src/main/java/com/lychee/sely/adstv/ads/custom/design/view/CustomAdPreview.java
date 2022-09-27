package com.lychee.sely.adstv.ads.custom.design.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.GeneralSettings;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO: document your custom view class.
 */
public class CustomAdPreview extends View {

    private Map<Integer,CustomAdView> ads = new HashMap<>();
    int currentAd=0;
    Paint p=new Paint();

    public CustomAdPreview(Context context) {
        super(context);
    }

    public CustomAdPreview(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        p.setColor(Color.BLUE);

        for (int i=1;i<=ads.size();++i){
            CustomAdView cav = ads.get(i);

            if(cav!=null) {
                if (cav.adType == CustomAdView.IMAGE) {
                    Bitmap bitmap = FileOperation.getBitmapFromImageFile(Resources.getSystem(), cav.getAdPath());
                    double bWidth=bitmap.getWidth();
                    double bHeight=bitmap.getHeight();
                    double rWidth=cav.getX2()-cav.getX1();
                    double rHeight=cav.getY2()-cav.getY1();

                    double scaledX=0;
                    double scaledY=0;
                    double scaledWidth=0;
                    double scaledHeight=0;

                    if(bWidth>rWidth||bHeight>rHeight){
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
                        //image is equal sized or smaller
                        if(((rWidth-bWidth)/bWidth)<(((rHeight-bHeight)/bHeight))){
                            //x2 has bigger size
                            scaledWidth = bWidth;
                            double val = (rWidth-bWidth)/rWidth;
                            scaledHeight= (rHeight-(rHeight*val));

                        }else{
                            //y2 is bigger or same size
                            scaledHeight = bHeight;
                            double val = (rHeight-bHeight)/rHeight;
                            scaledWidth= (rWidth-(rWidth*val));
                        }
                    }

                    Rect rSource = new Rect(0, 0, (int)bWidth, (int)bHeight);

                    scaledX=cav.getX1();
                    scaledY=cav.getY1();
                    scaledWidth+=cav.getX1();
                    scaledHeight+=cav.getY1();

                    //Making and positioning each Ad controls
                    double sWidth = getResources().getDisplayMetrics().widthPixels;
                    double sHeight = getResources().getDisplayMetrics().heightPixels;
                    double widthScale = GeneralSettings.CANVAS_WIDTH/sWidth;
                    double heightScale = GeneralSettings.CANVAS_HEIGHT/sHeight;

                    Rect rDestination = new Rect(((int)(scaledX*widthScale)), ((int)(scaledY*heightScale)), ((int)(scaledWidth*widthScale)), ((int)(scaledHeight*heightScale)));

                    canvas.drawBitmap(bitmap, rSource, rDestination, p);

                } else if (cav.adType == CustomAdView.VIDEO) {
                    Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), cav.getAdPath());
                    double bWidth=bitmap.getWidth();
                    double bHeight=bitmap.getHeight();
                    double rWidth=cav.getX2()-cav.getX1();
                    double rHeight=cav.getY2()-cav.getY1();

                    double scaledX=0;
                    double scaledY=0;
                    double scaledWidth=0;
                    double scaledHeight=0;

                    if(((bWidth-rWidth)/bWidth)>((bHeight-rHeight)/bHeight)){
                        //Image is bigger
                        if(bWidth-rWidth>bHeight-rHeight){
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
                        //image is equal sized or smaller
                        if(((rWidth-bWidth)/bWidth)<(((rHeight-bHeight)/bHeight))){
                            //rect width has less gap with image width
                            scaledWidth = rWidth;
                            double val = (rWidth-bWidth)/bWidth;
                            scaledHeight= (bHeight+(bHeight*val));

                        }else{
                            //rect height has less gap with image width
                            scaledHeight = rHeight;
                            double val = (rHeight-bHeight)/bHeight;
                            scaledWidth= (bWidth+(bWidth*val));
                        }
                    }

                    Rect rSource = new Rect(0, 0, (int)bWidth, (int)bHeight);

                    scaledX=cav.getX1();
                    scaledY=cav.getY1();
                    scaledWidth+=cav.getX1();
                    scaledHeight+=cav.getY1();

                    //Making and positioning each Ad controls
                    //final float scale = getResources().getDisplayMetrics().density;
                    double sWidth = getResources().getDisplayMetrics().widthPixels;
                    double sHeight = getResources().getDisplayMetrics().heightPixels;
                    double widthScale = GeneralSettings.CANVAS_WIDTH/sWidth;
                    double heightScale = GeneralSettings.CANVAS_HEIGHT/sHeight;

                    Rect rDestination = new Rect(((int)(scaledX*widthScale)), ((int)(scaledY*heightScale)), ((int)(scaledWidth*widthScale)), ((int)(scaledHeight*heightScale)));

                    canvas.drawBitmap(bitmap, rSource, rDestination, p);
                }
            }
        }

    }

    public void updateAdPositions(Map<Integer,CustomAdView> ads, int currentAd){
        this.ads=ads;
        this.currentAd=currentAd;
        invalidate();
    }

}

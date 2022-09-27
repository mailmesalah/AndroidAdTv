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

                    Rect rSource = new Rect(0, 0, (int)bWidth, (int)bHeight);

                    //Making and positioning each Ad controls
                    double sWidth = getResources().getDisplayMetrics().widthPixels;
                    double sHeight = getResources().getDisplayMetrics().heightPixels;
                    double widthScale = GeneralSettings.CANVAS_WIDTH/sWidth;
                    double heightScale = GeneralSettings.CANVAS_HEIGHT/sHeight;

                    Rect rDestination = new Rect(((int)(cav.getX1()*widthScale)), ((int)(cav.getY1()*heightScale)), ((int)(cav.getX2()*widthScale)), ((int)(cav.getY2()*heightScale)));
                    //Rect rDestination = new Rect((int)scaledX, ((int)scaledY), ((int)scaledWidth), ((int)scaledHeight));

                    canvas.drawBitmap(bitmap, rSource, rDestination, p);

                } else if (cav.adType == CustomAdView.VIDEO) {
                    Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), cav.getAdPath());
                    double bWidth=bitmap.getWidth();
                    double bHeight=bitmap.getHeight();

                    Rect rSource = new Rect(0, 0, (int)bWidth, (int)bHeight);

                    //Making and positioning each Ad controls
                    //final float scale = getResources().getDisplayMetrics().density;
                    double sWidth = getResources().getDisplayMetrics().widthPixels;
                    double sHeight = getResources().getDisplayMetrics().heightPixels;
                    double widthScale = GeneralSettings.CANVAS_WIDTH/sWidth;
                    double heightScale = GeneralSettings.CANVAS_HEIGHT/sHeight;

                    Rect rDestination = new Rect(((int)(cav.getX1()*widthScale)), ((int)(cav.getY1()*heightScale)), ((int)(cav.getX2()*widthScale)), ((int)(cav.getY2()*heightScale)));

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

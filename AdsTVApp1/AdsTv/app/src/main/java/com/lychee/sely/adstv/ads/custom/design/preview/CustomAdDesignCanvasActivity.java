package com.lychee.sely.adstv.ads.custom.design.preview;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.design.view.CustomAdDesignView;
import com.lychee.sely.adstv.ads.custom.design.view.CustomAdView;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.Utils;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.util.HashMap;
import java.util.Map;

public class CustomAdDesignCanvasActivity extends FragmentActivity {
    public final static int MIN_WIDTH=10;
    public final static int MIN_HEIGHT=10;

    public final static int POS_NONE=1;
    public final static int POS_LEFT=2;
    public final static int POS_RIGHT=3;
    public final static int POS_TOP=4;
    public final static int POS_BOTTOM=5;
    private static int MAX_HEIGHT ;
    private static int MAX_WIDTH ;

    private int currentAd=0;
    private int currentPosition=POS_NONE;

    private Map<Integer,CustomAdView> adItems = new HashMap<>();
    private Map<Integer,View> adViews = new HashMap<>();

    private long mAdID=0;
    private FloatingActionButton fabChangeSelection;
    private CustomAdDesignView canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ad_design_canvas);

        final FrameLayout layout = (FrameLayout) findViewById(R.id.activity_custom_ad_preview);
        canvas = (CustomAdDesignView) findViewById(R.id.custom_ad_design_view);

        fabChangeSelection = (FloatingActionButton)findViewById(R.id.fab_change_selection);

        //Get screen resolution details
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        MAX_HEIGHT = displayMetrics.heightPixels;
        MAX_WIDTH = displayMetrics.widthPixels;

        mAdID=getIntent().getLongExtra("AD_ID",0);

        //Ad Items
        Cursor cAdItems = getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{mAdID+""}, "ZOrder");
        if(cAdItems!=null && cAdItems.getCount()>0){
            cAdItems.moveToFirst();
            do {
                CustomAdView cav = new CustomAdView();
                cav.setAdId(cAdItems.getInt(cAdItems.getColumnIndex("_ID")));
                cav.setAdName(cAdItems.getString(cAdItems.getColumnIndex("AdName")));
                cav.setAdPath(cAdItems.getString(cAdItems.getColumnIndex("AdPath")));
                cav.setAdType(cAdItems.getInt(cAdItems.getColumnIndex("AdType")));
                cav.setZOrder(cAdItems.getInt(cAdItems.getColumnIndex("ZOrder")));
                cav.setX1(cAdItems.getInt(cAdItems.getColumnIndex("X")));
                cav.setY1(cAdItems.getInt(cAdItems.getColumnIndex("Y")));
                cav.setX2(cAdItems.getInt(cAdItems.getColumnIndex("Width")));
                cav.setY2(cAdItems.getInt(cAdItems.getColumnIndex("Height")));
                //Add the item to the map
                adItems.put(cav.getZOrder(), cav);
            }while (cAdItems.moveToNext());
        }
        cAdItems.close();

        //arranging ads according to their positions
        for (int i=1;i<=adItems.size();++i) {
            CustomAdView cav = adItems.get(i);
            if(cav!=null){
                if(cav.getAdType()==CustomAdView.IMAGE){
                    Bitmap bitmap = FileOperation.getBitmapFromImageFile(Resources.getSystem(), cav.getAdPath());
                    Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                    Rect rFrame = new Rect(cav.getX1(),cav.getY1(),cav.getX2(),cav.getY2());
                    Rect rScaled = Utils.getRectScaleToImageSize(rFrame,rImage);

                    ImageView ivAd = new ImageView(this);
                    FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(layout.getLayoutParams());
                    param.leftMargin = rScaled.left;
                    param.topMargin = rScaled.top;
                    param.width = rScaled.width();
                    param.height = rScaled.height();
                    ivAd.setLayoutParams(param);
                    ivAd.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),cav.getAdPath()));
                    //Adding control to the layout
                    layout.addView(ivAd);
                    adViews.put(i,ivAd);
                }else if(cav.getAdType()==CustomAdView.VIDEO){
                    Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), cav.getAdPath());
                    Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                    Rect rFrame = new Rect(cav.getX1(),cav.getY1(),cav.getX2(),cav.getY2());
                    Rect rScaled = Utils.getRectScaleToImageSize(rFrame,rImage);

                    VideoView vvAd = new VideoView(this);
                    FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(layout.getLayoutParams());
                    //param.leftMargin = Utils.pxToDp((int)(rScaled.left*widthScale),this);
                    //param.topMargin = Utils.pxToDp((int)(rScaled.top*heightScale),this);
                    param.leftMargin = rScaled.left;
                    param.topMargin = rScaled.top;
                    param.width = rScaled.width();
                    param.height = rScaled.height();
                    vvAd.setLayoutParams(param);
                    //vvAd.setZ(i);
                    //vvAd.bringToFront();
                    vvAd.setVideoPath(cav.getAdPath());
                    vvAd.start();
                    //Adding control to the layout
                    layout.addView(vvAd);
                    adViews.put(i,vvAd);
                }
            }
        }

        //Draw the selection
        currentAd=1;
        canvas.updateAdPositions(adItems,currentAd);
        canvas.bringToFront();

        //Drawing section
        canvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int threshold =10;
                switch (event.getActionMasked()){
                    case MotionEvent.ACTION_DOWN:
                    {

                        CustomAdView customAdView = adItems.get(currentAd);
                        if(customAdView!=null) {
                            //Left Line
                            if (event.getX()>=customAdView.getX1()-threshold && event.getX()<=customAdView.getX1()+threshold){
                                if (event.getY()>=customAdView.getY1() && event.getY()<=customAdView.getY2()){
                                    currentPosition=POS_LEFT;
                                }
                            }

                            //Top Line
                            else if (event.getY()>=customAdView.getY1()-threshold && event.getY()<=customAdView.getY1()+threshold){
                                if (event.getX()>=customAdView.getX1() && event.getX()<=customAdView.getX2()){
                                    currentPosition=POS_TOP;
                                }
                            }

                            //Right Line
                            else if (event.getX()>=customAdView.getX2()-threshold && event.getX()<=customAdView.getX2()+threshold){
                                if (event.getY()>=customAdView.getY1() && event.getY()<=customAdView.getY2()){
                                    currentPosition=POS_RIGHT;
                                }
                            }

                            //Bottom Line
                            else if (event.getY()>=customAdView.getY2()-threshold && event.getY()<=customAdView.getY2()+threshold){
                                if (event.getX()>=customAdView.getX1() && event.getX()<=customAdView.getX2()){
                                    currentPosition=POS_BOTTOM;
                                }
                            }
                            return true;
                        }

                    }

                    case MotionEvent.ACTION_MOVE:
                    {
                        CustomAdView customAdView = adItems.get(currentAd);

                        //Check if the mouse is out of canvas area
                        if(event.getX()> MAX_WIDTH-5 || event.getY()> MAX_HEIGHT-5 || event.getX()<5 || event.getY()<5){
                            //exit method
                            return true;
                        }


                        if(currentPosition==POS_LEFT){
                            if(customAdView.getX2()-event.getX()<MIN_WIDTH) {
                                //exit
                                return true;
                            }

                            customAdView.setX1((int) event.getX());
                        }else if(currentPosition==POS_RIGHT){
                            if(event.getX()-customAdView.getX1()<MIN_WIDTH) {
                                //exit
                                return true;
                            }

                            customAdView.setX2((int)event.getX());
                        }else if(currentPosition==POS_TOP){
                            if(customAdView.getY2()-event.getY()<MIN_HEIGHT) {
                                //exit
                                return true;
                            }

                            customAdView.setY1((int)event.getY());
                        }else if(currentPosition==POS_BOTTOM){
                            if(event.getY()-customAdView.getY1()<MIN_HEIGHT) {
                                //exit
                                return true;
                            }

                            customAdView.setY2((int)event.getY());
                        }


                        //Update selected view position
                        FrameLayout.LayoutParams param = (FrameLayout.LayoutParams) adViews.get(currentAd).getLayoutParams();
                        Rect rScaled= new Rect();
                        if(customAdView.getAdType()==CustomAdView.IMAGE){
                            Bitmap bitmap = FileOperation.getBitmapFromImageFile(Resources.getSystem(), customAdView.getAdPath());
                            Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                            Rect rFrame = new Rect(customAdView.getX1(),customAdView.getY1(),customAdView.getX2(),customAdView.getY2());
                            rScaled = Utils.getRectScaleToImageSize(rFrame,rImage);

                            param.leftMargin = rScaled.left;
                            param.topMargin = rScaled.top;
                            param.width = rScaled.width();
                            param.height = rScaled.height();
                            adViews.get(currentAd).setLayoutParams(param);


                        }else if(customAdView.getAdType()==CustomAdView.VIDEO){
                            ((VideoView)adViews.get(currentAd)).stopPlayback();
                            Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), customAdView.getAdPath());
                            Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                            Rect rFrame = new Rect(customAdView.getX1(),customAdView.getY1(),customAdView.getX2(),customAdView.getY2());
                            rScaled = Utils.getRectScaleToImageSize(rFrame,rImage);

                            param.leftMargin = rScaled.left;
                            param.topMargin = rScaled.top;
                            param.width = rScaled.width();
                            param.height = rScaled.height();
                            adViews.get(currentAd).setLayoutParams(param);

                            ((VideoView)adViews.get(currentAd)).start();
                        }

                        //Draw the Selection
                        canvas.updateAdPositions(adItems,currentAd);
                        layout.invalidate();
                        //adViews.get(currentAd).getLayoutParams();
                        return true;
                    }

                    case MotionEvent.ACTION_UP:
                    {
                        currentPosition=POS_NONE;
                        CustomAdView customAdView = adItems.get(currentAd);
                        if(customAdView!=null) {
                            //Update the new position
                            ContentValues cv = new ContentValues();
                            cv.put("X", customAdView.getX1());
                            cv.put("Y", customAdView.getY1());
                            cv.put("Width", customAdView.getX2());
                            cv.put("Height", customAdView.getY2());
                            getContentResolver().update(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, cv, "AdId=? and _ID=?", new String[]{mAdID + "", customAdView.getAdId()+"" });
                        }
                        return true;
                    }

                    case MotionEvent.ACTION_CANCEL:
                    {
                        Log.d("Cancel",event.getX()+" "+event.getY()+"");
                        return true;
                    }

                    default:
                }

                return false;
            }
        });

        //Register FAB
        registerForContextMenu(fabChangeSelection);
        fabChangeSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.showContextMenu();
            }
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select Ad");
        for (int i=1;i<=adItems.size();++i){
            CustomAdView customAdView = adItems.get(i);
            menu.add(0, i, 0, customAdView.getZOrder()+". "+customAdView.getAdName());
        }
        //menu.add(0, v.getId(), 0, "SMS");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        CustomAdView customAdView = adItems.get(item.getItemId());
        if(customAdView!=null){
            currentAd=item.getItemId();
            canvas.updateAdPositions(adItems,currentAd);
            return true;
        }

        return false;
    }
}

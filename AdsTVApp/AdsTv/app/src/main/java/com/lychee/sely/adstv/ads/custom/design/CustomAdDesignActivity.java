package com.lychee.sely.adstv.ads.custom.design;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v17.leanback.app.VerticalGridFragment;
import android.support.v17.leanback.widget.OnItemViewSelectedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.ads.custom.design.preview.CustomAdDesignCanvasActivity;
import com.lychee.sely.adstv.ads.custom.design.view.CustomAdPreview;
import com.lychee.sely.adstv.ads.custom.design.view.CustomAdView;
import com.lychee.sely.adstv.ads.image.ImageAdActivity;
import com.lychee.sely.adstv.ads.video.VideoAdActivity;
import com.lychee.sely.adstv.storage.tables.CustomDetailAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.util.HashMap;
import java.util.Map;

public class CustomAdDesignActivity extends FragmentActivity {

    public final static int MIN_WIDTH=300;
    public final static int MIN_HEIGHT=300;

    //For Image and video ad selection result
    public final static int IMAGE_AD_RESULT=1;
    public final static int VIDEO_AD_RESULT=2;

    public final static int POS_NONE=1;
    public final static int POS_LEFT=2;
    public final static int POS_RIGHT=3;
    public final static int POS_TOP=4;
    public final static int POS_BOTTOM=5;

    public long mAdID;
    private EditText etCustomeAdName;
    private Button btCreateCustomAd;
    private Button btAddImageAdCustomAd;
    private Button btAddVideoAdCustomAd;

    private Map<Integer,CustomAdView> adItems = new HashMap<>();
    private int currentAd=0;
    private int currentPosition=POS_NONE;
    private Button btCustomAdPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ad_design);

        mAdID=getIntent().getLongExtra("AD_ID",0);

        //Canvas
        final CustomAdPreview cadv = (CustomAdPreview) findViewById(R.id.custom_ad_preview_view);

        etCustomeAdName = (EditText) findViewById(R.id.edit_text_custom_ad_name);
        etCustomeAdName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Update database with new data
                if(mAdID!=0) {
                    if (etCustomeAdName.getText().toString().trim() != "") {
                        ContentValues cv = new ContentValues();
                        cv.put("AdName", etCustomeAdName.getText().toString().trim());
                        getContentResolver().update(ProgramAdContentProvider.CUSTOM_AD_URI, cv, "_ID=?", new String[]{mAdID + ""});
                    }
                }
                return false;
            }
        });

        btCreateCustomAd=(Button)findViewById(R.id.create_custom_ad);
        btCreateCustomAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etCustomeAdName.getText().toString().trim().length()==0){
                    Toast.makeText(getBaseContext(), "Please Provide Ad Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                ContentValues cv = new ContentValues();
                cv.put("AdName",etCustomeAdName.getText().toString().trim());

                Uri uri = getContentResolver().insert(ProgramAdContentProvider.CUSTOM_AD_URI, cv);
                mAdID = Long.parseLong(uri.getLastPathSegment());

                //Sending for fragment
                Intent retIntent = new Intent(CustomAdDesignFragment.REC_DATA);
                retIntent.putExtra("AD_ID", mAdID);
                sendBroadcast(retIntent);
                //Update controls
                setVisibilityOfControls();
            }
        });

        btAddImageAdCustomAd=(Button)findViewById(R.id.custom_ad_add_image_ad);
        btAddImageAdCustomAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ImageAdActivity.class);
                startActivityForResult(i, IMAGE_AD_RESULT);
            }
        });

        btAddVideoAdCustomAd=(Button)findViewById(R.id.custom_ad_add_video_ad);
        btAddVideoAdCustomAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), VideoAdActivity.class);
                startActivityForResult(i, VIDEO_AD_RESULT);
            }
        });

        btCustomAdPreview=(Button)findViewById(R.id.custom_ad_preview);
        btCustomAdPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mAdID!=0) {
                    Intent i = new Intent(getBaseContext(), CustomAdDesignCanvasActivity.class);
                    i.putExtra("AD_ID", mAdID);
                    startActivity(i);
                }
            }
        });

        VerticalGridFragment fCustomAdItems = (VerticalGridFragment)getFragmentManager().findFragmentById(R.id.vertical_grid_fragment_custom_ad_design_items);
        fCustomAdItems.setOnItemViewSelectedListener(new OnItemViewSelectedListener() {
            @Override
            public void onItemSelected(Presenter.ViewHolder itemViewHolder, Object item, RowPresenter.ViewHolder rowViewHolder, Row row) {
                CustomAdItem cav = (CustomAdItem) item;
                if(cav!=null) {
                    currentAd = cav.getSerialNo();
                    cadv.updateAdPositions(adItems, currentAd);
                }
            }
        });


        setVisibilityOfControls();

        Cursor cursor = getContentResolver().query(ProgramAdContentProvider.CUSTOM_AD_URI, null, "_ID=?", new String[]{mAdID+""}, null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            etCustomeAdName.setText(cursor.getString(cursor.getColumnIndex("AdName")));
        }
        cursor.close();
    }

    public void updateAdItemMaps(){
        adItems.clear();
        //Load Ad Items to canvas ready
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
    }

    private void setVisibilityOfControls(){
        if(mAdID==0){
            btCreateCustomAd.setVisibility(View.VISIBLE);
            btAddImageAdCustomAd.setVisibility(View.GONE);
            btAddVideoAdCustomAd.setVisibility(View.GONE);
            btCustomAdPreview.setVisibility(View.GONE);
        }else{
            btCreateCustomAd.setVisibility(View.GONE);
            btAddImageAdCustomAd.setVisibility(View.VISIBLE);
            btAddVideoAdCustomAd.setVisibility(View.VISIBLE);
            btCustomAdPreview.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==IMAGE_AD_RESULT && resultCode == Activity.RESULT_OK){
            long imageAdId = data.getLongExtra("AD_ID", 0);

            Cursor cAd = getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{mAdID + ""}, null);
            int zOrder=1;
            if(cAd!=null){
                zOrder=cAd.getCount()+1;
            }
            cAd.close();

            //Update the image with new one
            Cursor c = getContentResolver().query(ProgramAdContentProvider.IMAGE_AD_URI, null, "_ID=?", new String[]{imageAdId + ""}, null);
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                ContentValues cv = new ContentValues();
                cv.put("AdId",mAdID);
                cv.put("X",10);
                cv.put("Y", 10);
                cv.put("Width",MIN_WIDTH);
                cv.put("Height", MIN_HEIGHT);
                cv.put("ZOrder",zOrder);
                cv.put("AdType", CustomDetailAd.AD_TYPE_IMAGE);
                cv.put("AdName",c.getString(c.getColumnIndex("AdName")));
                cv.put("AdPath",c.getString(c.getColumnIndex("AdPath")));
                c.close();

                getContentResolver().insert(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI,cv);
                Log.i("CustomAdDesignActivity", "Saved "+mAdID+" "+zOrder);
            }else{
                c.close();
            }



        }else if(requestCode==VIDEO_AD_RESULT && resultCode == Activity.RESULT_OK){
            long videoAdId = data.getLongExtra("AD_ID", 0);

            Cursor cAd = getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{mAdID + ""}, null);
            int zOrder=1;
            if(cAd!=null){
                zOrder=cAd.getCount()+1;
            }
            cAd.close();

            //Update the image with new one
            Cursor c = getContentResolver().query(ProgramAdContentProvider.VIDEO_AD_URI, null, "_ID=?", new String[]{videoAdId + ""}, null);
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                ContentValues cv = new ContentValues();
                cv.put("AdId",mAdID);
                cv.put("X",10);
                cv.put("Y", 10);
                cv.put("Width",MIN_WIDTH);
                cv.put("Height", MIN_HEIGHT);
                cv.put("ZOrder",zOrder);
                cv.put("AdType", CustomDetailAd.AD_TYPE_VIDEO);
                cv.put("AdName",c.getString(c.getColumnIndex("AdName")));
                cv.put("AdPath",c.getString(c.getColumnIndex("AdPath")));
                c.close();

                getContentResolver().insert(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI,cv);
            }else{
                c.close();
            }

        }
    }
}

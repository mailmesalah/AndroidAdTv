package com.lychee.sely.adstv.stackedadprogram.ads;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.video.VideoAdActivity;
import com.lychee.sely.adstv.ads.video.VideoAdFragment;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.storage.tables.StackedAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

public class StackedAdVideoActivity extends FragmentActivity {

    EditText metHours;
    EditText metMinutes;
    EditText metSeconds;
    Button mbBrowseVideo;
    ImageCardView micvVideoThumbnail;

    private long mProgramID;
    private long mStackedAdItemID;
    private long mAdID;
    private Button mbCreateStackedAdVideo;
    private int mSerialNo;
    private TextView mtvAdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_ad_video);

        mProgramID=getIntent().getLongExtra("PROGRAM_ID",0);
        mStackedAdItemID=getIntent().getLongExtra("STACKED_AD_ITEM_ID",0);
        mAdID=getIntent().getLongExtra("AD_ID",0);
        mSerialNo=getIntent().getIntExtra("SERIAL_NO",0);

        mtvAdName = (TextView) findViewById(R.id.text_view_ad_name_stacked_ad_video);

        metHours = (EditText) findViewById(R.id.edit_text_stacked_ad_hours);
        metMinutes = (EditText) findViewById(R.id.edit_text_stacked_ad_minutes);
        metSeconds = (EditText) findViewById(R.id.edit_text_stacked_ad_seconds);

        micvVideoThumbnail = (ImageCardView) findViewById(R.id.image_card_view_video);

        mbBrowseVideo = (Button) findViewById(R.id.browse_stacked_ad_video);
        mbCreateStackedAdVideo = (Button) findViewById(R.id.create_stacked_ad_video);

        mbBrowseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), VideoAdActivity.class);
                startActivityForResult(i, VideoAdFragment.AD_SELECTED);
            }
        });

        mbCreateStackedAdVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hours,minutes,seconds;
                try {
                    hours = Integer.parseInt(metHours.getText().toString());
                    minutes = Integer.parseInt(metMinutes.getText().toString());
                    seconds = Integer.parseInt(metSeconds.getText().toString());
                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Please Give Valid Time Period.",Toast.LENGTH_SHORT).show();
                    metHours.requestFocus();
                    return;
                }

                if(!(mAdID>0)){
                    Toast.makeText(getBaseContext(),"Video Ad is not selected.",Toast.LENGTH_SHORT).show();
                    mbBrowseVideo.requestFocus();
                    return;
                }

                //If program id is invalid close the window
                if(!(mProgramID>0)){
                    Toast.makeText(getBaseContext(),"Wrong Program ID.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                //create stacked ad with the program id and ad id data

                Cursor cVideoAd = getContentResolver().query(ProgramAdContentProvider.VIDEO_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
                if(cVideoAd!=null&&cVideoAd.getCount()>0){
                    cVideoAd.moveToFirst();
                    String videoPath=cVideoAd.getString(cVideoAd.getColumnIndex("AdPath"));
                    cVideoAd.close();
                    //get serial no
                    if(mSerialNo==0) {
                        Cursor cStackAd = getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "ProgramCode=?", new String[]{mProgramID + ""}, null);
                        if (cStackAd != null) {
                            mSerialNo = cStackAd.getCount()+1;
                            cStackAd.close();
                        }

                        //Add to Stacked Ad
                        ContentValues cv = new ContentValues();
                        cv.put("SerialNo",mSerialNo);
                        cv.put("ProgramCode",mProgramID);
                        cv.put("AdType", StackedAd.AD_TYPE_VIDEO);
                        cv.put("AdCode",mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
                        cv.put("AdPath",videoPath);
                        cv.put("AdTimePeriod",FileOperation.getTotalTimeInSec(hours,minutes,seconds));

                        getContentResolver().insert(ProgramAdContentProvider.STACKED_AD_URI,cv);
                    }else{
                        //Update Stacked Ad
                        ContentValues cv = new ContentValues();
                        cv.put("AdCode",mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
                        cv.put("AdPath",videoPath);
                        cv.put("AdTimePeriod",FileOperation.getTotalTimeInSec(hours,minutes,seconds));

                        getContentResolver().update(ProgramAdContentProvider.STACKED_AD_URI,cv,"_ID=?",new String[]{mStackedAdItemID+""});
                    }

                    //close the window
                    finish();
                }
            }
        });

        //If Open from details instead of creation Populate the datas
        if(mStackedAdItemID>0) {
            Cursor c = getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "_ID=?", new String[]{mStackedAdItemID + ""}, null);
            if(c!=null&&c.getCount()>0){
                c.moveToFirst();

                mtvAdName.setText(c.getString(c.getColumnIndex("AdName"))+"");
                String videoPath = c.getString(c.getColumnIndex("AdPath"));
                int timePeriod = c.getInt(c.getColumnIndex("AdTimePeriod"));
                c.close();

                Drawable dImage = FileOperation.getThumbnailFromVideoFile(Resources.getSystem(), videoPath);
                micvVideoThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                micvVideoThumbnail.setMainImage(dImage);
                metHours.setText(FileOperation.getHoursFromTimePeriod(timePeriod)+"");
                metMinutes.setText(FileOperation.getMinutesFromTimePeriod(timePeriod)+"");
                metSeconds.setText(FileOperation.getSecondsFromTimePeriod(timePeriod)+"");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==VideoAdFragment.AD_SELECTED && resultCode == Activity.RESULT_OK){
            mAdID=data.getLongExtra("AD_ID",0);
            //Update the video with new one
            Cursor c = getContentResolver().query(ProgramAdContentProvider.VIDEO_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                mtvAdName.setText(c.getString(c.getColumnIndex("AdName")));
                String videoPath = c.getString(c.getColumnIndex("AdPath"));
                c.close();

                Drawable dImage = FileOperation.getThumbnailFromVideoFile(Resources.getSystem(), videoPath);
                micvVideoThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                micvVideoThumbnail.setMainImage(dImage);

            }

        }
    }
}

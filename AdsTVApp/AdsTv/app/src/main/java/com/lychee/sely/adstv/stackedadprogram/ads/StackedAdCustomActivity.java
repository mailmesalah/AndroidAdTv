package com.lychee.sely.adstv.stackedadprogram.ads;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v17.leanback.widget.ImageCardView;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.CustomAdActivity;
import com.lychee.sely.adstv.ads.custom.CustomAdFragment;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.storage.tables.StackedAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 18-May-17.
 */
public class StackedAdCustomActivity extends FragmentActivity {

    EditText metHours;
    EditText metMinutes;
    EditText metSeconds;
    Button mbBrowseCustom;
    ImageCardView micvCustomThumbnail;

    private long mProgramID;
    private long mStackedAdItemID;
    private long mAdID;
    private Button mbCreateStackedAdCustom;
    private int mSerialNo;
    private TextView mtvAdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_ad_custom);

        mProgramID=getIntent().getLongExtra("PROGRAM_ID",0);
        mStackedAdItemID=getIntent().getLongExtra("STACKED_AD_ITEM_ID",0);
        mAdID=getIntent().getLongExtra("AD_ID",0);
        mSerialNo=getIntent().getIntExtra("SERIAL_NO",0);

        mtvAdName = (TextView) findViewById(R.id.text_view_ad_name_stacked_ad_custom);

        metHours = (EditText) findViewById(R.id.edit_text_stacked_ad_hours);
        metMinutes = (EditText) findViewById(R.id.edit_text_stacked_ad_minutes);
        metSeconds = (EditText) findViewById(R.id.edit_text_stacked_ad_seconds);

        micvCustomThumbnail = (ImageCardView) findViewById(R.id.image_card_view_custom);

        mbBrowseCustom = (Button) findViewById(R.id.browse_stacked_ad_custom);
        mbCreateStackedAdCustom = (Button) findViewById(R.id.create_stacked_ad_custom);

        mbBrowseCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CustomAdActivity.class);
                startActivityForResult(i, CustomAdFragment.AD_SELECTED);
            }
        });

        mbCreateStackedAdCustom.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(getBaseContext(),"Custom Ad is not selected.",Toast.LENGTH_SHORT).show();
                    mbBrowseCustom.requestFocus();
                    return;
                }

                //If program id is invalid close the window
                if(!(mProgramID>0)){
                    Toast.makeText(getBaseContext(),"Wrong Program ID.",Toast.LENGTH_SHORT).show();
                    finish();
                }

                //create stacked ad with the program id and ad id data

                Cursor cCustomAd = getContentResolver().query(ProgramAdContentProvider.CUSTOM_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
                if(cCustomAd!=null&&cCustomAd.getCount()>0){
                    cCustomAd.close();
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
                        cv.put("AdType", StackedAd.AD_TYPE_CUSTOM);
                        cv.put("AdCode",mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
                        cv.put("AdPath", "");
                        cv.put("AdTimePeriod",FileOperation.getTotalTimeInSec(hours,minutes,seconds));

                        getContentResolver().insert(ProgramAdContentProvider.STACKED_AD_URI,cv);
                    }else{
                        //Update Stacked Ad
                        ContentValues cv = new ContentValues();
                        cv.put("AdCode",mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
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

                mtvAdName.setText(c.getString(c.getColumnIndex("AdName")));
                int timePeriod = c.getInt(c.getColumnIndex("AdTimePeriod"));
                c.close();

                /*Drawable dImage = FileOperation.getThumbnailFromCustomFile(Resources.getSystem(), customPath);
                micvCustomThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                micvCustomThumbnail.setMainImage(dImage);*/
                metHours.setText(FileOperation.getHoursFromTimePeriod(timePeriod)+"");
                metMinutes.setText(FileOperation.getMinutesFromTimePeriod(timePeriod)+"");
                metSeconds.setText(FileOperation.getSecondsFromTimePeriod(timePeriod)+"");
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CustomAdFragment.AD_SELECTED && resultCode == Activity.RESULT_OK){
            mAdID=data.getLongExtra("AD_ID",0);
            //Update the custom with new one
            Cursor c = getContentResolver().query(ProgramAdContentProvider.CUSTOM_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
            if(c!=null && c.getCount()>0){
                c.moveToFirst();
                mtvAdName.setText(c.getString(c.getColumnIndex("AdName")));
                c.close();

                /*Drawable dImage = FileOperation.getThumbnailFromCustomFile(Resources.getSystem(), customPath);
                micvCustomThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                micvCustomThumbnail.setMainImage(dImage);
                */
            }

        }
    }
}

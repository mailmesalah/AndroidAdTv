package com.lychee.sely.adstv.stackedadprogram;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdCustomActivity;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdImageActivity;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdVideoActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

public class StackedAdProgramActivity extends FragmentActivity {
    private static final String TAG = StackedAdProgramActivity.class.getSimpleName();
    //static values for Activity results
    private static final int RESULT_LOAD_IMAGE=1;
    private static final int RESULT_LOAD_VIDEO=2;
    private static final int RESULT_LOAD_CUSTOM=3;

    private long mProgramID;

    Button bAddImageAd;
    Button bAddVideoAd;
    private Button bAddCustomAd;
    private EditText etProgramName;
    Switch swRepeat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_ad_program);

        getWindow().setBackgroundDrawableResource(R.color.window_background);

        bAddImageAd= (Button) findViewById(R.id.stacked_ad_program_add_image_ad);
        bAddVideoAd= (Button) findViewById(R.id.stacked_ad_program_add_video_ad);
        bAddCustomAd= (Button) findViewById(R.id.stacked_ad_program_add_custom_ad);
        etProgramName=(EditText)findViewById(R.id.stacked_ad_program_name_edit_text);
        swRepeat = (Switch) findViewById(R.id.repeat_program_name_switch);
        mProgramID=getIntent().getLongExtra("PROGRAM_ID",0);

        //Loading the datas from database
        Cursor cursor = getContentResolver().query(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, null, "_ID=?", new String[]{mProgramID+""}, null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            etProgramName.setText(cursor.getString(cursor.getColumnIndex("ProgramName")));
            swRepeat.setChecked(cursor.getInt(cursor.getColumnIndex("Repeat"))==1);
        }
        cursor.close();

        //click listeners
        bAddImageAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), StackedAdImageActivity.class);
                i.putExtra("PROGRAM_ID",mProgramID);
                startActivity(i);
            }
        });

        bAddVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), StackedAdVideoActivity.class);
                i.putExtra("PROGRAM_ID",mProgramID);
                startActivity(i);
            }
        });

        bAddCustomAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), StackedAdCustomActivity.class);
                i.putExtra("PROGRAM_ID",mProgramID);
                startActivity(i);
            }
        });

        swRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Update Repeat in database
                ContentValues cv = new ContentValues();
                cv.put("Repeat",swRepeat.isChecked()?1:0);
                getContentResolver().update(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI,cv,"_ID=?",new String[]{mProgramID+""});
            }
        });

        etProgramName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Update database with new data
                if(etProgramName.getText().toString().trim()!="") {
                    ContentValues cv = new ContentValues();
                    cv.put("ProgramName", etProgramName.getText().toString().trim());
                    getContentResolver().update(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, cv, "_ID=?", new String[]{mProgramID + ""});
                }
                return false;
            }
        });

    }

}

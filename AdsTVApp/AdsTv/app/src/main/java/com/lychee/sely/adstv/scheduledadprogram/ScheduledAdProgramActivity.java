package com.lychee.sely.adstv.scheduledadprogram;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdCustomActivity;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdImageActivity;
import com.lychee.sely.adstv.scheduledadprogram.ads.ScheduledAdVideoActivity;
import com.lychee.sely.adstv.stackedadprogram.ads.StackedAdCustomActivity;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAdProgram;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

/**
 * Created by Sely on 25-Jan-17.
 */
public class ScheduledAdProgramActivity extends FragmentActivity {
    private static final String TAG = ScheduledAdProgramActivity.class.getSimpleName();
    //static values for Activity results
    private static final int RESULT_LOAD_IMAGE=1;
    private static final int RESULT_LOAD_VIDEO=2;

    private long mProgramID;

    Button bAddImageAd;
    Button bAddVideoAd;
    private EditText etProgramName;
    Switch swRepeat;
    private Spinner msRepeatMask;
    private Button bAddCustomAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_ad_program);

        getWindow().setBackgroundDrawableResource(R.color.window_background);

        bAddImageAd= (Button) findViewById(R.id.scheduled_ad_program_add_image_ad);
        bAddVideoAd= (Button) findViewById(R.id.scheduled_ad_program_add_video_ad);
        bAddCustomAd= (Button) findViewById(R.id.scheduled_ad_program_add_custom_ad);
        etProgramName=(EditText)findViewById(R.id.scheduled_ad_program_name_edit_text);
        swRepeat = (Switch) findViewById(R.id.repeat_program_name_switch);
        msRepeatMask = (Spinner) findViewById(R.id.spinner_repeat_mask);

        mProgramID=getIntent().getLongExtra("PROGRAM_ID",0);

        //Populate combobox(Spinner)
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, new String[]{"YEARLY", "MONTHLY", "DAILY"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        msRepeatMask.setAdapter(adapter);

        //Loading the datas from database
        Cursor cursor = getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, null, "_ID=?", new String[]{mProgramID+""}, null);
        if(cursor!=null && cursor.getCount()>0){
            cursor.moveToFirst();
            etProgramName.setText(cursor.getString(cursor.getColumnIndex("ProgramName")));
            swRepeat.setChecked(cursor.getInt(cursor.getColumnIndex("Repeat"))==1);
            int type=cursor.getInt(cursor.getColumnIndex("RepeatType"));
            //Setting Repeat mask spinner
            if(type== ScheduledAdProgram.REPEAT_TYPE_NONE || type== ScheduledAdProgram.REPEAT_TYPE_YEARLY){
                msRepeatMask.setSelection(ScheduledAdProgram.REPEAT_TYPE_YEARLY);
            }else if(type== ScheduledAdProgram.REPEAT_TYPE_MONTHLY){
                msRepeatMask.setSelection(ScheduledAdProgram.REPEAT_TYPE_MONTHLY);
            }else if(type== ScheduledAdProgram.REPEAT_TYPE_DAILY){
                msRepeatMask.setSelection(ScheduledAdProgram.REPEAT_TYPE_DAILY);
            }
            msRepeatMask.setVisibility(swRepeat.isChecked()?View.VISIBLE:View.GONE);

            cursor.close();
        }


        //click listeners
        bAddImageAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ScheduledAdImageActivity.class);
                i.putExtra("PROGRAM_ID",mProgramID);
                startActivity(i);
            }
        });

        bAddVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ScheduledAdVideoActivity.class);
                i.putExtra("PROGRAM_ID",mProgramID);
                startActivity(i);
            }
        });

        bAddCustomAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), ScheduledAdCustomActivity.class);
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
                getContentResolver().update(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI,cv,"_ID=?",new String[]{mProgramID+""});

                msRepeatMask.setVisibility(swRepeat.isChecked()?View.VISIBLE:View.GONE);
            }
        });

        etProgramName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //Update database with new data
                if(etProgramName.getText().toString().trim()!="") {
                    ContentValues cv = new ContentValues();
                    cv.put("ProgramName", etProgramName.getText().toString().trim());
                    getContentResolver().update(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, cv, "_ID=?", new String[]{mProgramID + ""});
                }
                return false;
            }
        });

        msRepeatMask.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    msRepeatMask.performClick();
                }
            }
        });

        msRepeatMask.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ContentValues cv = new ContentValues();
                cv.put("RepeatType", position);
                getContentResolver().update(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, cv, "_ID=?", new String[]{mProgramID + ""});
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

}

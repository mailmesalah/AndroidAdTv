package com.lychee.sely.adstv.extras;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.scheduledadprogram.ScheduledAdProgramActivity;
import com.lychee.sely.adstv.stackedadprogram.StackedAdProgramActivity;
import com.lychee.sely.adstv.storage.tables.ScheduledAdProgram;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

public class CreateProgramActivity extends AppCompatActivity {

    Button buttonCreateProgram;
    EditText textProgramName;
    String type="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        type=getIntent().getStringExtra("TYPE");

        textProgramName = (EditText) findViewById(R.id.program_name_edit_text);

        buttonCreateProgram=(Button)findViewById(R.id.create_program);
        buttonCreateProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(type.equals("STACKED")){
                    ContentValues cv = new ContentValues();
                    cv.put("ProgramName",textProgramName.getText().toString().trim()!=""?textProgramName.getText().toString().trim():"Program Name");
                    cv.put("Repeat",0);
                    Uri uri = getContentResolver().insert(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, cv);
                    long id = Long.parseLong(uri.getLastPathSegment());

                    Intent i = new Intent(getBaseContext(), StackedAdProgramActivity.class);
                    i.putExtra("PROGRAM_ID",id);
                    startActivity(i);
                    //Close the activity
                    finish();
                }else if(type.equals("SCHEDULED")){
                    ContentValues cv = new ContentValues();
                    cv.put("ProgramName",textProgramName.getText().toString().trim()!=""?textProgramName.getText().toString().trim():"Program Name");
                    cv.put("Repeat",0);
                    cv.put("RepeatType", ScheduledAdProgram.REPEAT_TYPE_NONE);
                    Uri uri = getContentResolver().insert(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, cv);
                    long id = Long.parseLong(uri.getLastPathSegment());

                    Intent i = new Intent(getBaseContext(), ScheduledAdProgramActivity.class);
                    i.putExtra("PROGRAM_ID",id);
                    startActivity(i);
                    //Close the activity
                    finish();
                }

            }
        });
    }
}

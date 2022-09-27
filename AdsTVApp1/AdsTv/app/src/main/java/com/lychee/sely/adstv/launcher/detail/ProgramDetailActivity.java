package com.lychee.sely.adstv.launcher.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.stackedadprogram.StackedAdProgramActivity;

public class ProgramDetailActivity extends Activity {

    private long mProgramID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_detail);
        getWindow().setBackgroundDrawableResource(R.color.window_background);

        mProgramID=getIntent().getLongExtra("_ID",0);

    }


}

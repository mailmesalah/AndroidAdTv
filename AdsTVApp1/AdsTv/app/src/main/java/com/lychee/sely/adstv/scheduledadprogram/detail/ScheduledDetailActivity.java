package com.lychee.sely.adstv.scheduledadprogram.detail;

import android.app.Activity;
import android.os.Bundle;

import com.lychee.sely.adstv.R;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_ad_detail);
        getWindow().setBackgroundDrawableResource(R.color.window_background);

    }


}

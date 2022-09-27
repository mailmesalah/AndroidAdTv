package com.lychee.sely.adstv.stackedadprogram.detail;

import android.app.Activity;
import android.os.Bundle;

import com.lychee.sely.adstv.R;

/**
 * Created by Sely on 14-Jan-17.
 */

public class StackedDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_ad_detail);
        getWindow().setBackgroundDrawableResource(R.color.window_background);

    }


}

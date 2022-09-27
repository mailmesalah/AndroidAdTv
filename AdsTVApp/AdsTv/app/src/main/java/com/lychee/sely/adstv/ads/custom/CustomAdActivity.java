package com.lychee.sely.adstv.ads.custom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.design.CustomAdDesignActivity;

/**
 * Created by Sely on 18-May-17.
 */

public class CustomAdActivity extends FragmentActivity {

    //static values for Activity results
    private static final int RESULT_LOAD_CUSTOM=1;

    Button mbDesignCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ad);

        mbDesignCustom = (Button) findViewById(R.id.design_custom_ad);

        mbDesignCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CustomAdDesignActivity.class);
                startActivityForResult(i, RESULT_LOAD_CUSTOM);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_CUSTOM && resultCode == RESULT_OK && null != data) {

        }
    }
}

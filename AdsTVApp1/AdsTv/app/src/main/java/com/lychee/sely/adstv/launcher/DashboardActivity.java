package com.lychee.sely.adstv.launcher;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.extras.CreateProgramActivity;
import com.lychee.sely.adstv.extras.FileOperation;

import java.util.Date;

public class DashboardActivity extends FragmentActivity {

    //Fragment constants
    private static final int FRAGMENT_DASHBOARD=0;
    private static final int FRAGMENT_STACKED_AD_PROGRAM=1;
    private static final int FRAGMENT_SCHEDULED_AD_PROGRAM=2;
    private static final int FRAGMENT_SETTINGS=3;
    private static final int FRAGMENT_ABOUT_US =4;

    Button btnAddStackedAdProgram;
    private Button btnAddScheduledAdProgram;
    private Button btnStackedAdMenu;
    private Button btnScheduledAdMenu;
    private Button btnSettingsMenu;
    private Button btnAboutUsMenu;
    private TextView tvDashboardHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        getWindow().setBackgroundDrawableResource(R.color.window_background);

        //Blocking for a date
        Date d = new Date();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.set(2017,6,30);
        if(d.after(cal.getTime())){
            Log.d("Expired",""+cal.getTime());
            finish();
        }

        //Required folders are created
        FileOperation.createFoldersIfNotExist();

        btnAddStackedAdProgram = (Button) findViewById(R.id.add_stacked_ad_program);
        btnAddScheduledAdProgram = (Button) findViewById(R.id.add_scheduled_ad_program);
        btnStackedAdMenu = (Button) findViewById(R.id.stacked_ad_menu);
        btnScheduledAdMenu = (Button) findViewById(R.id.scheduled_ad_menu);
        btnSettingsMenu = (Button) findViewById(R.id.settings_menu);
        btnAboutUsMenu = (Button) findViewById(R.id.about_us_menu);
        tvDashboardHeaders = (TextView) findViewById(R.id.text_view_dashboard_fragment_headers);

        nextFragment(FRAGMENT_DASHBOARD);

        btnAddStackedAdProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CreateProgramActivity.class);
                i.putExtra("TYPE", "STACKED");
                startActivity(i);
            }
        });

        btnAddScheduledAdProgram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CreateProgramActivity.class);
                i.putExtra("TYPE", "SCHEDULED");
                startActivity(i);
            }
        });

        btnStackedAdMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment(FRAGMENT_STACKED_AD_PROGRAM);
            }
        });

        btnStackedAdMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextFragment(FRAGMENT_STACKED_AD_PROGRAM);
                }
            }
        });

        btnScheduledAdMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment(FRAGMENT_SCHEDULED_AD_PROGRAM);
            }
        });

        btnScheduledAdMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                  nextFragment(FRAGMENT_SCHEDULED_AD_PROGRAM);
                }
            }
        });

        btnSettingsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment(FRAGMENT_SETTINGS);
            }
        });

        btnSettingsMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextFragment(FRAGMENT_SETTINGS);
                }
            }
        });

        btnAboutUsMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextFragment(FRAGMENT_ABOUT_US);
            }
        });

        btnAboutUsMenu.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    nextFragment(FRAGMENT_ABOUT_US);
                }
            }
        });

    }


    public void nextFragment(int fragmentNo) {
        switch (fragmentNo) {
            case FRAGMENT_DASHBOARD:
            {
                tvDashboardHeaders.setText("Dashboard");
                DashboardFragment fragment = (DashboardFragment)getFragmentManager().findFragmentByTag("DASHBOARD");
                if (fragment == null) {
                    fragment = new DashboardFragment();
                }
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_dashboard, fragment, "DASHBOARD");
                ft.commit();
            }
            break;
            case FRAGMENT_STACKED_AD_PROGRAM:
            {
                tvDashboardHeaders.setText("Stacked Ad Programs");
                StackedAdProgramCardFragment fragment = (StackedAdProgramCardFragment)getFragmentManager().findFragmentByTag("STACKED_AD_PROGRAM");
                if (fragment == null) {
                    fragment = new StackedAdProgramCardFragment();
                }
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_dashboard, fragment, "STACKED_AD_PROGRAM");
                ft.commit();
            }
            break;
            case FRAGMENT_SCHEDULED_AD_PROGRAM:
            {
                tvDashboardHeaders.setText("Scheduled Ad Programs");
                ScheduledAdProgramCardFragment fragment = (ScheduledAdProgramCardFragment)getFragmentManager().findFragmentByTag("SCHEDULED_AD_PROGRAM");
                if (fragment == null) {
                    fragment = new ScheduledAdProgramCardFragment();
                }
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_dashboard, fragment, "SCHEDULED_AD_PROGRAM");
                ft.commit();
            }
            break;
            case FRAGMENT_SETTINGS:
            {
                tvDashboardHeaders.setText("Settings");
                SettingsFragment fragment = (SettingsFragment)getFragmentManager().findFragmentByTag("SETTINGS");
                if (fragment == null) {
                    fragment = new SettingsFragment();
                }
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_dashboard, fragment, "SETTINGS");
                ft.commit();
            }
            break;
            case FRAGMENT_ABOUT_US:
            {
                tvDashboardHeaders.setText("About Us");
                AboutUsFragment fragment = (AboutUsFragment)getFragmentManager().findFragmentByTag("ABOUT_US");
                if (fragment == null) {
                    fragment = new AboutUsFragment();
                }
                android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_dashboard, fragment, "ABOUT_US");
                ft.commit();
            }
            break;

            default:

        }
    }
}
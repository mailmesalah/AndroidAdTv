package com.lychee.sely.adstv.scheduledadprogram.ads;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.CustomAdActivity;
import com.lychee.sely.adstv.ads.custom.CustomAdFragment;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAdProgram;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class ScheduledAdCustomActivity extends FragmentActivity {


    Button mbBrowseCustom;
    ImageCardView micvCustomThumbnail;

    private long mProgramID;
    private long mScheduledAdItemID;
    private long mAdID;
    private Button mbCreateScheduledAdCustom;
    private NumberPicker mnpHours;
    private NumberPicker mnpMinutes;
    private NumberPicker mnpSeconds;
    private NumberPicker mnpDay;
    private NumberPicker mnpYear;
    private NumberPicker mnpMonth;
    private int mRepeatMask= ScheduledAdProgram.REPEAT_TYPE_NONE;
    private LinearLayout mllDateLayout;
    private TextView mtvAdName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_ad_custom);

        mProgramID=getIntent().getLongExtra("PROGRAM_ID",-1);
        mScheduledAdItemID=getIntent().getLongExtra("SCHEDULED_AD_ITEM_ID",-1);
        mAdID=getIntent().getLongExtra("AD_ID",-1);

        mtvAdName = (TextView) findViewById(R.id.text_view_ad_name_scheduled_ad_custom);

        micvCustomThumbnail = (ImageCardView) findViewById(R.id.image_card_view_custom);

        mbBrowseCustom = (Button) findViewById(R.id.browse_scheduled_ad_custom);
        mbCreateScheduledAdCustom = (Button) findViewById(R.id.create_scheduled_ad_custom);

        mnpYear = (NumberPicker) findViewById(R.id.number_picker_year);
        mnpMonth = (NumberPicker) findViewById(R.id.number_picker_month);
        mnpDay = (NumberPicker) findViewById(R.id.number_picker_day);

        mnpHours = (NumberPicker) findViewById(R.id.number_picker_hours);
        mnpMinutes = (NumberPicker) findViewById(R.id.number_picker_minutes);
        mnpSeconds = (NumberPicker) findViewById(R.id.number_picker_seconds);

        mllDateLayout = (LinearLayout) findViewById(R.id.linear_layout_date);

        Cursor cq = getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, null, "_ID=?", new String[]{mProgramID+""}, null);
        if(cq!=null&&cq.getCount()>0){
            cq.moveToFirst();
            if(cq.getInt(cq.getColumnIndex("Repeat"))!=0) {
                mRepeatMask = cq.getInt(cq.getColumnIndex("RepeatType"));
            }
            cq.close();
        }
        
        
        mbBrowseCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), CustomAdActivity.class);
                startActivityForResult(i, CustomAdFragment.AD_SELECTED);
            }
        });

        mbCreateScheduledAdCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(mAdID > -1)) {
                    Toast.makeText(getBaseContext(), "Custom Ad is not selected.", Toast.LENGTH_SHORT).show();
                    mbBrowseCustom.requestFocus();
                    return;
                }

                //If program id is invalid close the window
                if (!(mProgramID > -1)) {
                    Toast.makeText(getBaseContext(), "Wrong Program ID.", Toast.LENGTH_SHORT).show();
                    finish();
                }

                //create scheduled ad with the program id and ad id data

                Cursor cCustomAd = getContentResolver().query(ProgramAdContentProvider.CUSTOM_AD_URI, null, "_ID=?", new String[]{mAdID + ""}, null);
                if (cCustomAd != null && cCustomAd.getCount() > 0) {
                    cCustomAd.moveToFirst();
                    cCustomAd.close();
                    if (mScheduledAdItemID == -1) {
                        //Add to Scheduled Ad
                        ContentValues cv = new ContentValues();
                        cv.put("ProgramCode", mProgramID);
                        cv.put("AdType", ScheduledAd.AD_TYPE_CUSTOM);
                        cv.put("AdCode", mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
                        cv.put("AdPath", "");
                        cv.put("AdDateTime", FileOperation.convertDateTimeToString(mnpYear.getValue(), mnpMonth.getValue() + 1, mnpDay.getValue(), mnpHours.getValue(), mnpMinutes.getValue(), mnpSeconds.getValue()));
                        getContentResolver().insert(ProgramAdContentProvider.SCHEDULED_AD_URI, cv);
                    } else {
                        //Update Scheduled Ad
                        ContentValues cv = new ContentValues();
                        cv.put("AdCode", mAdID);
                        cv.put("AdName", mtvAdName.getText().toString().trim());
                        cv.put("AdDateTime", FileOperation.convertDateTimeToString(mnpYear.getValue(), mnpMonth.getValue() + 1, mnpDay.getValue(), mnpHours.getValue(), mnpMinutes.getValue(), mnpSeconds.getValue()));
                        getContentResolver().update(ProgramAdContentProvider.SCHEDULED_AD_URI, cv, "_ID=?", new String[]{mScheduledAdItemID + ""});
                    }

                    //close the window
                    finish();
                }
            }
        });

        //Setting date
        mnpYear.setMaxValue(2050);
        mnpYear.setMinValue(2000);
        mnpYear.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpYear.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpYear.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });

        mnpYear.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(mnpMonth.getValue()== Calendar.FEBRUARY){
                    GregorianCalendar cal = new GregorianCalendar();
                    if(cal.isLeapYear(mnpYear.getValue())) {
                        mnpDay.setMinValue(1);
                        mnpDay.setMaxValue(29);
                    }else{
                        mnpDay.setMinValue(1);
                        mnpDay.setMaxValue(28);
                    }
                }
            }
        });

        //setting month in Text
        final String[] values = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        mnpMonth.setMinValue(0);
        mnpMonth.setMaxValue(values.length - 1);
        mnpMonth.setDisplayedValues(values);
        mnpMonth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpMonth.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpMonth.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });
        mnpDay.setMinValue(1);
        mnpDay.setMaxValue(31);
        mnpDay.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpDay.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpDay.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });

        mnpMonth.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                switch (newVal) {
                    case Calendar.JANUARY:
                    case Calendar.MARCH:
                    case Calendar.MAY:
                    case Calendar.JULY:
                    case Calendar.AUGUST:
                        mnpDay.setMinValue(1);
                        mnpDay.setMaxValue(31);
                        break;
                    case Calendar.DECEMBER:
                    case Calendar.APRIL:
                    case Calendar.JUNE:
                    case Calendar.SEPTEMBER:
                    case Calendar.NOVEMBER:
                        mnpDay.setMinValue(1);
                        mnpDay.setMaxValue(30);
                        break;
                    case Calendar.FEBRUARY:
                        mnpDay.setMinValue(1);
                        GregorianCalendar cal = new GregorianCalendar();
                        mnpDay.setMaxValue(cal.isLeapYear(mnpYear.getValue()) ? 29 : 28);
                        break;
                    default:
                        mnpDay.setMinValue(1);
                        mnpDay.setMaxValue(31);
                }
            }
        });

        //Setting time
        mnpHours.setMaxValue(23);
        mnpHours.setMinValue(0);
        mnpHours.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpHours.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpHours.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });
        mnpMinutes.setMaxValue(59);
        mnpMinutes.setMinValue(0);
        mnpMinutes.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpMinutes.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpMinutes.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });
        mnpSeconds.setMaxValue(59);
        mnpSeconds.setMinValue(0);
        mnpSeconds.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    mnpSeconds.setBackgroundResource(R.drawable.lb_background);
                } else {
                    mnpSeconds.setBackgroundResource(R.drawable.lb_action_bg);
                }
            }
        });

        //If Open from details instead of creation Populate the datas
        if (mScheduledAdItemID > -1) {
            Cursor c = getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_URI, null, "_ID=?", new String[]{mScheduledAdItemID + ""}, null);
            if (c != null && c.getCount() > 0) {
                c.moveToFirst();

                mtvAdName.setText(c.getString(c.getColumnIndex("AdName")));
                String sDateTime = c.getString(c.getColumnIndex("AdDateTime"));
                c.close();

                //Drawable dCustom = FileOperation.imgDrawableFromFile(Resources.getSystem(), customPath);
                //micvCustomThumbnail.setMainImageAdjustViewBounds(true);
                //micvCustomThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                //micvCustomThumbnail.setMainImage(dCustom);

                Date dateTime = FileOperation.convertStringToDateTime(sDateTime);
                Calendar cal = Calendar.getInstance();
                cal.setTime(dateTime);

                mnpYear.setValue(cal.get(Calendar.YEAR));
                mnpMonth.setValue(cal.get(Calendar.MONTH));
                mnpDay.setValue(cal.get(Calendar.DAY_OF_MONTH));
                mnpHours.setValue(cal.get(Calendar.HOUR_OF_DAY));
                mnpMinutes.setValue(cal.get(Calendar.MINUTE));
                mnpSeconds.setValue(cal.get(Calendar.SECOND));
            }
        } else {
            Calendar cal = Calendar.getInstance();
            mnpYear.setValue(cal.get(Calendar.YEAR));
            mnpMonth.setValue(cal.get(Calendar.MONTH));
            mnpDay.setValue(cal.get(Calendar.DAY_OF_MONTH));
            mnpHours.setValue(cal.get(Calendar.HOUR_OF_DAY));
            mnpMinutes.setValue(cal.get(Calendar.MINUTE));
            mnpSeconds.setValue(cal.get(Calendar.SECOND));
        }

        //Mask Fields depending on the selected mask and setting navigation
        setNavigation();
    }

    private void setNavigation(){
        //Default
        mbBrowseCustom.setNextFocusRightId(R.id.number_picker_year);
        mbBrowseCustom.setNextFocusDownId(R.id.create_scheduled_ad_custom);
        mnpYear.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
        mnpYear.setNextFocusRightId(R.id.number_picker_month);
        mnpMonth.setNextFocusLeftId(R.id.number_picker_year);
        mnpMonth.setNextFocusRightId(R.id.number_picker_day);
        mnpDay.setNextFocusLeftId(R.id.number_picker_month);
        mnpDay.setNextFocusRightId(R.id.number_picker_hours);
        mnpHours.setNextFocusLeftId(R.id.number_picker_day);
        mnpHours.setNextFocusRightId(R.id.number_picker_minutes);
        mnpMinutes.setNextFocusLeftId(R.id.number_picker_hours);
        mnpMinutes.setNextFocusRightId(R.id.number_picker_seconds);
        mnpSeconds.setNextFocusLeftId(R.id.number_picker_minutes);
        mnpSeconds.setNextFocusRightId(R.id.create_scheduled_ad_custom);
        mbCreateScheduledAdCustom.setNextFocusUpId(R.id.number_picker_seconds);
        mbCreateScheduledAdCustom.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);

        if(mRepeatMask==ScheduledAdProgram.REPEAT_TYPE_YEARLY){
            mnpYear.setVisibility(View.GONE);

            mbBrowseCustom.setNextFocusRightId(R.id.number_picker_month);
            mbBrowseCustom.setNextFocusDownId(R.id.create_scheduled_ad_custom);
            mnpMonth.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
            mnpMonth.setNextFocusRightId(R.id.number_picker_day);
            mnpDay.setNextFocusLeftId(R.id.number_picker_month);
            mnpDay.setNextFocusRightId(R.id.number_picker_hours);
            mnpHours.setNextFocusLeftId(R.id.number_picker_day);
            mnpHours.setNextFocusRightId(R.id.number_picker_minutes);
            mnpMinutes.setNextFocusLeftId(R.id.number_picker_hours);
            mnpMinutes.setNextFocusRightId(R.id.number_picker_seconds);
            mnpSeconds.setNextFocusLeftId(R.id.number_picker_minutes);
            mnpSeconds.setNextFocusRightId(R.id.create_scheduled_ad_custom);
            mbCreateScheduledAdCustom.setNextFocusUpId(R.id.number_picker_seconds);
            mbCreateScheduledAdCustom.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);

        }else if(mRepeatMask==ScheduledAdProgram.REPEAT_TYPE_MONTHLY){
            mnpYear.setVisibility(View.GONE);
            mnpMonth.setVisibility(View.GONE);

            mbBrowseCustom.setNextFocusRightId(R.id.number_picker_day);
            mbBrowseCustom.setNextFocusDownId(R.id.create_scheduled_ad_custom);
            mnpDay.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
            mnpDay.setNextFocusRightId(R.id.number_picker_hours);
            mnpHours.setNextFocusLeftId(R.id.number_picker_day);
            mnpHours.setNextFocusRightId(R.id.number_picker_minutes);
            mnpMinutes.setNextFocusLeftId(R.id.number_picker_hours);
            mnpMinutes.setNextFocusRightId(R.id.number_picker_seconds);
            mnpSeconds.setNextFocusLeftId(R.id.number_picker_minutes);
            mnpSeconds.setNextFocusRightId(R.id.create_scheduled_ad_custom);
            mbCreateScheduledAdCustom.setNextFocusUpId(R.id.number_picker_seconds);
            mbCreateScheduledAdCustom.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
        }else if(mRepeatMask==ScheduledAdProgram.REPEAT_TYPE_DAILY){
            mllDateLayout.setVisibility(View.GONE);

            mbBrowseCustom.setNextFocusRightId(R.id.number_picker_hours);
            mbBrowseCustom.setNextFocusDownId(R.id.create_scheduled_ad_custom);
            mnpHours.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
            mnpHours.setNextFocusRightId(R.id.number_picker_minutes);
            mnpMinutes.setNextFocusLeftId(R.id.number_picker_hours);
            mnpMinutes.setNextFocusRightId(R.id.number_picker_seconds);
            mnpSeconds.setNextFocusLeftId(R.id.number_picker_minutes);
            mnpSeconds.setNextFocusRightId(R.id.create_scheduled_ad_custom);
            mbCreateScheduledAdCustom.setNextFocusUpId(R.id.number_picker_seconds);
            mbCreateScheduledAdCustom.setNextFocusLeftId(R.id.browse_scheduled_ad_custom);
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

                //Drawable dCustom = FileOperation.getThumbnailFromCustomFile(Resources.getSystem(), customPath);
                //micvCustomThumbnail.setMainImageScaleType(ImageView.ScaleType.CENTER_INSIDE);
                //micvCustomThumbnail.setMainImage(dCustom);

            }

        }
    }
}

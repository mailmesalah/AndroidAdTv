package com.lychee.sely.adstv.scheduledadprogram.detail;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.scheduledadprogram.ScheduledAdItem;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        ScheduledAdItem sai = (ScheduledAdItem)item;

        viewHolder.getTitle().setText(sai.getAdName()+"");
        viewHolder.getSubtitle().setText(sai.getAdType()== ScheduledAd.AD_TYPE_IMAGE?"Image":sai.getAdType()==ScheduledAd.AD_TYPE_VIDEO?"Video":"Custom");
        //viewHolder.getBody().setText("Time : "+ FileOperation.getHoursFromTimePeriod(sai.getAdTimePeriod())+":"+FileOperation.getMinutesFromTimePeriod(sai.getAdTimePeriod())+":"+FileOperation.getSecondsFromTimePeriod(sai.getAdTimePeriod()));
    }
}

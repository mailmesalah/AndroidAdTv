package com.lychee.sely.adstv.stackedadprogram.detail;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.stackedadprogram.StackedAdItem;
import com.lychee.sely.adstv.storage.tables.StackedAd;

/**
 * Created by Sely on 14-Jan-17.
 */

public class StackedDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        StackedAdItem sai = (StackedAdItem)item;

        viewHolder.getTitle().setText(sai.getSerialNo()+". "+sai.getAdName());
        viewHolder.getSubtitle().setText(sai.getAdType()== StackedAd.AD_TYPE_IMAGE?"Image":sai.getAdType()==StackedAd.AD_TYPE_VIDEO?"Video":"Custom");
        viewHolder.getBody().setText("Time : "+ FileOperation.getHoursFromTimePeriod(sai.getAdTimePeriod())+":"+FileOperation.getMinutesFromTimePeriod(sai.getAdTimePeriod())+":"+FileOperation.getSecondsFromTimePeriod(sai.getAdTimePeriod()));
    }
}

package com.lychee.sely.adstv.ads.custom.design.details;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.ads.Ad;
import com.lychee.sely.adstv.ads.custom.CustomAdItem;

/**
 * Created by Sely on 05-Jun-17.
 */

public class CustomAdDesignDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        CustomAdItem ad = (CustomAdItem)item;

        viewHolder.getTitle().setText(ad.getSerialNo()+". "+ ad.getAdName()+"");

    }
}

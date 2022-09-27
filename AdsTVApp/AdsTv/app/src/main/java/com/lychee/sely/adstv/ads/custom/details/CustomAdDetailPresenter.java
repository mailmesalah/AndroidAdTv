package com.lychee.sely.adstv.ads.custom.details;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.ads.Ad;

/**
 * Created by Sely on 18-May-17.
 */

public class CustomAdDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Ad ad = (Ad)item;

        viewHolder.getTitle().setText(ad.getAdName()+"");

    }
}

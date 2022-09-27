package com.lychee.sely.adstv.ads.image.details;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.ads.Ad;

/**
 * Created by Sely on 13-Jan-17.
 */

public class ImageAdDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Ad ad = (Ad)item;

        viewHolder.getTitle().setText(ad.getAdName()+"");
        viewHolder.getSubtitle().setText(ad.getAdPath());

    }
}

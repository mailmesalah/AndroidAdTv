package com.lychee.sely.adstv.ads.video.details;

import android.support.v17.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.lychee.sely.adstv.ads.Ad;

/**
 * Created by Sely on 13-Jan-17.
 */

public class VideoAdDetailPresenter extends AbstractDetailsDescriptionPresenter {
    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Ad ad = (Ad)item;

        viewHolder.getTitle().setText(ad.getId()+"");
        viewHolder.getSubtitle().setText(ad.getAdPath());

    }
}

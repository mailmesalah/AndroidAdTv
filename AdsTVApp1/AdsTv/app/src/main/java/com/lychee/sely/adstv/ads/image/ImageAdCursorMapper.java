package com.lychee.sely.adstv.ads.image;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

import com.lychee.sely.adstv.ads.Ad;

/**
 * Created by Sely on 06-Jan-17.
 */

public class ImageAdCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected Ad bind(Cursor cursor) {
        Ad ad = new Ad();
        ad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        ad.setAdType(Ad.IMAGE_AD);
        ad.setAdName(cursor.getString(cursor.getColumnIndex("AdName")));
        ad.setAdPath(cursor.getString(cursor.getColumnIndex("AdPath")));

        return ad;
    }
}

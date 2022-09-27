package com.lychee.sely.adstv.ads.custom;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

import com.lychee.sely.adstv.ads.Ad;

/**
 * Created by Sely on 18-May-17.
 */

public class CustomAdCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected Ad bind(Cursor cursor) {
        Ad ad = new Ad();
        ad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        ad.setAdType(Ad.CUSTOM_AD);
        ad.setAdName(cursor.getString(cursor.getColumnIndex("AdName")));

        return ad;
    }
}

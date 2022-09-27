package com.lychee.sely.adstv.ads.custom.design;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

import com.lychee.sely.adstv.ads.Ad;
import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.storage.tables.CustomDetailAd;

/**
 * Created by Sely on 03-Jun-17.
 */

public class CustomAdDesignCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected CustomAdItem bind(Cursor cursor) {
        CustomAdItem ad = new CustomAdItem();
        ad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        ad.setSerialNo(cursor.getInt(cursor.getColumnIndex("ZOrder")));
        ad.setAdType(cursor.getInt(cursor.getColumnIndex("AdType"))== CustomDetailAd.AD_TYPE_IMAGE?CustomAdItem.IMAGE_AD:CustomAdItem.VIDEO_AD);
        ad.setAdPath(cursor.getString(cursor.getColumnIndex("AdPath")));
        ad.setAdName(cursor.getString(cursor.getColumnIndex("AdName")));

        return ad;
    }
}

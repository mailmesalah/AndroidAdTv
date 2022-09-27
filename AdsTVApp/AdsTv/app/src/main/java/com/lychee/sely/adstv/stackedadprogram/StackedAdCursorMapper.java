package com.lychee.sely.adstv.stackedadprogram;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

/**
 * Created by Sely on 06-Jan-17.
 */

public class StackedAdCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected StackedAdItem bind(Cursor cursor) {
        StackedAdItem ad = new StackedAdItem();
        ad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        ad.setSerialNo(cursor.getInt(cursor.getColumnIndex("SerialNo")));
        ad.setProgramCode(cursor.getLong(cursor.getColumnIndex("ProgramCode")));
        ad.setAdType(cursor.getInt(cursor.getColumnIndex("AdType")));
        ad.setAdCode(cursor.getLong(cursor.getColumnIndex("AdCode")));
        ad.setAdName(cursor.getString(cursor.getColumnIndex("AdName")));
        ad.setAdPath(cursor.getString(cursor.getColumnIndex("AdPath")));
        ad.setAdTimePeriod(cursor.getInt(cursor.getColumnIndex("AdTimePeriod")));

        return ad;
    }
}

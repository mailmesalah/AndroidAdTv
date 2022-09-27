package com.lychee.sely.adstv.scheduledadprogram;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected ScheduledAdItem bind(Cursor cursor) {
        ScheduledAdItem ad = new ScheduledAdItem();
        ad.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        ad.setProgramCode(cursor.getLong(cursor.getColumnIndex("ProgramCode")));
        ad.setAdType(cursor.getInt(cursor.getColumnIndex("AdType")));
        ad.setAdCode(cursor.getLong(cursor.getColumnIndex("AdCode")));
        ad.setAdName(cursor.getString(cursor.getColumnIndex("AdName")));
        ad.setAdPath(cursor.getString(cursor.getColumnIndex("AdPath")));
        ad.setAdDateTime( cursor.getString(cursor.getColumnIndex("AdDateTime")));

        return ad;
    }
}

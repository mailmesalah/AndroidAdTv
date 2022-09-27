package com.lychee.sely.adstv.launcher;

import android.database.Cursor;
import android.support.v17.leanback.database.CursorMapper;
import android.util.Log;

/**
 * Created by Sely on 06-Jan-17.
 */

public class StackedAdProgramCardCursorMapper extends CursorMapper {
    @Override
    protected void bindColumns(Cursor cursor) {
    }

    @Override
    protected Program bind(Cursor cursor) {
        Program p = new Program();
        p.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
        p.setTitle(cursor.getString(cursor.getColumnIndex("ProgramName")));
        p.setType(Program.STACKED);
        p.setDescription("Stacked Ad Program");
        p.setRepeat(cursor.getInt(cursor.getColumnIndex("Repeat"))==0?false:true);

        return p;
    }
}

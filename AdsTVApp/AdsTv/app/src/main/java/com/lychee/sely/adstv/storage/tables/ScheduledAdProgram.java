package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 25-Jan-17.
 */
public class ScheduledAdProgram {

    public static final int REPEAT_TYPE_YEARLY=0;
    public static final int REPEAT_TYPE_MONTHLY=1;
    public static final int REPEAT_TYPE_DAILY=2;
    public static final int REPEAT_TYPE_NONE=3;

    private long _id;
    private String programName;
    private int repeat;
    private int repeatType;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public int getRepeatType() {
        return repeatType;
    }

    public void setRepeatType(int repeatType) {
        this.repeatType = repeatType;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table ScheduledAdProgram(_ID Integer Primary Key autoincrement, ProgramName Text Not Null, Repeat Integer, RepeatType Integer);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists ScheduledAdProgram;");
        onCreate(database);
    }

}

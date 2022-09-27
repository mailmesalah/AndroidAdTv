package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 06-Jan-17.
 */
public class StackedAdProgram {

    private long _id;
    private String programName;
    private int repeat;

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

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table StackedAdProgram(_ID Integer Primary Key autoincrement, ProgramName Text Not Null,Repeat Integer);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists StackedAdProgram;");
        onCreate(database);
    }

}

package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 18-May-17.
 */
public class CustomAd {

    private long _id;
    private String adName;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public String getAdName() {
        return adName;
    }

    public void setAdName(String adName) {
        this.adName = adName;
    }


    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table CustomAd(_ID Integer Primary Key autoincrement, AdName String Not Null);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists CustomAd;");
        onCreate(database);
    }

}

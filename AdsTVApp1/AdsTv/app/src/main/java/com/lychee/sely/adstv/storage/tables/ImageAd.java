package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 12-Jan-17.
 */
public class ImageAd {

    private long _id;
    private String adName;
    private String adPath;

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

    public String getAdPath() {
        return adPath;
    }

    public void setAdPath(String adPath) {
        this.adPath = adPath;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table ImageAd(_ID Integer Primary Key autoincrement, AdName String Not Null, AdPath String Not Null);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists ImageAd;");
        onCreate(database);
    }

}

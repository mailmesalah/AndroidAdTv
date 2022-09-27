package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 25-Jan-17.
 */
public class ScheduledAd {

    public final static int AD_TYPE_IMAGE=0;
    public final static int AD_TYPE_VIDEO=1;
    public final static int AD_TYPE_CUSTOM=2;

    private long _id;
    private int programCode;
    private int adType;//image or video
    private long adCode;
    private String adName;
    private String adPath;
    private String adDateTime;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getProgramCode() {
        return programCode;
    }

    public void setProgramCode(int programCode) {
        this.programCode = programCode;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public long getAdCode() {
        return adCode;
    }

    public void setAdCode(long adCode) {
        this.adCode = adCode;
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

    public String getAdDateTime() {
        return adDateTime;
    }

    public void setAdDateTime(String adDateTime) {
        this.adDateTime = adDateTime;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table ScheduledAd(_ID Integer Primary Key autoincrement, ProgramCode Integer Not Null, AdType Integer Not Null, AdName String Not Null, AdPath String Not Null, AdCode Integer Not Null, AdDateTime String Not Null);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists ScheduledAd;");
        onCreate(database);
    }

}

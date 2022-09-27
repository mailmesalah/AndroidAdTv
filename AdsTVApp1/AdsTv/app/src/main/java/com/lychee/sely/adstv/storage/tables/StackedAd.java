package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 06-Jan-17.
 */
public class StackedAd {

    public final static int AD_TYPE_IMAGE=0;
    public final static int AD_TYPE_VIDEO=1;
    public final static int AD_TYPE_CUSTOM=2;

    private long _id;
    private int serialNo;
    private int programCode;
    private int adType;
    private long adCode;
    private String adName;
    private String adPath;
    private int adTimePeriod;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
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

    public int getAdTimePeriod() {
        return adTimePeriod;
    }

    public void setAdTimePeriod(int adTimePeriod) {
        this.adTimePeriod = adTimePeriod;
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL("Create Table StackedAd(_ID Integer Primary Key autoincrement, SerialNo Integer Not Null, ProgramCode Integer Not Null, AdType Integer Not Null, AdName String Not Null, AdPath String Not Null, AdCode Integer Not Null, AdTimePeriod Integer Not Null);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists StackedAd;");
        onCreate(database);
    }

}

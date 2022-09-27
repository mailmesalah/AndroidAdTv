package com.lychee.sely.adstv.storage.tables;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Sely on 18-May-17.
 */
public class CustomDetailAd {

    public final static int AD_TYPE_IMAGE=0;
    public final static int AD_TYPE_VIDEO=1;

    private long _id;
    private long adId;
    private double x;
    private double y;
    private double width;
    private double height;
    private int zOrder;
    private int adType;//0 = image 1= video
    private String adName;
    private String adPath;

    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getzOrder() {
        return zOrder;
    }

    public void setzOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
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
        database.execSQL("Create Table CustomDetailAd(_ID Integer Primary Key autoincrement, AdId Integer , X Integer, Y Integer, Width Integer, Height Integer, ZOrder Integer, AdType Integer, AdName String Not Null, AdPath String Not Null);");
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        // Drop older table if existed
        database.execSQL("Drop Table If Exists CustomDetailAd;");
        onCreate(database);
    }

}

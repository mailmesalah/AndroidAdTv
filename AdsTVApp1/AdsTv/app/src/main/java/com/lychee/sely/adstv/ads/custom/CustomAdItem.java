package com.lychee.sely.adstv.ads.custom;

/**
 * Created by Sely on 03-Jun-17.
 */

public class CustomAdItem {
    public static final int IMAGE_AD = 809;
    public static final int VIDEO_AD = 810;

    private long id;
    private int serialNo;
    private int adType;
    private String adName;
    private String adPath;

    public CustomAdItem() {
    }

    public CustomAdItem(long id, int serialNo, int adType, String adName, String adPath) {
        this.id = id;
        this.serialNo = serialNo;
        this.adType = adType;
        this.adName = adName;
        this.adPath = adPath;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(int serialNo) {
        this.serialNo = serialNo;
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
}

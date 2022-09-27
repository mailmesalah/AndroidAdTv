package com.lychee.sely.adstv.ads;

/**
 * Created by Sely on 12-Jan-17.
 */

public class Ad {
    public static final int IMAGE_AD = 809;
    public static final int VIDEO_AD = 810;
    public static final int CUSTOM_AD = 811;

    private long id;
    private int adType;
    private String adName;
    private String adPath;

    public Ad() {
    }

    public Ad(long id, int adType, String adName, String adPath) {
        this.id = id;
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

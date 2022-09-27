package com.lychee.sely.adstv.stackedadprogram;

/**
 * Created by Sely on 12-Jan-17.
 */

public class StackedAdItem {
    private long id;
    private int serialNo;
    private long programCode;
    private int adType;
    private long adCode;
    private String adName;
    private String adPath;
    private int adTimePeriod;

    public StackedAdItem() {
    }

    public StackedAdItem(long id, int serialNo, long programCode, int adType, long adCode, String adName, String adPath, int adTimePeriod) {
        this.id = id;
        this.serialNo = serialNo;
        this.programCode = programCode;
        this.adType = adType;
        this.adCode = adCode;
        this.adName = adName;
        this.adPath = adPath;
        this.adTimePeriod = adTimePeriod;
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

    public long getProgramCode() {
        return programCode;
    }

    public void setProgramCode(long programCode) {
        this.programCode = programCode;
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

    public long getAdCode() {
        return adCode;
    }

    public void setAdCode(long adCode) {
        this.adCode = adCode;
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
}

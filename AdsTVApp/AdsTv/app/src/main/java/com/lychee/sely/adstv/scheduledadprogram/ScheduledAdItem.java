package com.lychee.sely.adstv.scheduledadprogram;

import java.util.Date;

/**
 * Created by Sely on 25-Jan-17.
 */

public class ScheduledAdItem {
    private long id;
    private long programCode;
    private int adType;
    private long adCode;
    private String adName;
    private String adPath;
    private String adDateTime;

    public ScheduledAdItem() {
    }

    public ScheduledAdItem(long id, long programCode, int adType, long adCode, String adName, String adPath, String adDateTime) {
        this.id = id;
        this.programCode = programCode;
        this.adType = adType;
        this.adCode = adCode;
        this.adName = adName;
        this.adPath = adPath;
        this.adDateTime = adDateTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
}

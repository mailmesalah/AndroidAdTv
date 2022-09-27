package com.lychee.sely.adstv.launcher;

/**
 * Created by Sely on 26-Dec-16.
 */

public class Program {
    public final static int STACKED=0;
    public final static int SCHEDULED=1;

    private long id;
    private String title;
    private int type;
    private boolean repeat;
    private String description;

    public Program() {
    }

    public Program(long id, String title, int type, boolean repeat, String description) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.repeat = repeat;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

package com.lychee.sely.adstv.extras;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Sely on 12-Jan-17.
 */

public class FileOperation {

    public static String AD_IMAGE_PATH = Environment.getExternalStorageDirectory().getPath() + "/AdImages/";
    public static String AD_VIDEO_PATH = Environment.getExternalStorageDirectory().getPath() + "/AdVideos/";

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }

    public static Drawable imgDrawableFromFile(Resources res, String file_name) {

        File imgFile = new File(file_name);
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            if (myBitmap != null) {
                return new BitmapDrawable(res, myBitmap);
            } else {
                return null;
            }
        }
        return null;

    }

    public static Drawable getThumbnailFromVideoFile(Resources res, String file_name) {

        Bitmap myBitmap = ThumbnailUtils.createVideoThumbnail(file_name, 0);

        if (myBitmap != null) {
            return new BitmapDrawable(res, myBitmap);
        } else {
            return null;
        }
    }

    public static Bitmap getBitmapFromImageFile(Resources res, String file_name) {

        File imgFile = new File(file_name);
        if (imgFile.exists()) {

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            return myBitmap;
        }

        return null;

    }

    public static Bitmap getBitmapFromVideoFile(Resources res, String file_name) {

        return ThumbnailUtils.createVideoThumbnail(file_name, 0);

    }

    public static void createFoldersIfNotExist() {
        if (!new File(AD_IMAGE_PATH).exists()) {
            new File(AD_IMAGE_PATH).mkdir();
        }

        if (!new File(AD_VIDEO_PATH).exists()) {
            new File(AD_VIDEO_PATH).mkdir();
        }
    }

    public static int getTotalTimeInSec(int hours, int minutes, int seconds) {
        return hours * 360 + minutes * 60 + seconds;
    }

    public static int getHoursFromTimePeriod(int seconds) {
        return seconds / 360;
    }

    public static int getMinutesFromTimePeriod(int seconds) {
        return (seconds - (getHoursFromTimePeriod(seconds) * 360)) / 60;
    }

    public static int getSecondsFromTimePeriod(int seconds) {
        return seconds - ((getHoursFromTimePeriod(seconds) * 360) + (getMinutesFromTimePeriod(seconds) * 60));
    }

    public static String convertDateTimeToString(int year, int month, int day, int hours, int minutes, int seconds){
        return year+"-"+month+"-"+day+" "+hours+":"+minutes+":"+seconds;

    }

    public static Date convertStringToDateTime(String sDateTime){
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=null;
        try {
            d=outputFormat.parse(sDateTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return d;
    }
}

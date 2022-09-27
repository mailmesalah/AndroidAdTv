package com.lychee.sely.adstv.storage.tables.contentprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.lychee.sely.adstv.storage.DatabaseHandler;


/**
 * Created by Sely on 05-Sep-16.
 */
public class ProgramAdContentProvider extends ContentProvider {

    private DatabaseHandler database;
    private static final String AUTHORITY = "com.lychee.sely.adstv.contentprovider";

    public static final Uri STACKED_AD_PROGRAM_URI = Uri.parse("content://" + AUTHORITY + "/stacked_ad_program");
    public static final Uri STACKED_AD_URI = Uri.parse("content://" + AUTHORITY + "/stacked_ad");
    public static final Uri IMAGE_AD_URI = Uri.parse("content://" + AUTHORITY + "/image_ad");
    public static final Uri VIDEO_AD_URI = Uri.parse("content://" + AUTHORITY + "/video_ad");
    public static final Uri SCHEDULED_AD_PROGRAM_URI = Uri.parse("content://" + AUTHORITY + "/scheduled_ad_program");
    public static final Uri SCHEDULED_AD_URI = Uri.parse("content://" + AUTHORITY + "/scheduled_ad");
    public static final Uri CUSTOM_AD_URI = Uri.parse("content://" + AUTHORITY + "/custom_ad");
    public static final Uri CUSTOM_DETAIL_AD_URI = Uri.parse("content://" + AUTHORITY + "/custom_detail_ad");

    private static final int STACKED_AD_PROGRAM = 0;
    private static final int STACKED_AD = 1;
    private static final int IMAGE_AD = 2;
    private static final int VIDEO_AD = 3;
    private static final int SCHEDULED_AD_PROGRAM = 4;
    private static final int SCHEDULED_AD = 5;
    private static final int CUSTOM_AD = 6;
    private static final int CUSTOM_DETAIL_AD = 7;


    private static final UriMatcher uriMatcher = getUriMatcher();
    private static UriMatcher getUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "stacked_ad_program", STACKED_AD_PROGRAM);
        uriMatcher.addURI(AUTHORITY, "stacked_ad", STACKED_AD);
        uriMatcher.addURI(AUTHORITY, "image_ad", IMAGE_AD);
        uriMatcher.addURI(AUTHORITY, "video_ad", VIDEO_AD);
        uriMatcher.addURI(AUTHORITY, "scheduled_ad_program", SCHEDULED_AD_PROGRAM);
        uriMatcher.addURI(AUTHORITY, "scheduled_ad", SCHEDULED_AD);
        uriMatcher.addURI(AUTHORITY, "custom_ad", CUSTOM_AD);
        uriMatcher.addURI(AUTHORITY, "custom_detail_ad", CUSTOM_DETAIL_AD);
        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        database=DatabaseHandler.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Log.d("Contentmatch","matched to "+uriMatcher.match(uri));
        switch (uriMatcher.match(uri)){
            case STACKED_AD_PROGRAM: {
                Cursor c = database.getStackedAdPrograms(selection, selectionArgs);
                c.setNotificationUri(getContext().getContentResolver(), STACKED_AD_PROGRAM_URI);
                return c;
            }
            case STACKED_AD: {
                Cursor c = database.getStackedAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), STACKED_AD_URI);
                return c;
            }
            case IMAGE_AD: {
                Cursor c = database.getImageAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), IMAGE_AD_URI);
                return c;
            }
            case VIDEO_AD: {
                Cursor c = database.getVideoAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), VIDEO_AD_URI);
                return c;
            }
            case SCHEDULED_AD_PROGRAM: {
                Log.d("ContentProvi","Scheduler");
                Cursor c = database.getScheduledAdPrograms(selection, selectionArgs);
                c.setNotificationUri(getContext().getContentResolver(), SCHEDULED_AD_PROGRAM_URI);
                return c;
            }
            case SCHEDULED_AD: {
                Cursor c = database.getScheduledAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), SCHEDULED_AD_URI);
                return c;
            }

            case CUSTOM_AD: {
                Cursor c = database.getCustomAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), CUSTOM_AD_URI);
                return c;
            }

            case CUSTOM_DETAIL_AD: {
                Cursor c = database.getCustomDetailAds(selection, selectionArgs, sortOrder);
                c.setNotificationUri(getContext().getContentResolver(), CUSTOM_DETAIL_AD_URI);
                return c;
            }

            default:
        }

        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long id=0;
        Uri _uri;
        switch (uriMatcher.match(uri)){
            case STACKED_AD_PROGRAM:
                id=database.addStackedAdProgram(values);
                _uri = ContentUris.withAppendedId(STACKED_AD_PROGRAM_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case STACKED_AD:
                id=database.addStackedAd(values);
                _uri = ContentUris.withAppendedId(STACKED_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case IMAGE_AD:
                id=database.addImageAd(values);
                _uri = ContentUris.withAppendedId(IMAGE_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case VIDEO_AD:
                id=database.addVideoAd(values);
                _uri = ContentUris.withAppendedId(VIDEO_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case SCHEDULED_AD_PROGRAM:
                id=database.addScheduledAdProgram(values);
                _uri = ContentUris.withAppendedId(SCHEDULED_AD_PROGRAM_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case SCHEDULED_AD:
                id=database.addScheduledAd(values);
                _uri = ContentUris.withAppendedId(SCHEDULED_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case CUSTOM_AD:
                id=database.addCustomAd(values);
                _uri = ContentUris.withAppendedId(CUSTOM_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            case CUSTOM_DETAIL_AD:
                id=database.addCustomDetailAd(values);
                _uri = ContentUris.withAppendedId(CUSTOM_DETAIL_AD_URI, id);
                getContext().getContentResolver().notifyChange(_uri,null);
                return _uri;

            default:
        }

        return null;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        switch (uriMatcher.match(uri)){
            case STACKED_AD_PROGRAM: {
            }
            case STACKED_AD: {
            }
            default:
        }

        return 0;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case STACKED_AD_PROGRAM: {
                int r = database.deleteStackedAdProgram(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(STACKED_AD_PROGRAM_URI, null);
                return r;
            }
            case STACKED_AD: {
                int r = database.deleteStackedAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(STACKED_AD_URI, null);
                return r;
            }
            case IMAGE_AD: {
                int r = database.deleteImageAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(IMAGE_AD_URI, null);
                return r;
            }
            case VIDEO_AD: {
                int r = database.deleteVideoAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(VIDEO_AD_URI, null);
                return r;
            }
            case SCHEDULED_AD_PROGRAM: {
                int r = database.deleteScheduledAdProgram(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(SCHEDULED_AD_PROGRAM_URI, null);
                return r;
            }
            case SCHEDULED_AD: {
                int r = database.deleteScheduledAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(SCHEDULED_AD_URI, null);
                return r;
            }
            case CUSTOM_AD: {
                int r = database.deleteCustomAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(CUSTOM_AD_URI, null);
                return r;
            }
            case CUSTOM_DETAIL_AD: {
                int r = database.deleteCustomDetailAd(selection, selectionArgs);
                getContext().getContentResolver().notifyChange(CUSTOM_DETAIL_AD_URI, null);
                return r;
            }

            default:
        }

        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)){
            case STACKED_AD_PROGRAM: {
                int r = database.updateStackedAdProgram(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(STACKED_AD_PROGRAM_URI, null);
                return r;
            }
            case STACKED_AD: {
                int r = database.updateStackedAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(STACKED_AD_URI, null);
                return r;
            }
            case IMAGE_AD: {
                int r = database.updateImageAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(IMAGE_AD_URI, null);
                return r;
            }
            case VIDEO_AD: {
                int r = database.updateVideoAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(VIDEO_AD_URI, null);
                return r;
            }
            case SCHEDULED_AD_PROGRAM: {
                int r = database.updateScheduledAdProgram(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(SCHEDULED_AD_PROGRAM_URI, null);
                return r;
            }
            case SCHEDULED_AD: {
                int r = database.updateScheduledAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(SCHEDULED_AD_URI, null);
                return r;
            }
            case CUSTOM_AD: {
                int r = database.updateCustomAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(CUSTOM_AD_URI, null);
                return r;
            }
            case CUSTOM_DETAIL_AD: {
                int r = database.updateCustomDetailAd(values, selection, selectionArgs);
                getContext().getContentResolver().notifyChange(CUSTOM_DETAIL_AD_URI, null);
                return r;
            }

            default:
        }

        return 0;
    }
}

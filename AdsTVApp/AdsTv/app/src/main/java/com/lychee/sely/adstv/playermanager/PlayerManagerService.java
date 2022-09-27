package com.lychee.sely.adstv.playermanager;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;
import android.widget.Toast;

import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.GeneralSettings;
import com.lychee.sely.adstv.scheduledadprogram.ScheduledAdItem;
import com.lychee.sely.adstv.stackedadprogram.StackedAdItem;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAdProgram;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PlayerManagerService extends Service {
    boolean destroyed=false;
    //Incoming message constants
    public static final int RUN_PROGRAM = 0;

    Messenger mMessenger = new Messenger(new IncomingMessageHandler());

    // Incoming messages Handler
    class IncomingMessageHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {

            Log.d("Service Message", "" + msg.what);
            switch (msg.what) {
                case RUN_PROGRAM: {
                    Bundle bundle = msg.getData();
                    long programID = bundle.getLong("PROGRAM_ID");
                    int programType = bundle.getInt("PROGRAM_TYPE");
                    runProgram(programID, programType);
                }
                break;

                default:
                    super.handleMessage(msg);
            }

        }
    }

    public PlayerManagerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMessenger.getBinder();
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        destroyed=true;
        return true;
    }

    @Override
    public void onDestroy() {
        destroyed=true;
        super.onDestroy();
    }

    private void createNSendBroadcast(Intent broadcastIntent) {
        broadcastIntent.setAction(PlayerActivity.PlayerServiceResponseReceiver.ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        sendBroadcast(broadcastIntent);
    }

    public void runProgram(final long programID, final int programType) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (programType == GeneralSettings.STACKED_PROGRAM) {
                    boolean bRepeat = false;
                    List<StackedAdItem> adItems = new ArrayList<>();
                    //Loading datas from database
                    Cursor cStackedProgram = getContentResolver().query(ProgramAdContentProvider.STACKED_AD_PROGRAM_URI, null, "_ID=?", new String[]{programID + ""}, null);
                    if (cStackedProgram != null && cStackedProgram.getCount() > 0) {
                        cStackedProgram.moveToFirst();
                        bRepeat = cStackedProgram.getInt(cStackedProgram.getColumnIndex("Repeat")) == 1;
                        cStackedProgram.close();
                    }
                    Cursor c = getContentResolver().query(ProgramAdContentProvider.STACKED_AD_URI, null, "ProgramCode=?", new String[]{programID + ""}, "SerialNo");
                    if (c != null && c.getCount() > 0) {
                        c.moveToFirst();
                        while (!c.isAfterLast()) {
                            StackedAdItem sai = new StackedAdItem();
                            sai.setId(c.getLong(c.getColumnIndex("_ID")));
                            sai.setProgramCode(programID);
                            sai.setSerialNo(c.getInt(c.getColumnIndex("SerialNo")));
                            sai.setAdCode(c.getLong(c.getColumnIndex("AdCode")));
                            sai.setAdType(c.getInt(c.getColumnIndex("AdType")));
                            sai.setAdPath(c.getString(c.getColumnIndex("AdPath")));
                            sai.setAdTimePeriod(c.getInt(c.getColumnIndex("AdTimePeriod")));
                            adItems.add(sai);
                            c.moveToNext();
                        }
                        c.close();
                    }

                    //Running the program
                    for (StackedAdItem sai : adItems) {
                        Intent i = new Intent();
                        i.putExtra("AD_ID", sai.getAdCode());
                        i.putExtra("AD_TYPE", sai.getAdType());
                        createNSendBroadcast(i);

                        long l = System.currentTimeMillis() / 1000L;
                        int adTimePeriod = sai.getAdTimePeriod();
                        //Log.d("StackedPlayer",adTimePeriod +" "+l+" ");
                        //wait till the time is over for current add
                        while (System.currentTimeMillis() / 1000L < (l + adTimePeriod)) {
                            //Log.d("StackedPlayeri",adTimePeriod +"-"+l+"-"+(System.currentTimeMillis() / 1000L));
                        }
                        //Log.d("StackedPlayerb",adTimePeriod +"-"+l+"-"+(System.currentTimeMillis() / 1000L));
                    }

                    //if repeat is on, run for ever
                    while (bRepeat) {
                        for (StackedAdItem sai : adItems) {
                            Intent i = new Intent();
                            i.putExtra("AD_ID", sai.getAdCode());
                            i.putExtra("AD_TYPE", sai.getAdType());
                            createNSendBroadcast(i);

                            long l = System.currentTimeMillis() / 1000L;
                            int adTimePeriod = sai.getAdTimePeriod();
                            //Log.d("StackedPlayer",adTimePeriod +" "+l+" ");
                            //wait till the time is over for current add
                            while (System.currentTimeMillis() / 1000L < (l + adTimePeriod)) {
                                //Log.d("StackedPlayeri",adTimePeriod +"-"+l+"-"+(System.currentTimeMillis() / 1000L));
                                if(destroyed)break;
                            }
                            //Log.d("StackedPlayerb",adTimePeriod +"-"+l+"-"+(System.currentTimeMillis() / 1000L));
                        }
                        if(destroyed)break;
                    }
                } else if (programType == GeneralSettings.SCHEDULED_PROGRAM) {

                    int repeatMask;
                    boolean repeat = false;
                    List<ScheduledAdItem> adItems = new ArrayList<>();
                    Cursor cSheduledProgram = getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_PROGRAM_URI, null, "_ID=?", new String[]{programID + ""}, null);
                    if (cSheduledProgram != null && cSheduledProgram.getCount() > 0) {
                        cSheduledProgram.moveToFirst();
                        repeat = cSheduledProgram.getInt(cSheduledProgram.getColumnIndex("Repeat")) != 0;
                        if (repeat) {
                            repeatMask = cSheduledProgram.getInt(cSheduledProgram.getColumnIndex("RepeatType"));
                        } else {
                            repeatMask = ScheduledAdProgram.REPEAT_TYPE_NONE;
                        }
                        cSheduledProgram.close();

                        Cursor c = getContentResolver().query(ProgramAdContentProvider.SCHEDULED_AD_URI, null, "ProgramCode=?", new String[]{programID + ""}, "AdDateTime");

                        if (c != null && c.getCount() > 0) {
                            c.moveToFirst();
                            while (!c.isAfterLast()) {
                                ScheduledAdItem sai = new ScheduledAdItem();
                                sai.setId(c.getLong(c.getColumnIndex("_ID")));
                                sai.setProgramCode(programID);
                                sai.setAdCode(c.getLong(c.getColumnIndex("AdCode")));
                                sai.setAdType(c.getInt(c.getColumnIndex("AdType")));
                                sai.setAdPath(c.getString(c.getColumnIndex("AdPath")));
                                sai.setAdDateTime(c.getString(c.getColumnIndex("AdDateTime")));
                                adItems.add(sai);
                                c.moveToNext();
                            }
                            c.close();
                        }

                        //Running the program
                        if (!repeat) {
                            for (ScheduledAdItem sai : adItems) {
                                //wait till the date is smaller than current system date
                                //Log.d("Sai", sai.getAdPath() + " " + sai.getAdDateTime().getTime() + " " + new Date().getTime());
                                Date date = FileOperation.convertStringToDateTime(sai.getAdDateTime());
                                while (date.getTime() > new Date().getTime()) ;
                                Intent i = new Intent();
                                i.putExtra("AD_ID", sai.getAdCode());
                                i.putExtra("AD_TYPE", sai.getAdType());
                                createNSendBroadcast(i);
                            }
                        } else {
                            while (repeat) {
                                for (ScheduledAdItem sai : adItems) {
                                    //wait till the date is smaller than current system date
                                    while (true) {
                                        Calendar calNow = Calendar.getInstance();
                                        Calendar calAd = Calendar.getInstance();
                                        Date date = FileOperation.convertStringToDateTime(sai.getAdDateTime());
                                        calAd.setTime(date);
                                        if (repeatMask == ScheduledAdProgram.REPEAT_TYPE_YEARLY) {
                                            if (calAd.get(Calendar.MONTH) <= calNow.get(Calendar.MONTH)) {
                                                if (calAd.get(Calendar.DAY_OF_MONTH) <= calNow.get(Calendar.DAY_OF_MONTH)) {
                                                    if (calAd.get(Calendar.HOUR) <= calNow.get(Calendar.HOUR)) {
                                                        if (calAd.get(Calendar.MINUTE) <= calNow.get(Calendar.MINUTE)) {
                                                            if (calAd.get(Calendar.SECOND) <= calNow.get(Calendar.SECOND)) {
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (repeatMask == ScheduledAdProgram.REPEAT_TYPE_MONTHLY) {
                                            if (calAd.get(Calendar.DAY_OF_MONTH) <= calNow.get(Calendar.DAY_OF_MONTH)) {
                                                if (calAd.get(Calendar.HOUR) <= calNow.get(Calendar.HOUR)) {
                                                    if (calAd.get(Calendar.MINUTE) <= calNow.get(Calendar.MINUTE)) {
                                                        if (calAd.get(Calendar.SECOND) <= calNow.get(Calendar.SECOND)) {
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        } else if (repeatMask == ScheduledAdProgram.REPEAT_TYPE_DAILY) {
                                            if (calAd.get(Calendar.HOUR) <= calNow.get(Calendar.HOUR)) {
                                                if (calAd.get(Calendar.MINUTE) <= calNow.get(Calendar.MINUTE)) {
                                                    if (calAd.get(Calendar.SECOND) <= calNow.get(Calendar.SECOND)) {
                                                        break;
                                                    }
                                                }
                                            }
                                        }
                                        if(destroyed)break;
                                        //Log.d("Match","Not");
                                    }
                                    if(destroyed)break;
                                    //Log.d("Sai", sai.getAdPath() + " " + sai.getAdDateTime().getTime() + " " + new Date().getTime());
                                    Intent i = new Intent();
                                    i.putExtra("AD_ID", sai.getAdCode());
                                    i.putExtra("AD_TYPE", sai.getAdType());
                                    createNSendBroadcast(i);
                                }
                            }
                        }
                    }
                }
            }
        }).start();


    }
}

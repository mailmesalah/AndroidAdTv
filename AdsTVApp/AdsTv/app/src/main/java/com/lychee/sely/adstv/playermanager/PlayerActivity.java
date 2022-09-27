package com.lychee.sely.adstv.playermanager;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.ads.custom.CustomAdItem;
import com.lychee.sely.adstv.ads.custom.design.view.CustomAdView;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.GeneralSettings;
import com.lychee.sely.adstv.extras.Utils;
import com.lychee.sely.adstv.storage.tables.CustomAd;
import com.lychee.sely.adstv.storage.tables.ScheduledAd;
import com.lychee.sely.adstv.storage.tables.StackedAd;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

public class PlayerActivity extends FragmentActivity {

    private int mProgramType;
    private long mProgramID;

    private PlayerManagerService mPlayerService;
    private Messenger mPlayerMessenger;
    private boolean mServiceBound=false;
    private ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
            Log.d("PlayerActivity","onServiceDisconnected()");
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("PlayerActivity","onServiceConnected()");
            mPlayerMessenger=new Messenger(service);
            mServiceBound = true;
            runProgram(mProgramID,mProgramType);
            Log.d("PlayerActivity","onServiceConnected()1");
        }
    };
    private PlayerServiceResponseReceiver mReceiver;
    private Intent mServiceIntent;
    private FrameLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mProgramID=getIntent().getLongExtra("PROGRAM_ID",0);
        mProgramType=getIntent().getIntExtra("PROGRAM_TYPE",0);

        mLayout=(FrameLayout)findViewById(R.id.frame_layout_player);

        IntentFilter filter = new IntentFilter(PlayerServiceResponseReceiver.ACTION);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new PlayerServiceResponseReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mServiceIntent = new Intent(this, PlayerManagerService.class);
        mServiceIntent.putExtra("PROGRAM_ID",mProgramID);
        mServiceIntent.putExtra("PROGRAM_TYPE",mProgramType);
        startService(mServiceIntent);
        bindService(mServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        Log.d("PlayerActivity","onStop()");
        stopService(mServiceIntent);
        super.onStop();
        
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    public class PlayerServiceResponseReceiver extends BroadcastReceiver {
        public static final String ACTION = "com.lychee.sely.adstv.playermanager.BROADCAST_MESSAGE";

        @Override
        public void onReceive(Context context, Intent intent) {
            long adId = intent.getLongExtra("AD_ID", 0);
            int adType = intent.getIntExtra("AD_TYPE", 0);

            if(mProgramType== GeneralSettings.STACKED_PROGRAM){
                if(adType== StackedAd.AD_TYPE_IMAGE){
                    Cursor cAd = getContentResolver().query(ProgramAdContentProvider.IMAGE_AD_URI, null, "_ID=?", new String[]{adId + ""}, null);
                    if(cAd!=null && cAd.getCount()>0){
                        cAd.moveToFirst();
                        String path=cAd.getString(cAd.getColumnIndex("AdPath"));
                        cAd.close();

                        mLayout.removeAllViews();
                        ImageView ivAd = new ImageView(mLayout.getContext());
                        ivAd.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),path));
                        mLayout.addView(ivAd);
                        //Log.d("Player","Added");
                    }

                }else if(adType== StackedAd.AD_TYPE_VIDEO){
                    Cursor cAd = getContentResolver().query(ProgramAdContentProvider.VIDEO_AD_URI, null, "_ID=?", new String[]{adId + ""}, null);
                    if(cAd!=null && cAd.getCount()>0){
                        cAd.moveToFirst();
                        String path=cAd.getString(cAd.getColumnIndex("AdPath"));
                        cAd.close();
                        mLayout.removeAllViews();
                        VideoView vvAd = new VideoView(mLayout.getContext());
                        vvAd.setVideoPath(path);
                        vvAd.start();
                        mLayout.addView(vvAd);

                        //Log.d("Player",path);
                    }

                }else if(adType== StackedAd.AD_TYPE_CUSTOM){
                    mLayout.removeAllViews();

                    //Making and positioning each Ad controls
                    int x1,y1,x2,y2;
                    String path;
                    int adItemType;

                    Cursor cAdItems = getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{adId+""}, "ZOrder");
                    if(cAdItems!=null && cAdItems.getCount()>0){
                        cAdItems.moveToFirst();

                        do {
                            path=cAdItems.getString(cAdItems.getColumnIndex("AdPath"));
                            adItemType=cAdItems.getInt(cAdItems.getColumnIndex("AdType"));
                            x1=cAdItems.getInt(cAdItems.getColumnIndex("X"));
                            y1=cAdItems.getInt(cAdItems.getColumnIndex("Y"));
                            x2=cAdItems.getInt(cAdItems.getColumnIndex("Width"));
                            y2=cAdItems.getInt(cAdItems.getColumnIndex("Height"));

                            if(adItemType== CustomAdView.IMAGE){
                                Bitmap bitmap = FileOperation.getBitmapFromImageFile(Resources.getSystem(), path);
                                Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                                Rect rFrame = new Rect(x1,y1,x2,y2);

                                ImageView ivAd = new ImageView(mLayout.getContext());
                                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(mLayout.getLayoutParams());

                                param.leftMargin = rFrame.left;
                                param.topMargin = rFrame.top;
                                param.width = rFrame.width();
                                param.height = rFrame.height();
                                ivAd.setLayoutParams(param);
                                ivAd.setScaleType(ImageView.ScaleType.FIT_XY);
                                ivAd.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),path));

                                mLayout.addView(ivAd);

                            }else if(adItemType== CustomAdView.VIDEO){
                                Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), path);
                                Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                                Rect rFrame = new Rect(x1,y1,x2,y2);

                                VideoView vvAd = new VideoView(mLayout.getContext());
                                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(mLayout.getLayoutParams());
                                param.leftMargin = rFrame.left;
                                param.topMargin = rFrame.top;
                                param.width = rFrame.width();
                                param.height = rFrame.height();
                                vvAd.setLayoutParams(param);

                                vvAd.setVideoPath(path);
                                vvAd.start();
                                //Adding control to the layout
                                mLayout.addView(vvAd);
                            }

                        }while (cAdItems.moveToNext());
                    }
                    cAdItems.close();
                }
            }else if(mProgramType==GeneralSettings.SCHEDULED_PROGRAM){
                if(adType== ScheduledAd.AD_TYPE_IMAGE){
                    Cursor cAd = getContentResolver().query(ProgramAdContentProvider.IMAGE_AD_URI, null, "_ID=?", new String[]{adId + ""}, null);
                    if(cAd!=null && cAd.getCount()>0){
                        cAd.moveToFirst();
                        String path=cAd.getString(cAd.getColumnIndex("AdPath"));
                        cAd.close();

                        mLayout.removeAllViews();
                        ImageView ivAd = new ImageView(mLayout.getContext());
                        ivAd.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),path));
                        mLayout.addView(ivAd);
                        //Log.d("Player","Added");
                    }

                }else if(adType== ScheduledAd.AD_TYPE_VIDEO){
                    Cursor cAd = getContentResolver().query(ProgramAdContentProvider.VIDEO_AD_URI, null, "_ID=?", new String[]{adId + ""}, null);
                    if(cAd!=null && cAd.getCount()>0){
                        cAd.moveToFirst();
                        String path=cAd.getString(cAd.getColumnIndex("AdPath"));
                        cAd.close();
                        mLayout.removeAllViews();
                        VideoView vvAd = new VideoView(mLayout.getContext());
                        vvAd.setVideoPath(path);
                        vvAd.start();
                        mLayout.addView(vvAd);

                        //Log.d("Player",path);
                    }

                }else if(adType== ScheduledAd.AD_TYPE_CUSTOM){
                    mLayout.removeAllViews();

                    int x1,y1,x2,y2;
                    String path;
                    int adItemType;

                    Cursor cAdItems = getContentResolver().query(ProgramAdContentProvider.CUSTOM_DETAIL_AD_URI, null, "AdId=?", new String[]{adId+""}, "ZOrder");
                    if(cAdItems!=null && cAdItems.getCount()>0){
                        cAdItems.moveToFirst();

                        do {
                            path=cAdItems.getString(cAdItems.getColumnIndex("AdPath"));
                            adItemType=cAdItems.getInt(cAdItems.getColumnIndex("AdType"));
                            x1=cAdItems.getInt(cAdItems.getColumnIndex("X"));
                            y1=cAdItems.getInt(cAdItems.getColumnIndex("Y"));
                            x2=cAdItems.getInt(cAdItems.getColumnIndex("Width"));
                            y2=cAdItems.getInt(cAdItems.getColumnIndex("Height"));

                            if(adItemType== CustomAdView.IMAGE){
                                Bitmap bitmap = FileOperation.getBitmapFromImageFile(Resources.getSystem(), path);
                                Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                                Rect rFrame = new Rect(x1,y1,x2,y2);

                                ImageView ivAd = new ImageView(mLayout.getContext());
                                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(mLayout.getLayoutParams());

                                param.leftMargin = rFrame.left;
                                param.topMargin = rFrame.top;
                                param.width = rFrame.width();
                                param.height = rFrame.height();
                                ivAd.setLayoutParams(param);
                                ivAd.setScaleType(ImageView.ScaleType.FIT_XY);
                                ivAd.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),path));

                                mLayout.addView(ivAd);

                            }else if(adItemType== CustomAdView.VIDEO){
                                Bitmap bitmap = FileOperation.getBitmapFromVideoFile(Resources.getSystem(), path);
                                Rect rImage = new Rect(0,0,bitmap.getWidth(),bitmap.getHeight());
                                Rect rFrame = new Rect(x1,y1,x2,y2);

                                VideoView vvAd = new VideoView(mLayout.getContext());
                                FrameLayout.LayoutParams param = new FrameLayout.LayoutParams(mLayout.getLayoutParams());
                                param.leftMargin = rFrame.left;
                                param.topMargin = rFrame.top;
                                param.width = rFrame.width();
                                param.height = rFrame.height();
                                vvAd.setLayoutParams(param);

                                vvAd.setVideoPath(path);
                                vvAd.start();
                                //Adding control to the layout
                                mLayout.addView(vvAd);
                            }

                        }while (cAdItems.moveToNext());
                    }
                    cAdItems.close();
                }
            }

            /*
            if(adType== StackedAd.AD_TYPE_IMAGE){

                if(mVideoPlayer.isPlaying()){
                    mVideoPlayer.stopPlayback();
                }
                mVideoPlayer.setVisibility(View.GONE);
                mImagePlayer.setVisibility(View.VISIBLE);
                mImagePlayer.setImageDrawable(FileOperation.imgDrawableFromFile(Resources.getSystem(),adPath));
            }else if(adType== StackedAd.AD_TYPE_VIDEO){
                if(mVideoPlayer.isPlaying()){
                    mVideoPlayer.stopPlayback();
                }
                mImagePlayer.setVisibility(View.GONE);
                mVideoPlayer.setVisibility(View.VISIBLE);
                mVideoPlayer.setVideoPath(adPath);
                mVideoPlayer.start();
            }else if(adType== StackedAd.AD_TYPE_CUSTOM){
                if(mVideoPlayer.isPlaying()){
                    mVideoPlayer.stopPlayback();
                }
                mImagePlayer.setVisibility(View.GONE);
                mVideoPlayer.setVisibility(View.VISIBLE);
                mVideoPlayer.setVideoPath(adPath);
                mVideoPlayer.start();
            }
            */

        }
    }

    //Service Methods start
    private void runProgram(long mProgramID, int mProgramType) {

        Message msg = Message.obtain(null, PlayerManagerService.RUN_PROGRAM);
        try {
            Bundle data = new Bundle();
            data.putLong("PROGRAM_ID", mProgramID);
            data.putInt("PROGRAM_TYPE", mProgramType);

            msg.setData(data);
            mPlayerMessenger.send(msg);

        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }

}

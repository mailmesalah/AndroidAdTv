package com.lychee.sely.adstv.ads.video;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.ImageFilePickerActivity;
import com.lychee.sely.adstv.extras.InputDialogActivity;
import com.lychee.sely.adstv.extras.VideoFilePickerActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.io.File;
import java.io.IOException;

public class VideoAdActivity extends FragmentActivity {

    //static values for Activity results
    private static final int RESULT_LOAD_VIDEO =1;
    private static final int RESULT_AD_NAME = 2;

    Button mbBrowseVideo;
    private String adName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_ad);

        mbBrowseVideo = (Button) findViewById(R.id.browse_video_ad);

        mbBrowseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), InputDialogActivity.class);
                i.putExtra("TITLE", "Please Give Name for Ad.");
                startActivityForResult(i, RESULT_AD_NAME);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_VIDEO && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();

            //Adding to Database
            ContentValues cv = new ContentValues();
            cv.put("AdName", adName.trim().equals("") ? "Video Ad" : adName);
            cv.put("AdPath","");
            Uri videoURI = getContentResolver().insert(ProgramAdContentProvider.VIDEO_AD_URI, cv);
            long id = Long.parseLong(videoURI.getLastPathSegment());

            File source= new File(selectedVideo.getPath());
            File destination = new File(Environment.getExternalStorageDirectory().getPath()+"/AdVideos/"+id+selectedVideo.getPath().substring(selectedVideo.getPath().lastIndexOf(".")));

            ContentValues cv1 = new ContentValues();
            cv1.put("AdPath",destination.getPath());
            getContentResolver().update(ProgramAdContentProvider.VIDEO_AD_URI,cv1,"_ID=?",new String[]{id+""});

            try {
                FileOperation.copyFile(source,destination);
            } catch (IOException e) {
                getContentResolver().delete(ProgramAdContentProvider.VIDEO_AD_URI,"_ID=?",new String[]{id+""});
                Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this,"Path is "+destination.getPath(),Toast.LENGTH_SHORT).show();
        }else if (requestCode == RESULT_AD_NAME && resultCode == RESULT_OK) {
            adName = data.getStringExtra("INPUT_VALUE");
            // This always works
            Intent i = new Intent(getBaseContext(), VideoFilePickerActivity.class);
            // This works if you defined the intent filter
            // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

            // Set these depending on your use case. These are the defaults.
            i.putExtra(VideoFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(VideoFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(VideoFilePickerActivity.EXTRA_MODE, VideoFilePickerActivity.MODE_FILE);

            // Configure initial directory by specifying a String.
            // You could specify a String like "/storage/emulated/0/", but that can
            // dangerous. Always use Android's API calls to get paths to the SD-card or
            // internal memory.
            i.putExtra(VideoFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

            startActivityForResult(i, RESULT_LOAD_VIDEO);
        }
    }
}

package com.lychee.sely.adstv.ads.image;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lychee.sely.adstv.R;
import com.lychee.sely.adstv.extras.FileOperation;
import com.lychee.sely.adstv.extras.ImageFilePickerActivity;
import com.lychee.sely.adstv.extras.InputDialogActivity;
import com.lychee.sely.adstv.storage.tables.contentprovider.ProgramAdContentProvider;

import java.io.File;
import java.io.IOException;

public class ImageAdActivity extends FragmentActivity {

    //static values for Activity results
    private static final int RESULT_LOAD_IMAGE = 1;
    private static final int RESULT_AD_NAME = 2;

    Button mbBrowseImage;
    private String adName="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_ad);

        mbBrowseImage = (Button) findViewById(R.id.browse_image_ad);

        mbBrowseImage.setOnClickListener(new View.OnClickListener() {
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

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {


            Uri selectedImage = data.getData();

            //Adding to Database
            ContentValues cv = new ContentValues();
            cv.put("AdPath", "");
            cv.put("AdName", adName.trim().equals("") ? "Image Ad" : adName);
            Uri imageURI = getContentResolver().insert(ProgramAdContentProvider.IMAGE_AD_URI, cv);
            long id = Long.parseLong(imageURI.getLastPathSegment());

            File source = new File(selectedImage.getPath());
            File destination = new File(Environment.getExternalStorageDirectory().getPath() + "/AdImages/" + id + selectedImage.getPath().substring(selectedImage.getPath().lastIndexOf(".")));

            ContentValues cv1 = new ContentValues();
            cv1.put("AdPath", destination.getPath());
            getContentResolver().update(ProgramAdContentProvider.IMAGE_AD_URI, cv1, "_ID=?", new String[]{id + ""});

            try {
                FileOperation.copyFile(source, destination);
            } catch (IOException e) {
                getContentResolver().delete(ProgramAdContentProvider.IMAGE_AD_URI, "_ID=?", new String[]{id + ""});
                Toast.makeText(this, "Failed", Toast.LENGTH_SHORT).show();
            }

            Toast.makeText(this, "Path is " + destination.getPath(), Toast.LENGTH_SHORT).show();
        } else if (requestCode == RESULT_AD_NAME && resultCode == RESULT_OK) {
            adName = data.getStringExtra("INPUT_VALUE");
            // This always works
            Intent i = new Intent(getBaseContext(), ImageFilePickerActivity.class);
            // This works if you defined the intent filter
            // Intent i = new Intent(Intent.ACTION_GET_CONTENT);

            // Set these depending on your use case. These are the defaults.
            i.putExtra(ImageFilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(ImageFilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
            i.putExtra(ImageFilePickerActivity.EXTRA_MODE, ImageFilePickerActivity.MODE_FILE);

            // Configure initial directory by specifying a String.
            // You could specify a String like "/storage/emulated/0/", but that can
            // dangerous. Always use Android's API calls to get paths to the SD-card or
            // internal memory.
            i.putExtra(ImageFilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

            startActivityForResult(i, RESULT_LOAD_IMAGE);
        }
    }
}

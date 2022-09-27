package com.lychee.sely.adstv.extras;

import android.support.annotation.NonNull;

import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

/**
 * Created by Sely on 06-Jan-17.
 */

public class VideoFilePickerFragment extends FilePickerFragment {

    /**
     *
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        boolean ret = super.isItemVisible(file);
        if (ret && !isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            String ext = getExtension(file);
            return ext != null && (".mp4".equalsIgnoreCase(ext)||".avi".equalsIgnoreCase(ext)||".mpeg".equalsIgnoreCase(ext));
        }
        return ret;
    }

}

package com.example.noxqs.crypto.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by noxqs on 25.11.15..
 */
public class ExternalStorageHelper {

    public ExternalStorageHelper() {
    }

    public static void checkExternalMedia(){

        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }


    public static void instantiateFile() {
        File baseWriteLocation = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/CryptoFiles/");
        if (!baseWriteLocation.exists()) {
            baseWriteLocation.mkdirs();
        }
    }
}

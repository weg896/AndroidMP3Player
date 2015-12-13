package com.jerry.mp3player;

import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by test on 12/10/2015.
 */
public class MP3MusicFileReader {

    private final static String TAG = "MP3_Music_File_Reader";
    private static String sdCardDir = null;
    private static String[] musicList = null;

    public static String[] readSDCardMusic(){
        if(sdCardDir == null) {
            sdCardDir = Environment.getExternalStorageDirectory().toString();
            Log.d(TAG, "sdcard dir: " + sdCardDir);
        }

        File file = new File(sdCardDir);

        if(file.isDirectory()) {
            musicList = file.list(new MP3MusicFileFilter());
            return musicList;
        }else{
            Log.d(TAG, "sd card location is not a directory");
            return null;
        }
    }

    public static String getSdCardDir(){
        if(sdCardDir == null) {
            sdCardDir = Environment.getExternalStorageDirectory().toString();
        }
        return sdCardDir;
    }

}

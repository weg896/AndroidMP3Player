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

    public static String[] readSDCardMusic(){
        String str = Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "sdcard dir: " + str);

        File file = new File(str);

        if(file.isDirectory()) {
            return file.list(new MP3MusicFileFilter());
        }else{
            return null;
        }
    }

}

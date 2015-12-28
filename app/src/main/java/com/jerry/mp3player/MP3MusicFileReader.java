package com.jerry.mp3player;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by test on 12/10/2015.
 * this class is for read the music file and folder location
 * for the local storage
 */
public class MP3MusicFileReader {

    private final static String TAG = "MP3_Music_File_Reader";
    private String currentDir = null;
    protected static final String[] musicListHashMapStr = {"name","type","path","icon"};

    private ArrayList<HashMap<String, Object>> musicList = null;
    private final String rootDir;

    public MP3MusicFileReader(){
        rootDir = Environment.getExternalStorageDirectory().getPath();
        currentDir = rootDir;
        Log.d(TAG, "sdcard dir: " + rootDir);
        getDir(rootDir);
    }


    public ArrayList<HashMap<String, Object>> getDir(String dirPath) {
        currentDir = dirPath;
        musicList = new ArrayList<HashMap<String, Object>>();

        HashMap<String, Object> tempMap;

        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(rootDir)){

            tempMap = new HashMap<String, Object>();
            tempMap.put(musicListHashMapStr[0], "../");
            tempMap.put(musicListHashMapStr[1], R.integer.PARENT_PATH);
            tempMap.put(musicListHashMapStr[2], f.getParent());
            tempMap.put(musicListHashMapStr[3], R.drawable.folder_up);

            musicList.add(tempMap);
        }

        for(int i=0; i < files.length; i++) {
            File file = files[i];

            if(!file.isHidden() && file.canRead()){
                tempMap = new HashMap<String, Object>();
                String tempFileName = file.getName();

                if(file.isDirectory()){
                    tempMap.put(musicListHashMapStr[0],tempFileName+"/");
                    tempMap.put(musicListHashMapStr[1],R.integer.FOLDER);
                    tempMap.put(musicListHashMapStr[2], file.getPath());
                    tempMap.put(musicListHashMapStr[3], R.drawable.folder);
                    musicList.add(tempMap);
                }else if(tempFileName.endsWith(".mp3") || tempFileName.endsWith(".ogg")) {
                    tempMap.put(musicListHashMapStr[0],tempFileName);
                    tempMap.put(musicListHashMapStr[1],R.integer.MUSIC_FILE);
                    tempMap.put(musicListHashMapStr[2],file.getPath());
                    tempMap.put(musicListHashMapStr[3], R.drawable.music);
                    musicList.add(tempMap);
                }
            }
        }

        return musicList;
    }

    public String getCurrentDir(){
        return currentDir;
    }
}

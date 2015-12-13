package com.jerry.mp3player;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * Created by test on 12/11/2015.
 */
public class MP3MusicFileFilter implements FilenameFilter {

    @Override
    public boolean accept(File dir, String filename){
        if(filename.endsWith(".mp3")||filename.endsWith(".ogg")){
            return true;
        }
        return false;
    }
}

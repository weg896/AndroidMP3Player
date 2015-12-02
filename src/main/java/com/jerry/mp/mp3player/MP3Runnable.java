package com.jerry.mp.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by test on 11/27/2015.
 */
public class MP3Runnable implements Runnable, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp3Player;
<<<<<<< ace5e16118794f641052c17d2d2b658732ed1093



    public void run(){
=======
    private String mp3url;

    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";

    private final String TAG = "MP3_RUNNABLE";



    public MP3Runnable(String url){
        Log.d(TAG,"Thread/runnable create");
        mp3url = url;
    }

    public void run(){
        Log.d(TAG,"Thread/runnable start");
>>>>>>> just for save work, try to new a thread to handle the mediaPlayer object
        mp3Player = new MediaPlayer();

        //set player properties
        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mp3Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp3Player.setOnPreparedListener(this);
        mp3Player.setOnCompletionListener(this);
        mp3Player.setOnErrorListener(this);
<<<<<<< ace5e16118794f641052c17d2d2b658732ed1093
=======
        try {
            mp3Player.setDataSource(mp3url);
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
            return;
        }
        mp3Player.start();
/*
        while(true){
            try {
                wait();
                Log.d(TAG,"music playing");
            }catch(InterruptedException e){
                Log.e(TAG,e.getMessage());
                return;
            }

        }*/
>>>>>>> just for save work, try to new a thread to handle the mediaPlayer object
    }

    //////////////////////////////////////////////////////////////
    // MediaPlayer interface
    public boolean onError(MediaPlayer mp,int what, int extra){
<<<<<<< ace5e16118794f641052c17d2d2b658732ed1093
/*
=======

>>>>>>> just for save work, try to new a thread to handle the mediaPlayer object
        switch(what) {
            case MediaPlayer.MEDIA_ERROR_UNKNOWN:
                messageWhat = "MEDIA_ERROR_UNKNOWN.";
                break;
            case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                messageWhat = "MEDIA_ERROR_SERVER_DIED.";
                break;
        }

        switch(extra){
            case MediaPlayer.MEDIA_ERROR_IO:
                messageExtra = "MEDIA_ERROR_IO.";
                break;
            case MediaPlayer.MEDIA_ERROR_MALFORMED:
                messageExtra = "MEDIA_ERROR_MALFORMED.";
                break;
            case MediaPlayer.MEDIA_ERROR_UNSUPPORTED:
                messageExtra = "MEDIA_ERROR_UNSUPPORTED.";
                break;
            case MediaPlayer.MEDIA_ERROR_TIMED_OUT:
                messageExtra = "MEDIA_ERROR_TIMED_OUT.";
                break;
            default:
                messageExtra = "maybe MEDIA_ERROR_SYSTEM.(low level)";
        }
<<<<<<< ace5e16118794f641052c17d2d2b658732ed1093
        Log.e(TAG, messageWhat + " " + messageExtra);*/
=======
        Log.e(TAG, messageWhat + " " + messageExtra);
>>>>>>> just for save work, try to new a thread to handle the mediaPlayer object
        return false;
    }

    public void onPrepared(MediaPlayer mp){
<<<<<<< ace5e16118794f641052c17d2d2b658732ed1093
        //Log.d(TAG,"onPrepared called");
=======
        Log.d(TAG,"onPrepared called");
>>>>>>> just for save work, try to new a thread to handle the mediaPlayer object
        mp.start();
    }

    public void onCompletion(MediaPlayer mp){
        // TODO:
    }


}

package com.jerry.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by test on 11/27/2015.
 * this class is for experiment,
 * test if media player object can run in a new thread rather that the UI thread
 */
public class MP3Runnable implements Runnable, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp3Player;



    public void run(){
        mp3Player = new MediaPlayer();

        //set player properties
        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mp3Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp3Player.setOnPreparedListener(this);
        mp3Player.setOnCompletionListener(this);
        mp3Player.setOnErrorListener(this);
    }

    //////////////////////////////////////////////////////////////
    // MediaPlayer interface
    public boolean onError(MediaPlayer mp,int what, int extra){
/*
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
        Log.e(TAG, messageWhat + " " + messageExtra);*/
        return false;
    }

    public void onPrepared(MediaPlayer mp){
        //Log.d(TAG,"onPrepared called");
        mp.start();
    }

    public void onCompletion(MediaPlayer mp){
        // TODO:
    }


}

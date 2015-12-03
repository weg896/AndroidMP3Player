

package com.jerry.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Looper;
import android.util.Log;

/**
 * Created by test on 11/27/2015.
 */
public class MP3Runnable implements Runnable, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private MediaPlayer mp3Player;
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
        if(Looper.myLooper() != Looper.getMainLooper()){
            Log.d(TAG,"using a new looper");
        }else{
            Log.d(TAG,"using a UI looper");
        }

        mp3Player = new MediaPlayer();

        //set player properties
        //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mp3Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp3Player.setOnPreparedListener(this);
        mp3Player.setOnCompletionListener(this);
        mp3Player.setOnErrorListener(this);
        try {
            mp3Player.setDataSource(mp3url);
            mp3Player.prepareAsync();
        }catch(Exception e){
            Log.e(TAG,e.getMessage());
            return;
        }



        while(true){
            try {
                Thread.sleep(1000);
                //Log.d(TAG,"music playing");
            }catch(InterruptedException e){
                Log.e(TAG,e.getMessage());
                return;
            }

        }
    }

    //////////////////////////////////////////////////////////////
    // MediaPlayer interface
    public boolean onError(MediaPlayer mp,int what, int extra){

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
        Log.e(TAG, messageWhat + " " + messageExtra);
        return false;
    }

    public void onPrepared(MediaPlayer mp){
        Log.d(TAG,"onPrepared called");
        mp.start();
    }

    public void onCompletion(MediaPlayer mp){
        // TODO:
    }


}

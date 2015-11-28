package com.jerry.mp.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.util.Log;

import java.io.IOException;

/**
 * Created by test on 11/26/2015.
 */
public class MP3Service extends Service implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    // define mp3 player action for Intent class
    public static final String ACTION_PLAY = "com.jerry.mp.mp3player.PLAY";
    public static final String ACTION_PAUSE = "com.jerry.mp.mp3player.PAUSE";

    private static MediaPlayer mediaPlayer = null;
    private final String TAG = "MP3_SERVICE";
    private final IBinder mBinder = new MusicBinder() ;

    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";

    public void onCreate(){
        super.onCreate();
        mediaPlayerInit();
        Log.d(TAG,"onCrate called");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG,"onStartCommand called");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG,"onBind called");

        // allow other component bind to this service
        // so return a binder object,
        return mBinder;
    }


    private void mediaPlayerInit(){
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            //set player properties
            //mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
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
        Log.e(TAG,messageWhat+" "+messageExtra);
        return false;
    }

    public void onPrepared(MediaPlayer mp){
        Log.d(TAG,"onPrepared called");
        mp.start();
    }

    public void onCompletion(MediaPlayer mp){
        // TODO:
    }



    public class MusicBinder extends Binder {
        MP3Service getService(){
            return MP3Service.this;
        }
    }


    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
    }

    ///////////////////////////////////////////

    public void musicPlayThis(String url){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public void startMusic(){
        mediaPlayer.start();
    }

    public void pauseMusic(){
        mediaPlayer.pause();
    }

    public void stopMusic(){
        mediaPlayer.stop();
    }
}

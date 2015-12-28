package com.jerry.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.io.IOException;

/**
 * Created by test on 11/26/2015.
 * this service holds a media player object
 * and it allows music playing in background
 */
public class MP3Service extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    private static final String TAG = "MP3_SERVICE";

    private final IBinder musicBinder = new MusicBinder() ;
    private final MediaPlayer mp3Player = new MediaPlayer();
    private MP3ControlListener controlInterface = null;
    private String currentMusicName="";

    private boolean startAfterPrepared = false;
    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";

    public void onCreate(){
        super.onCreate();
        mediaPlayerInit();
        Log.d(TAG,"onCrate called");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand called");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG, "onBind called");
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind called");
        stopMusic();
        // All clients have unbound with unbindService()
        return true;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
        Log.d(TAG, "onRebind called");
    }

    private void mediaPlayerInit(){
        mp3Player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp3Player.setOnPreparedListener(this);
        mp3Player.setOnCompletionListener(this);
        mp3Player.setOnErrorListener(this);
    }


    //////////////////////////////////////////////////////////////
    // inner class space
    public class MusicBinder extends Binder {
        MP3Service getService(){
            return MP3Service.this;
        }
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
        if(startAfterPrepared) {
            mp.start();
        }
    }

    public void onCompletion(MediaPlayer mp){
        // TODO:
    }

    /////////////////////////////////////////////////////////////
    // music control functions

    public void musicPlayThis(String url, boolean startPlay, String name){
        startAfterPrepared = startPlay;
        try {
            if(controlInterface != null) {
                controlInterface.onUpdateMusicName(name);
                currentMusicName=name;
            }
            mp3Player.reset();
            mp3Player.setDataSource(url);
            mp3Player.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    public boolean isPlaying(){
        return mp3Player.isPlaying();
    }

    public void startMusic(){
        mp3Player.start();
    }

    public void pauseMusic(){
        mp3Player.pause();
    }

    public void stopMusic(){
        mp3Player.stop();
        mp3Player.release();
    }

    public String getCurrentMusicName(){
        return currentMusicName;
    }

    public int musicDuration(){
        return mp3Player.getDuration();
    }

    public int musicCurrentPosition(){
        return mp3Player.getCurrentPosition();
    }

    public void setMusicCurrentPosition(int position){
        Log.d(TAG,"seek to position:"+position);
        mp3Player.seekTo(position);
    }


    // after set the control listener,
    // this MP3Service can change the MP3ControlFragment's view
    // the implementation is in MP3ControlFragment
    public void setControlListener(MP3ControlListener controlInterface){
        this.controlInterface = controlInterface;
        this.controlInterface.onDurationPrepared();
    }

}

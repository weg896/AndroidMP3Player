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

/**
 * Created by test on 11/26/2015.
 */
public class MP3Service extends Service implements
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    private static final String ACTION_PLAY = "com.example.action.PLAY";

    private MediaPlayer mediaPlayer = null;
    private final String TAG = "MP3Service: ";
    private final IBinder mBinder = new MusicBinder() ;

    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";

    public void onCreate(){
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        if(intent.getAction().equals(ACTION_PLAY)){
            mediaPlayer = new MediaPlayer();

            //set player properties
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnErrorListener(this);
        }
        return 0;
    }

    @Override
    public IBinder onBind(Intent arg0){
        return null;
    }


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
}

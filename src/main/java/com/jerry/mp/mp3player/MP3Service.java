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
public class MP3Service extends Service {

    // define mp3 player action for Intent class
    public static final String ACTION_PLAY = "com.jerry.mp.mp3player.PLAY";
    public static final String ACTION_PAUSE = "com.jerry.mp.mp3player.PAUSE";

    private final String TAG = "MP3_SERVICE";
    private final IBinder mBinder = new MusicBinder() ;

    private Thread mp3Thread;

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
        Log.d(TAG,"onBind called");

        // allow other component bind to this service
        // so return a binder object,
        return mBinder;
    }

    private void mediaPlayerInit(){
        mp3Thread = new Thread(new MP3Runnable("http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3"));
        mp3Thread.start();
    }


    public class MusicBinder extends Binder {
        MP3Service getService(){
            return MP3Service.this;
        }
    }


    public boolean isPlaying(){
        //return mediaPlayer.isPlaying();
        return false;
    }

    ///////////////////////////////////////////

    public void musicPlayThis(String url){
        /*try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }*/
    }


    public void startMusic(){
        //mediaPlayer.start();
    }

    public void pauseMusic(){
        //mediaPlayer.pause();
    }

    public void stopMusic(){
        //mediaPlayer.stop();
    }
}

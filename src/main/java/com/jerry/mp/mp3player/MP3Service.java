package com.jerry.mp.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
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
    private static final String TAG = "MP3_SERVICE";


    private final IBinder musicBinder = new MusicBinder() ;


    private Thread mp3Thread;

    private Looper serviceLooper;
    private ServiceHandler serviceHandler;

    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";

    public void onCreate(){
        super.onCreate();
        mp3Thread= new Thread(,"mp3Thread");

        mp3Thread.start();
        serviceLooper = thread.getLooper();
        serviceHandler = new ServiceHandler(serviceLooper);

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

        Message msg = serviceHandler.obtainMessage();
        serviceHandler.sendMessage(msg);

        // allow other component bind to this service
        // so return a binder object,
        return musicBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // All clients have unbound with unbindService()
        return true;
    }
    @Override
    public void onRebind(Intent intent) {
        // A client is binding to the service with bindService(),
        // after onUnbind() has already been called
    }


    private void mediaPlayerInit(){
        if(mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();

        }
    }






    //////////////////////////////////////////////////////////////
    // inner class space

    public class MusicBinder extends Binder {
        MP3Service getService(){
            return MP3Service.this;
        }
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            Log.i(TAG, "Inside handle Message");

            while (true) {
                synchronized (this) {
                    try {
                        wait(50);

                        switch (msg.arg1){
                            case 0:
                                break;
                            default:
                                ;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
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


    public boolean isPlaying(){
        return mediaPlayer.isPlaying();
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



package com.jerry.mp3player;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
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
    private Handler mp3Handler;
    private MP3Runnable runn;

    public void onCreate(){
        super.onCreate();

        Log.d(TAG,"onCrate called");
    }

    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand called");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        Log.d(TAG,"onBind called");
        mediaPlayerInit();
        // allow other component bind to this service
        // so return a binder object,
        return mBinder;
    }

    private void mediaPlayerInit(){
        if(Looper.myLooper() != Looper.getMainLooper()){
            Log.d(TAG,"using a new looper");
        }else{
            Log.d(TAG,"using a UI looper");
        }

        runn = new MP3Runnable("/sdcard/64.mp3");
        mp3Thread = new Thread(runn);
        runn.testLooper();
        mp3Thread.setName("wei xiong thread");
        runn.setThread(mp3Thread);
        mp3Thread.start();



        mp3Handler = new Handler();
        mp3Handler.postDelayed(new testRunnable(), 20);

    }


    public class testRunnable implements Runnable{
        public void run(){
            runn.testLooper();
            if(mp3Thread.getState() == Thread.State.TERMINATED) {
                //Log.d(TAG, "thread terminated");
            }else if(mp3Thread.getState() == Thread.State.BLOCKED){
                //Log.d(TAG, "thread blocked");
                mp3Handler.postDelayed(testRunnable.this, 100);
            }else if(mp3Thread.getState() == Thread.State.NEW){
                //Log.d(TAG, "thread new");
                mp3Handler.postDelayed(testRunnable.this, 100);
            }else if(mp3Thread.getState() == Thread.State.RUNNABLE){
                //Log.d(TAG, "thread runnable");
                mp3Handler.postDelayed(testRunnable.this, 100);
            }else if(mp3Thread.getState() == Thread.State.TIMED_WAITING){
                //Log.d(TAG, "thread TIMED_WAITING");
                mp3Handler.postDelayed(testRunnable.this, 100);
            }
        }
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

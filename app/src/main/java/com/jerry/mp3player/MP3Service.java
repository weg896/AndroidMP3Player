package com.jerry.mp3player;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by test on 11/26/2015.
 * this service holds a media player object
 * and it allows music playing in background
 */
public class MP3Service extends Service implements MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener{

    public static final String ACTION = "play_pause_music";

    private static final String TAG = "MP3_SERVICE";

    private final IBinder musicBinder = new MusicBinder() ;
    private final MediaPlayer mp3Player = new MediaPlayer();
    private MP3ControlListener controlInterface = null;
    private String currentMusicName="/sdcard/gate.ogg";

    private final MP3ServiceActionReceiver serviceActionReceiver = new MP3ServiceActionReceiver();

    private boolean startAfterPrepared = false;
    // for OnErrorListener
    private String messageWhat = "";
    private String messageExtra = "";


    public void onCreate(){
        super.onCreate();
        mediaPlayerInit();
        Log.d(TAG,"onCrate called");
    }

    // mp3Service as a broadcast sender, and mp3AppWidgetProvider as a broadcast receiver
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.d(TAG, "onStartCommand called "+this.toString());
        setAppWidgetClickEvent(intent);
        setServiceActionReceiver();
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
        // stopMusic();
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
    }

    public void releaseMusic(){
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


    private void setAppWidgetClickEvent(Intent intent){
        Log.d(TAG,"setAppWidgetClickEvent");

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());
        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        for (int widgetId : allWidgetIds) {
            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_mp3);
            remoteViews.setTextViewText(R.id.music_name, currentMusicName);

            // Register an onClickListener for appWidget button
            Intent clickIntent = new Intent(this.getApplicationContext(), MP3AppWidgetProvider.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.play_stop, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }

    // set up broadcast receiver for responding music action
    private void setServiceActionReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION);
        registerReceiver(serviceActionReceiver, intentFilter);
    }


    // an inner class for responding music action from appWidget
    public class MP3ServiceActionReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "MP3ServiceActionReceiver-onReceive " + intent.getAction());
            if(mp3Player.isPlaying()){
                mp3Player.pause();
            }else{
                musicPlayThis(currentMusicName, false, currentMusicName);
            }
        }
    }
}

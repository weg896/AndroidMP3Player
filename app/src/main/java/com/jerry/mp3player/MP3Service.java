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

    public static final String MUSIC_TAG = "com.jerry.mp3player.MUSIC_TAG";
    public static final String MUSIC_CODE = "com.jerry.mp3player.MUSIC_CODE";
    public static final String MUSICING = "com.jerry.mp3player.MUSICING";
    public static final int MUSIC_PLAY = 0;
    public static final int MUSIC_PAUSE = 1;
    public static final int MUSIC_STOP = 2;

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
        musicPlayThis(currentMusicName, false, currentMusicName);
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
        Log.d(TAG, "onError");
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
        if(startAfterPrepared) {
            mp.start();
        }
    }

    public void onCompletion(MediaPlayer mp){
        Log.d(TAG, "onCompletion");
        if(controlInterface != null) {
            controlInterface.onUpdatePlayPauseButton(false);
        }
    }

    /////////////////////////////////////////////////////////////
    // music control functions

    public void musicPlayThis(String url, boolean startPlay, String name){
        startAfterPrepared = startPlay;
        if(controlInterface != null) {
            controlInterface.onUpdateMusicName(name);
            controlInterface.onUpdatePlayPauseButton(startPlay);
            currentMusicName = name;
        }
        try {
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
        if(controlInterface != null) {
            controlInterface.onUpdatePlayPauseButton(true);
        }
    }

    public void pauseMusic(){
        mp3Player.pause();
        if(controlInterface != null) {
            controlInterface.onUpdatePlayPauseButton(false);
        }
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
            remoteViews.setTextViewText(R.id.widg_music_name, currentMusicName);

            // Register an onClickListener for appWidget button
            Intent clickIntent = new Intent(this.getApplicationContext(), MP3AppWidgetProvider.class);
            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widg_play_stop, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }

    }

    // set up broadcast receiver for responding music action
    private void setServiceActionReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MUSIC_TAG);
        registerReceiver(serviceActionReceiver, intentFilter);
    }


    // an inner class for responding music action from appWidget
    public class MP3ServiceActionReceiver extends BroadcastReceiver{
        public void onReceive(Context context, Intent intent){
            Log.d(TAG, "MP3ServiceActionReceiver-onReceive " + intent.getAction());

            int actionCode = intent.getIntExtra(MUSIC_CODE,-1);

            switch(actionCode) {
                /*case MUSIC_PLAY:
                    mp3Player.start();
                    break;
                case MUSIC_PAUSE:
                    mp3Player.pause();
                    break;*/
                case MUSIC_STOP:
                    mp3Player.stop();
                    break;
                default: //do nothing
                    if (mp3Player.isPlaying()) {
                        mp3Player.pause();
                    } else {
                        mp3Player.start();
                        //musicPlayThis(currentMusicName, true, currentMusicName);
                    }
            }
        }
    }
}

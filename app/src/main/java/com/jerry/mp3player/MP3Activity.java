package com.jerry.mp3player;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MP3Activity extends AppCompatActivity {

    private static final String TAG = "MP3_PLAYER";

    private MP3ControlFragment controlFragment;
    private MP3PlayListFragment playlistFragment;

    private MP3Service mp3Service;
    private String sampleMP3URL = "/sdcard/gate.ogg";

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // get service
            mp3Service = ((MP3Service.MusicBinder)service).getService();
            controlFragment.setMP3Service(mp3Service);
            mp3Service.musicPlayThis(sampleMP3URL);

            Log.d(TAG, "onServiceConnected called");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mp3Service = null;
            Log.d(TAG,"onServiceDisconnected called");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        FragmentManager fm = getFragmentManager();
        controlFragment = (MP3ControlFragment) fm.findFragmentById(R.id.fragment_control);
        Log.d(TAG, "onCreate--before bind");

        Intent intent = new Intent(this,MP3Service.class);
        bindService(intent, musicConnection, BIND_AUTO_CREATE);

        Log.d(TAG, "onCreate--after bind");
    }



    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        unbindService(musicConnection);
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

}

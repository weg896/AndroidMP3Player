package com.jerry.mp.mp3player;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MP3Activity extends AppCompatActivity {

    // screen view component
    private Button controlButton; // start and stop
    private Button forward5Button; // forward 5 second
    private Button backward5Button; // backward 5 second
    private TextView musicNameTextView; // show the music's name
    private SeekBar progressingBar;

    // music player

    private Handler progressingBarHandler = null;
    private int progressingPosition=0;
    //MediaPlayer mediaPlayer;
    private MP3Service mp3Service;
    private String sampleMP3URL = "/sdcard/64.mp3";
    private String TAG = "MP3_PLAYER";


    ComponentName componentName;

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // get service
            mp3Service = ((MP3Service.MusicBinder)service).getService();
            // set music
            mp3Service.musicPlayThis(sampleMP3URL);
            Log.d(TAG,"onServiceConnected called");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mp3Service = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        Log.d(TAG, "onCreate--before bind");
        Intent intent = new Intent(this,MP3Service.class);
        bindService(intent, musicConnection, BIND_AUTO_CREATE);

        //sampleMP3URL = Environment.getExternalStorageDirectory().toString()+"/Music/64.mp3";

        progressingBarHandler = new Handler();
        progressingBarHandler.postDelayed(progressingBarUpdate,100);

        screenViewComponentsInit();

        // initial the media player

       // musicDuration = mediaPlayer.getDuration();

       //
        Log.d(TAG, "onCreate--after bind");

    }
/*


*/
    private void screenViewComponentsInit(){
        // initial the screen view components
        controlButton = (Button) findViewById(R.id.play_pause_button);
        forward5Button = (Button) findViewById(R.id.forward_5_button);
        backward5Button = (Button) findViewById(R.id.backward_5_button);

        musicNameTextView = (TextView) findViewById(R.id.music_name_textView);
        progressingBar = (SeekBar) findViewById(R.id.progressing_bar);

        // set up listener for screen view components
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (mp3Service.isPlaying()) { // currently playing music, stop it
                    Toast.makeText(getApplicationContext(), "Pause music", Toast.LENGTH_SHORT).show();
                    mp3Service.pauseMusic();
                } else { // currently no music played, start it
                    Toast.makeText(getApplicationContext(), "Play music", Toast.LENGTH_SHORT).show();

                    mp3Service.startMusic();
                }
            }
        });

        /*
        forward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });

        backward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        */
    }


    @Override
    protected void onDestroy() {
        unbindService(musicConnection);
        super.onDestroy();

       // mediaPlayer.release();
       // mediaPlayer = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    ////////////////////////////////////////


    // update duration
    private Runnable progressingBarUpdate = new Runnable(){
        public void run(){
            if(mp3Service != null) {
                progressingPosition = mp3Service.musicCurrentPosition() * progressingBar.getMax() / mp3Service.musicDuration();
                progressingBar.setProgress(progressingPosition);
                //Log.d(TAG, "time: " + progressingPosition + " musCp: " + mp3Service.musicCurrentPosition() + " musDr: " + mp3Service.musicDuration() + " progeMa: " + progressingBar.getMax());
            }
            progressingBarHandler.postDelayed(this,100);
        }
    };
}

package com.jerry.mp3player;

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
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MP3Activity extends AppCompatActivity implements MP3SeekBarInterface {

    // screen view component
    private ImageButton controlButton; // start and stop
    private Button forward5Button; // forward 5 second
    private Button backward5Button; // backward 5 second
    private TextView musicNameTextView; // show the music's name
    private SeekBar seekBar;

    // music player

    private Handler seekBarHandler = null;
    private boolean seekFromUser=false;
    //MediaPlayer mediaPlayer;
    private MP3Service mp3Service;
    private String sampleMP3URL = "/sdcard/gate.ogg";
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
/*
    public setListener(MP3FilesAbstract context);{

    }

*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);
        Log.d(TAG, "onCreate--before bind");
        Intent intent = new Intent(this,MP3Service.class);
        bindService(intent, musicConnection, BIND_AUTO_CREATE);

        //sampleMP3URL = Environment.getExternalStorageDirectory().toString()+"/Music/64.mp3";

        seekBarHandler = new Handler();
        seekBarHandler.postDelayed(seekBarUpdate,100);

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
        controlButton = (ImageButton) findViewById(R.id.play_pause_button);
        forward5Button = (Button) findViewById(R.id.forward_5_button);
        backward5Button = (Button) findViewById(R.id.backward_5_button);

        musicNameTextView = (TextView) findViewById(R.id.music_name_textView);
        seekBar = (SeekBar) findViewById(R.id.progressing_bar);

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

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser){
                if(fromuser) {
                    seekFromUser = true;
                    progressChanged = progress;
                    Log.d(TAG, "progress changing " + progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar){
                Log.d(TAG, "progress start touch ");
                seekFromUser = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar){
                Log.d(TAG, "progress stop touch ");
                mp3Service.setMusicCurrentPosition(progressChanged);
                seekBar.setProgress(progressChanged);
                seekFromUser = false;
            }
        });


        forward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3Service.setMusicCurrentPosition(seekBar.getMax() / 2);
            }
        });

        /*
        backward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });
        */
    }

    public void onDurationPrepared(int duration){
        seekBar.setMax(duration);
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
    private Runnable seekBarUpdate = new Runnable(){
        public void run(){
            if(mp3Service != null && !seekFromUser) {
                int seekPosition = mp3Service.musicCurrentPosition() / mp3Service.musicDuration();
                seekBar.setProgress(seekPosition);
                //Log.d(TAG, "time: " + progressingPosition + " musCp: " + mp3Service.musicCurrentPosition() + " musDr: " + mp3Service.musicDuration() + " progeMa: " + progressingBar.getMax());
            }
            seekBarHandler.postDelayed(this,100);
        }
    };

    public interface MP3FilesAbstract {

        public void setListener(MP3FilesAbstract context);

        public void onListener();
    }
}

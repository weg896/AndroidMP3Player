package com.jerry.mp.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MP3Activity extends AppCompatActivity {

    Button controlButton; // start and stop
    Button forward5Button; // forward 5 second
    Button backward5Button; // backward 5 second

    TextView musicNameTextView; // show the music's name
    SeekBar processingBar;

    MediaPlayer mediaPlayer;
    String sampleMP3URL = "http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3";
    String MP3PlayerTAG = "mp3 player";
    int musicDuration = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);

        // initial the screen view components
        controlButton = (Button) findViewById(R.id.play_pause_button);
        forward5Button = (Button) findViewById(R.id.forward_5_button);
        backward5Button = (Button) findViewById(R.id.backward_5_button);

        musicNameTextView = (TextView) findViewById(R.id.music_name_textView);
        processingBar = (SeekBar) findViewById(R.id.progressing_bar);

        // initial the media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(sampleMP3URL);
            mediaPlayer.prepare();
        } catch (IOException e) {
            Log.e("IO error", e.getMessage());
            return;
        }
        musicDuration = mediaPlayer.getDuration();


        // set up listener for screen view components
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) { // currently playing music, stop it
                    Toast.makeText(getApplicationContext(), "Pause music", Toast.LENGTH_SHORT).show();
                    mediaPlayer.pause();
                } else { // currently no music played, start it
                    Toast.makeText(getApplicationContext(), "Play music", Toast.LENGTH_SHORT).show();
                    mediaPlayer.start();
                }
            }
        });

        forward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i(MP3PlayerTAG, "have called onDestroy()");

        mediaPlayer.release();
        mediaPlayer = null;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}

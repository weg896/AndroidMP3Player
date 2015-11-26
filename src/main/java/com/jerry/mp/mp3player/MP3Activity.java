package com.jerry.mp.mp3player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

public class MP3Activity extends AppCompatActivity {

    MediaPlayer mediaPlayer;

    String sampleMP3URL = "http://www.stephaniequinn.com/Music/Allegro%20from%20Duet%20in%20C%20Major.mp3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(sampleMP3URL);
            mediaPlayer.prepare();
        }catch(IOException e){
            Log.e("IO error", e.getMessage());
        }
        mediaPlayer.start();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}

package com.jerry.mp3player;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by test on 12/5/2015.
 */
public class MP3ControlFragment extends Fragment{

    private static final String TAG = "MP3_CONTROL_FRAGMENT";

    // screen view component
    private ImageButton controlButton = null; // start and stop
    private Button forward5Button = null; // forward 5 second
    private Button backward5Button = null; // backward 5 second
    private TextView musicNameTextView = null; // show the music's name
    private TextView musicTimeTextView = null; // show the music's duration and current
    private SeekBar seekBar = null;
    private String currentMusicDuration = "0:00";

    private MP3Service mp3Service = null;

    private Handler seekBarHandler = null;
    private boolean seekFromUser = false;

    private View controlView = null;

    // implement view component here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        controlView = inflater.inflate(R.layout.fragment_mp3_control, container, false);
        // initial the screen view components
        controlButton = (ImageButton) controlView.findViewById(R.id.play_pause_button);
        forward5Button = (Button) controlView.findViewById(R.id.forward_5_button);
        backward5Button = (Button) controlView.findViewById(R.id.backward_5_button);

        musicNameTextView = (TextView) controlView.findViewById(R.id.music_name_textView);
        musicTimeTextView = (TextView) controlView.findViewById(R.id.music_time_textView);
        seekBar = (SeekBar) controlView.findViewById(R.id.progressing_bar);

        Log.d(TAG,"onCreateView ");
        return controlView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ++"+MP3ControlFragment.this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }

    public void setMP3Service(MP3Service mp3Ser) {
        mp3Service = mp3Ser;
        setViewListener();
    }

    private void setViewListener(){
        if(controlView == null){
            Log.d(TAG, "controlView == null, setViewListener()");
            return;
        }else if(mp3Service == null){
            Log.d(TAG, "mp3service == null, setViewListener()");
            return;
        }

        mp3Service.setSeekBarListener(new MP3SeekBarListener() {
            public void onDurationPrepared() {
                int duration = mp3Service.musicDuration();
                seekBar.setMax(duration);
                currentMusicDuration = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(duration),
                        TimeUnit.MILLISECONDS.toSeconds(duration) -
                                TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(duration)
                                ));
                int prog = seekBar.getProgress();
                String timeCur = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(prog),
                        TimeUnit.MILLISECONDS.toSeconds(prog) -
                                TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(prog)
                                ));

                musicTimeTextView.setText(timeCur+"/"+currentMusicDuration);
            }
        });
        Log.d(TAG, "setMP3Service ");


        // set up listener for screen view components
        controlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mp3Service.isPlaying()) { // currently playing music, stop it
                    Toast.makeText(getActivity().getApplicationContext(), "Pause music", Toast.LENGTH_SHORT).show();
                    mp3Service.pauseMusic();
                } else { // currently no music played, start it
                    Toast.makeText(getActivity().getApplicationContext(), "Play music", Toast.LENGTH_SHORT).show();
                    mp3Service.startMusic();
                }
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromuser) {
                if (fromuser) {
                    seekFromUser = true;
                    progressChanged = progress;
                    Log.d(TAG, "progress changing " + progress);
                }
            }

            public void onStartTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "progress start touch ");
                seekFromUser = false;
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d(TAG, "progress stop touch ");
                mp3Service.setMusicCurrentPosition(progressChanged);
                seekBar.setProgress(progressChanged);
                seekFromUser = false;
            }
        });

        forward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mp3Service.musicPlayThis("/sdcard/74.mp3", true);
            }
        });

        backward5Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        seekBarHandler = new Handler();
        seekBarHandler.postDelayed(seekBarUpdate, 100);
    }


    // update duration
    private Runnable seekBarUpdate = new Runnable(){
        public void run(){
            if(mp3Service != null && !seekFromUser) {

                int duration = mp3Service.musicDuration();
                if(seekBar.getMax() != duration) {
                    seekBar.setMax(duration);
                    currentMusicDuration = String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(duration),
                            TimeUnit.MILLISECONDS.toSeconds(duration) -
                                    TimeUnit.MINUTES.toSeconds(
                                            TimeUnit.MILLISECONDS.toMinutes(duration)
                                    ));
                }

                int seekPosition = mp3Service.musicCurrentPosition();
                seekBar.setProgress(seekPosition);
                String timeCur = String.format("%02d:%02d",
                        TimeUnit.MILLISECONDS.toMinutes(seekPosition),
                        TimeUnit.MILLISECONDS.toSeconds(seekPosition) -
                                TimeUnit.MINUTES.toSeconds(
                                        TimeUnit.MILLISECONDS.toMinutes(seekPosition)
                                ));

                musicTimeTextView.setText(timeCur + "/" + currentMusicDuration);
                //Log.d(TAG, "seekP:"+seekPosition+" musCp: " + mp3Service.musicCurrentPosition() + " musDr: " + mp3Service.musicDuration());
            }
            seekBarHandler.postDelayed(this,100);
        }
    };

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Log.d(TAG, "onAttach");
    }

    @Override
    public void onDetach(){
        super.onDetach();
        Log.d(TAG, "onDetach");
    }

    @Override
    public void onStart(){
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        Log.d(TAG, "onDestroyView");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    public void onResume(){
        super.onResume();
        setViewListener();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public boolean isViewCreated(){
        return controlView != null;
    }

    public boolean isServiceBound(){
        return mp3Service != null;
    }
}

package com.jerry.mp3player;

/**
 * Created by test on 12/2/2015.
 * This interface allow MP3Service communicate with MP3ControlFragment
 */
public interface MP3ControlListener {

    public void onDurationPrepared();

    public void onUpdateMusicName(String name);

}

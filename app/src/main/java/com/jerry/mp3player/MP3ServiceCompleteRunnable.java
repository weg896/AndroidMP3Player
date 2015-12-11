package com.jerry.mp3player;

/**
 * Created by test on 12/10/2015.
 */
public class MP3ServiceCompleteRunnable implements Runnable{

    private boolean isServiceCompleted = false;

    public void run(){

    }

    public void setServiceCompleted(boolean completed){
        isServiceCompleted = completed;
    }
}

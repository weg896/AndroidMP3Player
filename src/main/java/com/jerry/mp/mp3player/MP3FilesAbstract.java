package com.jerry.mp.mp3player;

import android.content.Context;

/**
 * Created by test on 12/1/2015.
 */
public abstract class MP3FilesAbstract {
    private String mp3url;
    private Context context;

    public void setListener(Context context){
        this.context = context;
    }

    public Context getContext() {

        return context;
    }

    public abstract void onListener();
}

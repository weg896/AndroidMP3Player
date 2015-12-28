package com.jerry.mp3player;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by test on 12/9/2015.
 * this fragment is for maintaining connection to a service during screen rotation
 */
public class MP3ConfigFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}

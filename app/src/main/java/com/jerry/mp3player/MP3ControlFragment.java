package com.jerry.mp3player;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by test on 12/5/2015.
 */
public class MP3ControlFragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        return inflater.inflate(R.layout.fragment_mp3_control, container, false);
    }


    protected void onActivityCreated(){
        this.onActivityCreated();
    }
}

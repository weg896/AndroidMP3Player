package com.jerry.mp3player;

import android.app.Fragment;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

/**
 * Created by test on 12/5/2015.
 */
public class MP3PlayListFragment extends Fragment {

    private static final String TAG = "MP3_PLAYLIST_FRAGMENT";


    private View playlistView = null;
    private MP3Service mp3Service = null;

    // implement view component here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        playlistView = inflater.inflate(R.layout.fragment_mp3_playlist, container, false);

        ListView listview = (ListView)playlistView.findViewById(R.id.playlist);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getActivity(),android.R.layout.simple_expandable_list_item_1,MP3MusicFileReader.readSDCardMusic());

        listview.setAdapter(arrayAdapter);

        Log.d(TAG, "onCreateView");
        return playlistView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated");
    }


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
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void setMP3Service(MP3Service mp3Ser){
        mp3Service = mp3Ser;
    }

    public boolean isViewCreated(){
        return playlistView != null;
    }

    public boolean isServiceBound(){
        return mp3Service != null;
    }

}

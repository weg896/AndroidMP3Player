package com.jerry.mp3player;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.HashMap;

/**
 * Created by test on 12/5/2015.
 * This fragment is for showing music files and folder
 */
public class MP3PlayListFragment extends Fragment {

    private static final String TAG = "MP3_PLAYLIST_FRAGMENT";

    private View listViewContainer = null;
    private MP3Service mp3Service = null;
    private ListView listView = null;
    private SimpleAdapter simpleAdapter = null;
    private MP3MusicFileReader mp3MusicFileReader = null;


    public void setMP3MusicFileReader(MP3MusicFileReader mp3MusicFileReader){
        this.mp3MusicFileReader = mp3MusicFileReader;
    }

    // implement view component here
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        listViewContainer = inflater.inflate(R.layout.fragment_playlist_mp3, container, false);

        listView = (ListView)listViewContainer.findViewById(R.id.frag_play_playlist);
        String tempCurrentDir = mp3MusicFileReader.getCurrentDir();
        simpleAdapter = new SimpleAdapter(this.getActivity(),
                mp3MusicFileReader.getDir(tempCurrentDir),
                R.layout.music_item_mp3,
                MP3MusicFileReader.musicListHashMapStr,
                new int[]{R.id.musi_item_name,R.id.musi_item_type,R.id.musi_item_path,R.id.musi_item_icon});

        listView.setAdapter(simpleAdapter);

        Log.d(TAG, "onCreateView");
        return listViewContainer;
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
        setViewItemClickListener();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    public void setMP3Service(MP3Service mp3Ser){
        mp3Service = mp3Ser;
        setViewItemClickListener();
    }

    public void setViewItemClickListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // musicListHashMapStr = {"name","type","path","icon"};
                HashMap<String, Object> tempHashMap = (HashMap<String, Object>)parent.getItemAtPosition(position);
                int tempType = (int)tempHashMap.get(MP3MusicFileReader.musicListHashMapStr[1]);
                switch(tempType) {
                    case R.integer.PARENT_PATH:
                    case R.integer.FOLDER:
                        // if user click a folder icon,
                        // go into this folder and show it files and subfolders
                        simpleAdapter = new SimpleAdapter(MP3PlayListFragment.this.getActivity(),
                                mp3MusicFileReader.getDir((String) tempHashMap.get(MP3MusicFileReader.musicListHashMapStr[2])),
                                R.layout.music_item_mp3,
                                MP3MusicFileReader.musicListHashMapStr,
                                new int[]{R.id.musi_item_name, R.id.musi_item_type, R.id.musi_item_path, R.id.musi_item_icon});
                        listView.setAdapter(simpleAdapter);
                        break;
                    case R.integer.MUSIC_FILE:
                        mp3Service.musicPlayThis((String) tempHashMap.get(MP3MusicFileReader.musicListHashMapStr[2]),
                                true, (String)tempHashMap.get(MP3MusicFileReader.musicListHashMapStr[0]));
                        break;
                }
            }
        });
    }

    public boolean isViewCreated(){
        return listViewContainer != null;
    }

    public boolean isServiceBound(){
        return mp3Service != null;
    }

}

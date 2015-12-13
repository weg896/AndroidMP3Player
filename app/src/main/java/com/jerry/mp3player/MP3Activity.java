package com.jerry.mp3player;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MP3Activity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener{

    private static final String TAG = "MP3_ACTIVITY";

    private MP3ControlFragment controlFragment;
    private MP3PlayListFragment playlistFragment;

    private MP3Service mp3Service = null;
    private String sampleMP3URL = "/sdcard/gate.ogg";

    private Handler mHandler = new Handler();
    private boolean mFlipped = false;

    private Handler servicePreparedListener = new Handler();

    private MP3ConfigFragment configFragment;

    // connect to the service
    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // get service
            mp3Service = ((MP3Service.MusicBinder)service).getService();
            mp3Service.musicPlayThis(sampleMP3URL,false);
            Log.d(TAG, "onServiceConnected called");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mp3Service = null;
            Log.d(TAG,"onServiceDisconnected called");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mp3);

        configFragment = (MP3ConfigFragment)getFragmentManager().findFragmentByTag("config_fragment");
        controlFragment = (MP3ControlFragment)getFragmentManager().findFragmentByTag("control_fragment");
        playlistFragment = (MP3PlayListFragment)getFragmentManager().findFragmentByTag("playlist_fragment");


        if(controlFragment == null) {
            controlFragment = new MP3ControlFragment();
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, controlFragment, "control_fragment")
                    .commit();
        }

        if(playlistFragment == null){
            playlistFragment = new MP3PlayListFragment();
        }

        servicePreparedListener.postDelayed(servicePreparedRunnable,20);

        if(configFragment == null){
            configFragment = new MP3ConfigFragment();
            getFragmentManager().beginTransaction().add(configFragment, "config_fragment");

            Log.d(TAG, "onCreate--before bind");
            Intent intent = new Intent(getApplicationContext(),MP3Service.class);
            bindService(intent, musicConnection, BIND_AUTO_CREATE);
            Log.d(TAG, "onCreate--after bind");
        }

        mFlipped = (getFragmentManager().getBackStackEntryCount() > 0);
        getFragmentManager().addOnBackStackChangedListener(this);


    }


    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onDestroy() {
        unbindService(musicConnection);
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }


    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);

        MenuItem item=menu.add(Menu.NONE, R.id.action_flip, Menu.NONE,
                mFlipped
                        ?R.string.action_playlist
                        :R.string.action_controller);
        item.setIcon(mFlipped
                ? R.drawable.ic_action_photo
                : R.drawable.ic_action_info);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.action_flip:
                flipCard();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackStackChanged(){
        mFlipped = (getFragmentManager().getBackStackEntryCount() >0);
        Log.d("CARD_", "onBackStackChanged()");
        invalidateOptionsMenu();
    }

    private void flipCard(){
        if(mFlipped){
            getFragmentManager().popBackStack();
            return;
        }

        mFlipped = true;

        getFragmentManager()
                .beginTransaction()
                .setCustomAnimations(
                        R.animator.card_flip_right_in, R.animator.card_flip_right_out,
                        R.animator.card_flip_left_in, R.animator.card_flip_left_out)
                .replace(R.id.fragment_container, playlistFragment,"playlist_fragment")
                .addToBackStack(null)
                .commit();
        mHandler.post(new Runnable() {
            public void run() {
                invalidateOptionsMenu();
            }
        });
    }

    private Runnable servicePreparedRunnable = new Runnable(){
        public void run(){
            if(mp3Service != null) {
                if(controlFragment != null && controlFragment.isViewCreated() && !controlFragment.isServiceBound()) {
                    controlFragment.setMP3Service(mp3Service);
                    Log.d(TAG,"control fragment set service");
                }
                if(playlistFragment != null && playlistFragment.isViewCreated() && !playlistFragment.isServiceBound()){
                    playlistFragment.setMP3Service(mp3Service);
                    Log.d(TAG, "playlist fragment set service");
                }
            }
            servicePreparedListener.postDelayed(this, 20);
        }
    };
}

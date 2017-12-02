package com.example.yahia.myapplication;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.MediaExtractor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.InputStream;

public class Music extends AppCompatActivity {

    Messenger mMessenger = null;
    boolean mBound;
    Server server;

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName playMusic, IBinder service) {
            mMessenger = new Messenger(service);
            mBound = true;
        }

        public void onServiceDisconnected(ComponentName playMusic) {
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mMessenger = null;
            mBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        Intent calledIntent = getIntent();
        String songName = calledIntent.getStringExtra("songName");
        SeekBar seekbar = (SeekBar) findViewById(R.id.seekbar);
        int songId = calledIntent.getIntExtra("songId", 0);
        TextView tv_song = (TextView)findViewById(R.id.tv_song);
        tv_song.setText(songName);

        Intent intent = new Intent(this, playMusic.class);
        intent.putExtra("songId", songId);
        startService(intent);
        InputStream inputStream = getResources().openRawResource(songId);
        server = new Server(inputStream);
        final AssetFileDescriptor afd=getResources().openRawResourceFd(songId);
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        int duration = Integer.parseInt(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        seekbar.setMax(duration/1000);
        TextView tv_artist = (TextView) findViewById(R.id.tv_artist);
        tv_artist.setText(mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));


    }
    public void seekBack(View v){
        Message msg = new Message();
        msg.obj = "seekTo";
        msg.arg1 = -5000;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
    public void seekForward(View v){
        Message msg = new Message();
        msg.obj = "seekTo";
        msg.arg1 = 5000;
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
    public void togglePause(View v){
        Message msg = new Message();
        msg.obj = "pause";
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, playMusic.class), mConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mBound) {
            unbindService(mConnection);
            mBound = false;
        }


    }

    public void pause(View v){
        Message msg = new Message();
        msg.obj = "pause";
        try {
            mMessenger.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}

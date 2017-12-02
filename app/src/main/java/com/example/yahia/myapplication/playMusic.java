package com.example.yahia.myapplication;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.rtp.AudioCodec;
import android.net.rtp.AudioGroup;
import android.net.rtp.AudioStream;
import android.net.rtp.RtpStream;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.StrictMode;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class playMusic extends Service {
    MediaPlayer mediaPlayer;


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent,  int flags, int startId) {
        int songId = intent.getIntExtra("songId",0);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), songId);

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
            }
        });
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.start();
            }
        });
        t.start();

        return START_STICKY;
    }
    class IncomingHandler extends Handler {
        public void handleMessage(Message msg){
            String data = (String)msg.obj;
            switch(data){
                case "pause":
                    if(mediaPlayer.isPlaying())
                        mediaPlayer.pause();

                    else {
                        int length = mediaPlayer.getCurrentPosition();
                        mediaPlayer.seekTo(length);
                        mediaPlayer.start();
                    }
                    break;
                case "stop":mediaPlayer.stop();break;
                case "seekTo":int position = msg.arg1;
                    mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+position);
                    break;
            }
        }

    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    @Override
        public IBinder onBind (Intent intent){
            return mMessenger.getBinder();
        }

}

package com.example.yahia.myapplication;

import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import static java.lang.System.out;

/**
 * Created by yahia on 01/12/17.
 */

public class Client extends AsyncTask<Void, Void, Void> {

    String dstAddress;
    int dstPort;
    String response = "";
    TextView clientResponse;
    MainActivity1 mainActivity;

    Client(MainActivity1 mainActivity,String addr, int port,TextView clientResponse) {
        this.mainActivity = mainActivity;
        dstAddress = addr;
        dstPort = port;
    }

    @Override
    protected Void doInBackground(Void... arg0) {

        Socket socket = null;

        try {
            socket = new Socket(dstAddress, dstPort);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            File convertedFile = File.createTempFile("convertedFile", ".dat", mainActivity.getDir("filez", 0));
            FileOutputStream out = new FileOutputStream(convertedFile);
            byte buffer[] = new byte[16384];
            int length = 0;
            while ( (length = socket.getInputStream().read(buffer)) != -1 )
            {
                out.write(buffer,0, length);
            }
            System.out.println(new String(buffer,"UTF-8"));

            //stream.read(buffer);

            out.close();
            final MediaPlayer mp = new MediaPlayer();

            FileInputStream fis = new FileInputStream(convertedFile);
            mp.setDataSource(fis.getFD());
            fis.close();

            mp.prepare();
            mp.start();
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.release();
                }
            });
            socket.close();



        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "UnknownHostException: " + e.toString();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            response = "IOException: " + e.toString();
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
    }

}
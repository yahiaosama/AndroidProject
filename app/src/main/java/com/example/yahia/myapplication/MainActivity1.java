package com.example.yahia.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Messenger;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity1 extends AppCompatActivity {

    Server server;
    TextView infoip, msg,clientResponse;
    Client client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        server.onDestroy();
    }
    public void Server(View v){
        Intent intent = new Intent(this,allMusic.class);
        startActivity(intent);


    }
    public void Client(View v){
        client = new Client(this,"192.168.2.118",8080,clientResponse);
        client.execute();
    }

}

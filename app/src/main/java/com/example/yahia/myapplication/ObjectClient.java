package com.example.yahia.myapplication;

import java.net.InetAddress;

/**
 * Created by yahia on 02/12/17.
 */

public class ObjectClient {
    InetAddress ip;
    int port = 8080;
    public ObjectClient(InetAddress ip){
        this.ip = ip;
    }
}

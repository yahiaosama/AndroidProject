package com.example.yahia.myapplication;

/**
 * Created by yahia on 01/12/17.
 */


import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

public class Server {
    ServerSocket serverSocket;
    String message = "";
    InputStream input;
    ArrayList<ObjectClient>clients = new ArrayList<ObjectClient>();
    int id;
    static final int socketServerPORT = 8080;

    public Server(InputStream input) {
        this.input = input;
        Thread socketServerThread = new Thread(new SocketServerThread());
        socketServerThread.start();
    }

    public int getPort() {
        return socketServerPORT;
    }

    public void onDestroy() {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private class SocketServerThread extends Thread {


        @Override
        public void run() {
            try {

                serverSocket = new ServerSocket(socketServerPORT);

                while (true) {
                    Socket socket = serverSocket.accept();
                    System.out.println("connected");
                    DataOutputStream out = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
                    byte buffer[] = new byte[16384];
                    int length = 0;
                    while ( (length = input.read(buffer)) != -1 )
                    {
                        out.write(buffer,0, length);
                    }
                    System.out.println(buffer);

                    SocketServerReplyThread socketServerReplyThread = new SocketServerReplyThread(
                            socket, buffer);
                    socketServerReplyThread.run();

                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public class SocketServerReplyThread extends Thread {

        private Socket hostThreadSocket;
        byte[] buffer;
        String action;
        int position;

        SocketServerReplyThread(Socket socket, byte[] buffer) {
            hostThreadSocket = socket;
            this.buffer = buffer;
        }

        @Override
        public void run() {
                try {
                    OutputStream outputStream = hostThreadSocket.getOutputStream();
                    DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                    dataOutputStream.write(buffer);
                    hostThreadSocket.close();

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    message += "Something wrong! " + e.toString() + "\n";
                }
            }


        }


    public String getIpAddress() {
        String ip = "";
        try {
            Enumeration<NetworkInterface> enumNetworkInterfaces = NetworkInterface
                    .getNetworkInterfaces();
            while (enumNetworkInterfaces.hasMoreElements()) {
                NetworkInterface networkInterface = enumNetworkInterfaces
                        .nextElement();
                Enumeration<InetAddress> enumInetAddress = networkInterface
                        .getInetAddresses();
                while (enumInetAddress.hasMoreElements()) {
                    InetAddress inetAddress = enumInetAddress
                            .nextElement();

                    if (inetAddress.isSiteLocalAddress()) {
                        ip += "Server running at : "
                                + inetAddress.getHostAddress();
                    }
                }
            }

        } catch (SocketException e) {
            e.printStackTrace();
            ip += "Something Wrong! " + e.toString() + "\n";
        }
        return ip;
    }
}
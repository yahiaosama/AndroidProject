package com.example.yahia.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class allMusic extends AppCompatActivity {
    ArrayList<Song> songs;
    ListView listView;
    CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_music);
        listView=(ListView)findViewById(R.id.list);
        songs= new ArrayList<>();
        Field[] fields=R.raw.class.getFields();
        for(int i =0;i<fields.length;i++){
            try {
                Song song = new Song(fields[i].getName(),"Eminem",fields[i].getInt(fields[i]));
                songs.add(song);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        InputStream in = null;
        try {
            in = getResources().openRawResource(fields[0].getInt(fields[0]));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        System.out.println(in);

        adapter = new CustomAdapter(songs,getApplicationContext());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Song song= songs.get(position);

                Intent intent = new Intent(getApplicationContext(),Music.class);
                intent.putExtra("songId",song.id);
                intent.putExtra("songName",song.name);
                startActivity(intent);


            }
        });
    }


    }


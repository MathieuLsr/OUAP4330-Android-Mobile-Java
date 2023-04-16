package com.example.projetp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static MainActivity INSTANCE ;

    private DatabaseHelper databaseHelper ;

    private ListView lv;
    private Button buttonToAdd;
    private Switch swMusic;
    private Intent intent_music_background;

    static boolean isMusic = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        INSTANCE = this ;

        setContentView(R.layout.activity_main);

        this.databaseHelper = new DatabaseHelper(this) ;
        databaseHelper.open();

        Cursor cursor = databaseHelper.getAllRDV() ;
        AdapterCursor adapterCursor = new AdapterCursor(cursor) ;

        List<RdvDetails> list = adapterCursor.getList() ;

        lv = (ListView) findViewById(R.id.listView);


        RdvAdapter rdvAdapter = new RdvAdapter(
                this,
                R.layout.custom_list,
                list) ;

        lv.setAdapter(rdvAdapter);




        //...................AJOUTER......................

        buttonToAdd = (Button) findViewById(R.id.buttonAdd);

        buttonToAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddManager.class) ;
                startActivity(intent);
            }
        });


        //...................MUSIQUE......................

        intent_music_background = new Intent(MainActivity.this, BackgroundSoundService.class);
        swMusic = (Switch) findViewById(R.id.switchMusic) ;

        if (isMusic) swMusic.setChecked(true);

        swMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(swMusic.isChecked()) {
                    startService(intent_music_background);
                    isMusic = true;
                }
                else {
                    stopService(intent_music_background) ;
                    isMusic = false;
                }
            }
        });

    }

    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

}
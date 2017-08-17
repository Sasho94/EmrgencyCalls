package com.example.user.emergencycall;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;


import com.example.user.emergencycall.Models.*;
import com.example.user.emergencycall.Services.BackgroundService;
import com.example.user.emergencycall.databinding.ActivityStartStopAppBinding;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public class StartStopApplicationActivity extends AppCompatActivity {
    public static final String LOAD = "loads";
   // public static final String mBroadcastDataFloatAction = "com.truiton.broadcast.float";
    double data = 0d;
    private static Switch switchKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityStartStopAppBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_start_stop_app);

        final Button buttonSave = (Button) findViewById(R.id.btnSaveSettings);
        final EditText ETalarm1 = (EditText) findViewById(R.id.txtAlarm1);
        final EditText ETalarm2 = (EditText) findViewById(R.id.txtAlarm2);
        final EditText ETrest = (EditText) findViewById(R.id.txtResting);

        // Binding values for alarms with default value 1
        SharedPreferences pref = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
        String alarm1 = pref.getString("alarm1", "1");
        String alarm2 = pref.getString("alarm2", "1");
        String rest = pref.getString("rest", "1");

        Settings settings = new Settings(alarm1, alarm2, rest);

        binding.setSettings(settings);

        //Save values for the alarms
        buttonSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                String alarm1 = ETalarm1.getText().toString();
                String alarm2 = ETalarm2.getText().toString();
                String rest = ETrest.getText().toString();

                SharedPreferences pref = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("alarm1", alarm1);
                editor.putString("alarm2", alarm2);
                editor.putString("rest", rest);

                editor.commit();


            }


        });

        switchKey = (Switch) findViewById(R.id.switchKey);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            if(item.getItemId()==R.id.menuSettings) {
                Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(getApplicationContext(), UpdateInfoActivity.class);
                startActivity(intent);
            }
        }catch (Exception e){
             e.printStackTrace();
        }
        return  true;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}



















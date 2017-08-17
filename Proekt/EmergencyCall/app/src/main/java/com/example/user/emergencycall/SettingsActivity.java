package com.example.user.emergencycall;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.user.emergencycall.Models.ConfigurationSettings;

public class SettingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final String LOAD_SETTINGS = "loadSettings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        final Button buttonSave = (Button) findViewById(R.id.btnSaveSettings);
        final EditText ETIP = (EditText) findViewById(R.id.txtIP);

        SharedPreferences pref = getSharedPreferences(LOAD_SETTINGS, Context.MODE_PRIVATE);
        final String IPpref = pref.getString("IP", "");
        final int position = pref.getInt("position", 0);
        ETIP.setText(IPpref);

        ConfigurationSettings settings = new ConfigurationSettings(IPpref);
        spinner.setOnItemSelectedListener(this);
        try {

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item,
                    settings.sensitivityTypes
            );

            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(dataAdapter);
            spinner.setSelection(position);
            buttonSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String IP = ETIP.getText().toString();
                    int position = spinner.getSelectedItemPosition();
                    SharedPreferences pref = getSharedPreferences(LOAD_SETTINGS, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("IP", IP);
                    editor.putInt("position", position);
                    editor.commit();
                    finish();

                }
            });

        } catch (Exception e) {
            Log.d("ERROR ", e.toString());
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getSelectedItem().toString();
        Log.d("SELECTED", item);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

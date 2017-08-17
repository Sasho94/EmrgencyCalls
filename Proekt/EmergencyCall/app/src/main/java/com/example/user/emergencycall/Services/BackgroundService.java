package com.example.user.emergencycall.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.example.user.emergencycall.Alarm1;
import com.example.user.emergencycall.Fragments.GraphFragment;

public class BackgroundService extends Service implements SensorEventListener {
    private SensorManager sensorManager;
    public static final String LOAD_SETTINGS = "loadSettings";
    public static final String LOAD = "loads";
    private int hitValue=0;
    private double acceleration =0;
    private long startTime = 0L;
    private float x;
    private float y;
    private float z;
    private int rest = 0;
    private int alarm1 = 0;
    private int alarm2 = 0;
    private boolean isUp = false;
    private boolean hasFallen = false;

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION), SensorManager.SENSOR_DELAY_FASTEST);

        }catch (Exception e){
            e.printStackTrace();
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // Wont be called as service is not bound
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        //Check when the sensor has changed
        try {
            if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
                float[] values = event.values;
                x = values[0];
                y = values[1];
                z = values[2];
                Intent broadcastIntent = new Intent();
                broadcastIntent.setAction(GraphFragment.mBroadcastFloatAction);
                acceleration = Math.sqrt(x * x + y * y + z * z);
                broadcastIntent.putExtra("Data", (float) acceleration);
                sendBroadcast(broadcastIntent);

                /*
                 * Check if the acceleration is over 15
                 * if it is it sets the alarms
                 */
                if (acceleration > 20) {
                    SharedPreferences pref = getSharedPreferences(LOAD_SETTINGS, Context.MODE_PRIVATE);
                    SharedPreferences prefAlarms = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
                    final int position = pref.getInt("position", 0);
                    rest = Integer.parseInt(prefAlarms.getString("rest", "0"));
                    alarm1 = Integer.parseInt(prefAlarms.getString("alarm1", "0"));
                    alarm2 = Integer.parseInt(prefAlarms.getString("alarm2", "0"));
                    switch (position) {
                        case 0:
                            hitValue = 25;
                            break;
                        case 1:
                            hitValue = 30;
                            break;
                        case 2:
                            hitValue = 35;
                            break;
                    }

                }
                //Sets the timer
                if (hitValue <= acceleration && hitValue != 0) {
                    startTime = SystemClock.uptimeMillis();
                    hasFallen = true;
                    isUp=false;
                }
                //Checks if the minutes have passed, and starts 5 secs later
                if(hasFallen) {
                    double timePassed = SystemClock.uptimeMillis() - startTime;
                    if (rest * 1000 > timePassed) {
                    /*
                     * Checks if the acceleration is more than 2 (the phone is moving)
                     * sets hitValue to 0
                     */
                    Log.d("TIMEPASSED", String.valueOf(timePassed));
                        if (acceleration > 2 && timePassed > 5000) {
                            isUp = true;
                            Log.d("x*x+y*y+z*z:", "" + Math.sqrt(x * x + y * y + z * z));
                        }
                    } else {
                        if (!isUp) {
                            hasFallen = false;
                            Intent dialogIntent = new Intent(this, Alarm1.class);
                            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(dialogIntent);
                        }
                    }
                }


            }

        }catch (Exception e){
            Log.d("ERROR: ", e.toString());
        }
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}


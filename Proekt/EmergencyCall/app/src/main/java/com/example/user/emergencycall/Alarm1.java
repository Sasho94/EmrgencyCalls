package com.example.user.emergencycall;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.akhgupta.easylocation.EasyLocationAppCompatActivity;
import com.akhgupta.easylocation.EasyLocationRequest;
import com.akhgupta.easylocation.EasyLocationRequestBuilder;
import com.example.user.emergencycall.Services.URLPaths;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Alarm1 extends EasyLocationAppCompatActivity {
    public static final String LOAD = "loads";
    public static final String LOAD_SETTINGS = "loadSettings";
    private double latitude;
    private double longditude;
    MediaPlayer mediaPlayer;
    int counter = 0;
    Button btnYes;
    Button btnNo;
    Vibrator v;
    PulsatorLayout pulsator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        try {
            pulsator = (PulsatorLayout) findViewById(R.id.pulsator);
            btnYes = (Button) findViewById(R.id.btnYes);
            btnNo = (Button) findViewById(R.id.btnNo);
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                    + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                    +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                    +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
            LocationRequest locationRequest = new LocationRequest()
                    .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                    .setInterval(1000)
                    .setFastestInterval(1000);
            EasyLocationRequest easyLocationRequest = new EasyLocationRequestBuilder()
                    .setLocationRequest(locationRequest)
                    .setFallBackToLastLocationTime(3000)
                    .build();
            requestSingleLocationFix(easyLocationRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            super.onDestroy();
        }

    }

    public User getUser(SharedPreferences pref) {
        String userFirstName = pref.getString("userFirstName", "");
        String userFamilyName = pref.getString("userFamilyName", "");
        String userAddress = pref.getString("userAddress", "");
        String userWeight = pref.getString("userWeight", "");
        String userHeight = pref.getString("userHeight", "");
        String userPhone = pref.getString("userPhone", "");

        User user = new User(userFirstName, userFamilyName, userAddress, userWeight, userHeight, userPhone);

        return user;
    }

    public byte[] getByteArray(SharedPreferences pref) {
        String picturePath = pref.getString("picturePath", "@drawable/human");
        Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            return stream.toByteArray();
        }
        return null;
    }

    @Override
    public void onLocationPermissionGranted() {
    }

    @Override
    public void onLocationPermissionDenied() {
    }

    @Override
    public void onLocationReceived(Location location) {
        try {
            latitude = location.getLatitude();
            longditude = location.getLongitude();
            SharedPreferences prefAlarms = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
            final int alarm1 = Integer.parseInt(prefAlarms.getString("alarm1", "0"));
            final int alarm2 = Integer.parseInt(prefAlarms.getString("alarm2", "0"));

            pulsator.start();
            mediaPlayer.start();
            mediaPlayer.setLooping(true);
            v.vibrate(alarm1 * 1000);
            mediaPlayer.setVolume(25, 25);

            final Toast toast = Toast.makeText(this, "Аларма 1", Toast.LENGTH_SHORT);
            toast.show();
            final Toast toastAlarm2 = Toast.makeText(this, "Аларма 2", Toast.LENGTH_SHORT);
            final Handler handler = new Handler();
            SharedPreferences pref = getSharedPreferences(LOAD_SETTINGS, Context.MODE_PRIVATE);
            final String IPpref = pref.getString("IP", "");

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://" + IPpref)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            final User user = getUser(prefAlarms);
            user.setLatitude(latitude);
            user.setLongditude(longditude);
            final URLPaths api = retrofit.create(URLPaths.class);

            File partFile = new File(prefAlarms.getString("picturePath", "@drawable/human"));
            final RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), partFile);
            final MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_file", partFile.getName(), requestFile);


           final Runnable r = new Runnable() {
                public void run() {
                    if (alarm1 > counter) {
                        counter++;
                    } else {
                        toastAlarm2.show();
                        mediaPlayer.setVolume(49, 49);
                        if (alarm2 + alarm1 > counter) {
                            counter++;
                        } else {
                            mediaPlayer.stop();
                            Call<User> call = api.sendUser(user);
                            call.enqueue(new Callback<User>() {
                                @Override
                                public void onResponse(Call<User> call, Response<User> response) {
                                    Log.d("SUCCESS", "SUCCESS");
                                    Call<ResponseBody> call1 = api.upload(requestFile, body);
                                    call1.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            Log.d("SUCCESS", "SUCCESSPHOTO");
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d("FAIL", "FAILPHOTO");
                                            t.printStackTrace();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure(Call<User> call, Throwable t) {
                                    Log.d("FAIL", "FAIL");
                                    t.printStackTrace();
                                    Call<ResponseBody> call1 = api.upload(requestFile, body);
                                    call1.enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                            Log.d("SUCCESS", "SUCCESSPHOTO");
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            Log.d("FAIL", "FAILPHOTO");
                                            t.printStackTrace();
                                        }
                                    });
                                }
                            });

                            finish();
                            return;

                        }
                    }
                    handler.postDelayed(this, 1000);
                }
            };
            handler.post(r);

            btnYes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    v.cancel();
                    mediaPlayer.stop();
                    handler.removeCallbacks(r);
                    //TODO Stop thread
                    finish();
                }
            });

            btnNo.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    handler.removeCallbacks(r);
                    Call<User> call = api.sendUser(user);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            Log.d("SUCCESS", "SUCCESS");
                            Call<ResponseBody> call1 = api.upload(requestFile, body);
                            call1.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d("SUCCESS", "SUCCESSPHOTO");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("FAIL", "FAILPHOTO");
                                    t.printStackTrace();
                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.d("FAIL", "FAIL");
                            t.printStackTrace();
                            Call<ResponseBody> call1 = api.upload(requestFile, body);
                            call1.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    Log.d("SUCCESS", "SUCCESSPHOTO");
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    Log.d("FAIL", "FAILPHOTO");
                                    t.printStackTrace();
                                }
                            });
                        }
                    });
                    finish();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationProviderEnabled() {
    }

    @Override
    public void onLocationProviderDisabled() {
    }
}

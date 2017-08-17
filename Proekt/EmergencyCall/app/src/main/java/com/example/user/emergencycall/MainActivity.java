package com.example.user.emergencycall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    public static final String LOAD = "loads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        final SharedPreferences pref = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
        ImageView buttonLoadImage = (ImageView) findViewById(R.id.imageView);
        buttonLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                if(pref.getString("userFirstName", null)!=null) {
                    Intent intent = new Intent(getApplicationContext(), UpdateInfoActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), SaveInfoActivity.class);
                    startActivity(intent);
                }
            }
        });

    }

}

package com.example.user.emergencycall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.user.emergencycall.Models.User;
import com.example.user.emergencycall.databinding.ActivityUpdateInfoBinding;

public class UpdateInfoActivity extends AppCompatActivity {
    public static final String LOAD = "loads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_update_info);

            ActivityUpdateInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_update_info);
            SharedPreferences pref = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
            String userFirstName = pref.getString("userFirstName", "");
            String userFamilyName = pref.getString("userFamilyName", "");
            String userAddress = pref.getString("userAddress", "");
            String userWeight = pref.getString("userWeight", "");
            String userHeight = pref.getString("userHeight", "");
            String userPhone = pref.getString("userPhone", "");
            User user = new User(userFirstName, userFamilyName, userAddress, userWeight, userHeight, userPhone);
            binding.setUser(user);

            ImageView imgLoadImage = (ImageView) findViewById(R.id.imgView);
            if (pref.getString("picturePath", null) != null) {
                imgLoadImage.setImageBitmap(BitmapFactory.decodeFile(pref.getString("picturePath", "")));
            }

            Button buttonUpdate = (Button) findViewById(R.id.btnUpdate);
            buttonUpdate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), SaveInfoActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });
            Button buttonNext = (Button) findViewById(R.id.btnNext);
            buttonNext.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    try {
                        Intent intent = new Intent(getApplicationContext(), StartStopApplicationActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

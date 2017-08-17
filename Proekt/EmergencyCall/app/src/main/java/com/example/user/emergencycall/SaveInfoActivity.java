package com.example.user.emergencycall;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.user.emergencycall.Models.User;
import com.example.user.emergencycall.databinding.ActivitySaveInfoBinding;


public class SaveInfoActivity extends AppCompatActivity {
    private static int RESULT_LOAD_IMAGE = 1;
    public static final String LOAD = "loads";
    public static String picturePath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            ActivitySaveInfoBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_save_info);
            final Button buttonSave = (Button) findViewById(R.id.btnSave);
            final EditText ETname = (EditText) findViewById(R.id.txtName);
            final EditText ETfamilyName = (EditText) findViewById(R.id.txtFamilyName);
            final EditText ETadddress = (EditText) findViewById(R.id.txtAddress);
            final EditText ETweight = (EditText) findViewById(R.id.txtWeight);
            final EditText ETheight = (EditText) findViewById(R.id.txtHeight);
            final EditText ETphone = (EditText) findViewById(R.id.txtPhone);

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
            imgLoadImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
            });
            buttonSave.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    String firstName = ETname.getText().toString();
                    String familyName = ETfamilyName.getText().toString();
                    String address = ETadddress.getText().toString();
                    String weight = ETweight.getText().toString();
                    String height = ETheight.getText().toString();
                    String phone = ETphone.getText().toString();

                    SharedPreferences pref = getSharedPreferences(LOAD, Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("userFirstName", firstName);
                    editor.putString("userFamilyName", familyName);
                    editor.putString("userAddress", address);
                    editor.putString("userWeight", weight);
                    editor.putString("userHeight", height);
                    editor.putString("userPhone", phone);
                    if (picturePath != null) {
                        editor.putString("picturePath", picturePath);
                    }
                    editor.commit();

                    Intent intent = new Intent(getApplicationContext(), UpdateInfoActivity.class);
                    startActivity(intent);
                    finish();

                }


            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
                Uri selectedImage = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                picturePath = cursor.getString(columnIndex);
                cursor.close();
                ImageView imageView = (ImageView) findViewById(R.id.imgView);
                imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


package com.example.user.emergencycall.Receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by User on 17.4.2017 г..
 */

public class BackgroundBroadcastReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        CharSequence intentData = intent.getCharSequenceExtra("message");
    }
}



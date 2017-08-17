package com.example.user.emergencycall.Models;

import android.databinding.ObservableArrayList;

/**
 * Created by User on 22.5.2017 г..
 */

public class ConfigurationSettings {
    private String IP;
    private int position;

    public ObservableArrayList<String> sensitivityTypes = new ObservableArrayList<>();

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public ConfigurationSettings(String IP){
        this.IP=IP;
        sensitivityTypes.add("Висока");
        sensitivityTypes.add("Нормална");
        sensitivityTypes.add("Ниска");

    }

}

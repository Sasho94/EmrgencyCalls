package com.example.user.emergencycall.Models;

/**
 * Created by User on 14.4.2017 г..
 */

public class Settings {
    private String alarm1;
    private String alarm2;
    private String resting;

    public Settings(String alarm1, String alarm2,String resting){
        this.alarm1=alarm1;
        this.alarm2= alarm2;
        this.resting=resting;
    }

    public String getAlarm1() {
        return alarm1;
    }

    public String getAlarm2() {
        return alarm2;
    }

    public String getResting() {
        return resting;
    }
}

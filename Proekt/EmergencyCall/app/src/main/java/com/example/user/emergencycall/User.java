package com.example.user.emergencycall;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable{
    private String firstName;
    private String familyName;
    private String address;
    private String weight;
    private String height;
    private String phone;

    private double latitude;
    private double longditude;

    public User(String firstName, String familyName, String address, String weight, String height, String phone) {
        this.firstName = firstName;
        this.familyName = familyName;
        this.address =address;
        this.height = height;
        this.weight = weight;
        this.phone = phone;
    }
    @Bindable
    public String getFirstName() {
        return firstName;
    }
    @Bindable
    public String getFamilyName() {
        return familyName;
    }
    @Bindable
    public String getAddress() {
        return address;
    }
    @Bindable
    public String getWeight() {
        return weight;
    }
    @Bindable
    public String getHeight() {
        return height;
    }
    @Bindable
    public String getPhone() {
        return phone;
    }
    @Bindable
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
    @Bindable
    public double getLongditude() {
        return longditude;
    }

    public void setLongditude(double longditude) {
        this.longditude = longditude;
    }
}

package com.example.user.emergencycall.Models;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class User extends BaseObservable{
    private String firstName;
    private String familyName;
    private String address;
    private String weight;
    private String height;
    private String phone;

    public User(String firstName, String familyName,String address, String weight,String height, String phone) {
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
}

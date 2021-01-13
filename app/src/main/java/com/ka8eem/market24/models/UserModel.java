package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UserModel implements Serializable {


    @SerializedName("user_id")
    private int userId;

    @SerializedName("exist")
    private String exist;

    @SerializedName("name")
    private String userName;

    @SerializedName("phone")
    private String phone;

    @SerializedName("address")
    private String address;

    @SerializedName("SSN")
    private String SSN;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @SerializedName("acc")
    private String email;

    @SerializedName("pass")
    private String password;

    @SerializedName("img_profile")
    private String image;

    public UserModel() {

    }

    public UserModel(String pass, int user_id , String userName, String phone, String address, String image)
    {
        this.userName = userName;
        this.phone = phone;
        this.address = address;
        this.image = image;
        this.userId = user_id;
        this.password = pass;
    }

    public UserModel(String userName, String phone, String address, String email, String password) {
        this.userName = userName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;

    }

    public void setImage(String image)
    {
        this.image = image;
    }

    public String getImage() {
        return this.image;
    }

    public int getUserId() {
        return userId;
    }

    public String getExist() {
        return exist;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getSSN() {
        return SSN;
    }

    public String getEmail() {
        return email;
    }
}

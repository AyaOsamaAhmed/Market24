package com.ka8eem.market24.models;

public class UserFirebaseModel {
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    private String user_id , username , Email , profile_image , ID_ADS;

    public String getID_ADS() {
        return ID_ADS;
    }

    public void setID_ADS(String ID_ADS) {
        this.ID_ADS = ID_ADS;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public UserFirebaseModel(){}

    public UserFirebaseModel( String user_id  , String Email , String username , String profile_image , String ID_ADS){

        this.Email = Email;
        this.user_id = user_id;
        this.username = username;
        this.profile_image = profile_image;
        this.ID_ADS = ID_ADS;


    }
    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

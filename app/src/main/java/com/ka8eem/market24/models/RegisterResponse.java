package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterResponse implements Serializable {


    @SerializedName("response")
    private String res;

    public String getRes() {
        return res;
    }
}

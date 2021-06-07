package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class UpdateProfileResponse implements Serializable {


    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    @SerializedName("result")
    private String result;

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}

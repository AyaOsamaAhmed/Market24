package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ForgetPasswordResponse implements Serializable {


    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private Boolean status;

    @SerializedName("result")
    private String result;

    public String getMessage() {
        return message;
    }

    public Boolean getStatus() {
        return status;
    }

    public String getResult() {
        return result;
    }
}

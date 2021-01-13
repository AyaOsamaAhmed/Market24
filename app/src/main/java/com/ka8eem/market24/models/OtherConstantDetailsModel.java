package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OtherConstantDetailsModel implements Serializable {

    @SerializedName("status")
    private String status;

    @SerializedName("deatails")
    private String details;

    public OtherConstantDetailsModel() {
    }

    public OtherConstantDetailsModel(String status, String details) {
        this.status = status;
        this.details = details;
    }

    public String getStatus() {
        return status;
    }

    public String getDetails() {
        return details;
    }
}

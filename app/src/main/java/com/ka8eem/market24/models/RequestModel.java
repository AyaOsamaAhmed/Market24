package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RequestModel implements Serializable {

    @SerializedName("ads_id")
    private String adsId;

    @SerializedName("bayer_id")
    private String bayerId;

    @SerializedName("salesman_id")
    private String salesManId;

    @SerializedName("price")
    private String price;

    public RequestModel()
    {

    }

    public RequestModel(String adsId, String bayerId, String salesManId, String price) {
        this.adsId = adsId;
        this.bayerId = bayerId;
        this.salesManId = salesManId;
        this.price = price;
    }

}

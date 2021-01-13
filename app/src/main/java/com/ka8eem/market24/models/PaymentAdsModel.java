package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentAdsModel implements Serializable {

    @SerializedName("ads_id")
    private String adsID;

    @SerializedName("name")
    private String adsName;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private String price;

    @SerializedName("waiting_time")
    private String waitingTime;

    @SerializedName("img_ads")
    private String adsImage;

    @SerializedName("link_ads")
    private String adsLink;

    public String getAdsID() {
        return adsID;
    }

    public String getAdsName() {
        return adsName;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

    public String getWaitingTime() {
        return waitingTime;
    }

    public String getAdsImage() {
        return adsImage;
    }

    public String getAdsLink() {
        return adsLink;
    }
}

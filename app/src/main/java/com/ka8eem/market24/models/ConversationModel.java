package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConversationModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("buyer_id")
    private int buyer_id;

    @SerializedName("seller_id")
    private int seller_id;

    @SerializedName("ads_id")
    private int ads_id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("last_message")
    private LastMessageModel last_message;

    @SerializedName("ad")
    private ProductModel ads_model;

    public ConversationModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getAds_id() {
        return ads_id;
    }

    public void setAds_id(int ads_id) {
        this.ads_id = ads_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getBuyer_id() { return buyer_id; }

    public void setBuyer_id(int buyer_id) {
        this.buyer_id = buyer_id;
    }

    public int getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(int seller_id) {
        this.seller_id = seller_id;
    }

    public LastMessageModel getLast_message() {
        return last_message;
    }

    public void setLast_message(LastMessageModel last_message) {
        this.last_message = last_message;
    }

    public ProductModel getAds_model() { return ads_model; }
}

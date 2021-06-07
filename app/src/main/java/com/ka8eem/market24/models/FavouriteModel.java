package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class FavouriteModel implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("user_id")
    private int user_id;

    @SerializedName("ads_id")
    private int ads_id;

    @SerializedName("created_at")
    private String created_at;

    @SerializedName("ad")
    private ProductModel ads;

    public FavouriteModel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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

    public ProductModel getAds() {
        return ads;
    }

    public void setAds(ProductModel ads) {
        this.ads = ads;
    }
}

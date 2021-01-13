package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SpecialInfoModel implements Serializable {
    public String getImg_path() {
        return img_path;
    }

    public String getAds_id() {
        return ads_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public SpecialInfoModel() {
    }

    public SpecialInfoModel(String img_path, String ads_id, String product_name) {
        this.img_path = img_path;
        this.ads_id = ads_id;
        this.product_name = product_name;
    }

    @SerializedName("img_path")
    private String img_path;

    @SerializedName("ads_id")
    private String ads_id;

    @SerializedName("product_name")
    private String product_name;
}

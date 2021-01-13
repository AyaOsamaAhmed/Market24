package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PannerModel implements Serializable {

    @SerializedName("img_id")
    private String imgId;

    @SerializedName("img_url")
    private String imgUrl;

    public PannerModel() {
    }

    public String getImgId() {
        return imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}

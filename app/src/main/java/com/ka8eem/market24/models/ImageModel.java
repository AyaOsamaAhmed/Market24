package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageModel implements Serializable {


    @SerializedName("img_id")
    private String imgId;

    @SerializedName("img_path")
    private String imgUrl;

    @SerializedName("ads_id")
    private String ads_id;

    @SerializedName("image_name")
    private String image_name;

    public ImageModel() {

    }

    public String getImgId() {
        return imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getAds_id() {
        return ads_id;
    }

    public void setAds_id(String ads_id) {
        this.ads_id = ads_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}

package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ImageModel implements Serializable {


    @SerializedName("img_id")
    private String imgId;

    @SerializedName("img_path")
    private String imgUrl;

    public ImageModel() {

    }

    public String getImgId() {
        return imgId;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}

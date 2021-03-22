package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PannerModel implements Serializable {

    @SerializedName("img_id")
    private String imgId;

    @SerializedName("img_url")
    private String imgUrl;

    @SerializedName("link")
    private String link;

    @SerializedName("show_img")
    private int show_img;


    public PannerModel() {
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

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getShow_img() {
        return show_img;
    }

    public void setShow_img(int show_img) {
        this.show_img = show_img;
    }
}

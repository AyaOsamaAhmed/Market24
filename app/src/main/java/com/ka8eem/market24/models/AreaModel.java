package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AreaModel implements Serializable {

    @SerializedName("area_id")
    private int areaID;

    @SerializedName("area_name")
    private String areaName;

    @SerializedName("area_img")
    private String areaImg;

    @SerializedName("area_name_E")
    private String areaNameEn;

    public AreaModel() {
    }

    public AreaModel(int areaID, String areaName, String areaImg, String areaNameEn) {
        this.areaID = areaID;
        this.areaName = areaName;
        this.areaImg = areaImg;
        this.areaNameEn = areaNameEn;
    }

    public int getAreaID() {
        return areaID;
    }

    public void setAreaID(int areaID) {
        this.areaID = areaID;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public String getAreaImg() {
        return areaImg;
    }

    public void setAreaImg(String areaImg) {
        this.areaImg = areaImg;
    }

    public String getAreaNameEn() {
        return areaNameEn;
    }

    public void setAreaNameEn(String areaNameEn) {
        this.areaNameEn = areaNameEn;
    }
}

package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubAreaModel implements Serializable {

    @SerializedName("Sub_area_id")
    private int subAreaID;

    @SerializedName("Sub_area_name")
    private String subAreaName;

    @SerializedName("Sub_area_img")
    private String subAreaImg;

    @SerializedName("Sub_area_name_E")
    private String subAreaNameEn;

    @SerializedName("area_id")
    private String areaId;

    public SubAreaModel() {
    }

    public SubAreaModel(int subAreaID, String subAreaName, String subAreaImg, String subAreaNameEn, String areaId) {
        this.subAreaID = subAreaID;
        this.subAreaName = subAreaName;
        this.subAreaImg = subAreaImg;
        this.subAreaNameEn = subAreaNameEn;
        this.areaId = areaId;
    }

    public int getSubAreaID() {
        return subAreaID;
    }

    public void setSubAreaID(int subAreaID) {
        this.subAreaID = subAreaID;
    }

    public String getSubAreaName() {
        return subAreaName;
    }

    public void setSubAreaName(String subAreaName) {
        this.subAreaName = subAreaName;
    }

    public String getSubAreaImg() {
        return subAreaImg;
    }

    public void setSubAreaImg(String subAreaImg) {
        this.subAreaImg = subAreaImg;
    }

    public String getSubAreaNameEn() {
        return subAreaNameEn;
    }

    public void setSubAreaNameEn(String subAreaNameEn) {
        this.subAreaNameEn = subAreaNameEn;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }
}

package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityModel implements Serializable {

    @SerializedName("area_id")
    private int cityID;

    @SerializedName("area_name")
    private String cityName;

    @SerializedName("area_img")
    private String urlImage;

    @SerializedName("area_name_E")
    private String areaNameEn;

    public CityModel() {
    }


    public CityModel(int id, String cityName) {
        this.cityID = id;
        this.cityName = cityName;
    }

    public String getAreaNameEn() {
        return areaNameEn;
    }

    public int getCityID() {
        return cityID;
    }

    public String getCityName() {
        return cityName;
    }

    public String getUrlImage() {
        return urlImage;
    }
}

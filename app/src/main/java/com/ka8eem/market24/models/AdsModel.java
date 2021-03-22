package com.ka8eem.market24.models;

import android.net.Uri;

import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class AdsModel implements Serializable {

    @SerializedName("user_id")
    private String user_id;

    @SerializedName("category_id")
    private String category_id;

    @SerializedName("subcategory_id")
    private String subcategory_id;

    @SerializedName("area_id")
    private String area_id;

    @SerializedName("subarea_id")
    private String subarea_id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private String price;

    @SerializedName("description")
    private String description;

    @SerializedName("images")
    private ArrayList<Uri> images;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("lng")
    private String longitude;

    public AdsModel(String user_id, String category_id, String subcategory_id, String area_id, String subarea_id, String name, String price, String description, ArrayList<Uri> images, String latitude, String longitude) {
        this.user_id = user_id;
        this.category_id = category_id;
        this.subcategory_id = subcategory_id;
        this.area_id = area_id;
        this.subarea_id = subarea_id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.images = images;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    public String getSubarea_id() {
        return subarea_id;
    }

    public void setSubarea_id(String subarea_id) {
        this.subarea_id = subarea_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Uri> getImages() {
        return images;
    }

    public void setImages(ArrayList<Uri> images) {
        this.images = images;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}

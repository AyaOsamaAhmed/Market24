package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CategoryModel implements Serializable {

    @SerializedName("cat_name_E")
    private String categoryNameEnglish;

    @SerializedName("cat_img")
    private String urlImage;

    @SerializedName("cat_id")
    private int categoryId;


    @SerializedName("Vehicles")
    private String isVehicles;

    @SerializedName("cat_name")
    private String categoryName;

    public CategoryModel() {

    }

    public String getCatNameEn() {
        return categoryNameEnglish;
    }

    public CategoryModel(int cat_id, String cat_name, String isVehicles) {
        this.categoryId = cat_id;
        this.categoryName = cat_name;
        this.isVehicles = isVehicles;
    }

    public void setCategoryNameEnglish(String categoryNameEnglish) {
        this.categoryNameEnglish = categoryNameEnglish;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getIsVehicles() {
        return isVehicles;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public int getCategoryId() {
        return categoryId;
    }
}

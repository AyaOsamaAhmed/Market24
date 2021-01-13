package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategoryModel implements Serializable {

    @SerializedName("id_cat")
    private int categoryId;

    @SerializedName("sub_id")
    private int subCatId;

    public SubCategoryModel(int categoryId, String cubCatName, String isCar) {
        this.categoryId = categoryId;
        this.subCatId = subCatId;
        this.imageUrl = imageUrl;
        this.cubCatName = cubCatName;
        this.isCar = isCar;
        this.subCatNameEn = subCatNameEn;
    }

    @SerializedName("sub_img")
    private String imageUrl;

    @SerializedName("sub_name_A")
    private String cubCatName;

    @SerializedName("Vehicles_Car")
    private String isCar;

    public String getIsCar() {
        return isCar;
    }

    public String getCubCatName() {
        return cubCatName;
    }

    @SerializedName("sub_name")
    private String subCatNameEn;


    public int getCategoryId() {
        return categoryId;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getSubCatNameEn() {
        return subCatNameEn;
    }
}
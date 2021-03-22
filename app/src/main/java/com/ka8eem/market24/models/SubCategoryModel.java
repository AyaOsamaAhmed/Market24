package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SubCategoryModel implements Serializable {

    @SerializedName("sub_id")
    private int sub_id;

    @SerializedName("id_cat")
    private int subCatId;

    @SerializedName("sub_img")
    private String subImage;

    @SerializedName("sub_name")
    private String cubCatName;

    @SerializedName("Vehicles_Car")
    private String isCar;

    @SerializedName("sub_name_E")
    private String subCatNameEn;

    public SubCategoryModel(int sub_id, int subCatId, String subImage, String cubCatName, String subCatNameEn) {
        this.sub_id = sub_id;
        this.subCatId = subCatId;
        this.subImage = subImage;
        this.cubCatName = cubCatName;
        this.subCatNameEn = subCatNameEn;
    }

    public int getSub_id() {
        return sub_id;
    }

    public void setSub_id(int sub_id) {
        this.sub_id = sub_id;
    }

    public int getSubCatId() {
        return subCatId;
    }

    public void setSubCatId(int subCatId) {
        this.subCatId = subCatId;
    }

    public String getSubImage() {
        return subImage;
    }

    public void setSubImage(String subImage) {
        this.subImage = subImage;
    }

    public String getCubCatName() {
        return cubCatName;
    }

    public void setCubCatName(String cubCatName) {
        this.cubCatName = cubCatName;
    }

    public String getSubCatNameEn() {
        return subCatNameEn;
    }

    public void setSubCatNameEn(String subCatNameEn) {
        this.subCatNameEn = subCatNameEn;
    }
}

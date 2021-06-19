package com.ka8eem.market24.models;

import android.widget.ImageView;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductModel implements Serializable {

    @SerializedName("ads_id")
    private String adsID;

    @SerializedName("user_id")
    private String userID;

    @SerializedName("cat_id")
    private String categoryId;

    @SerializedName("sub_cat")
    private String subCategory;

    @SerializedName("area_id")
    private String areaId;

    @SerializedName("sub_area_id")
    private String subAreaId;

    @SerializedName("product_name")
    private String product_name;

    @SerializedName("description")
    private String description;

    @SerializedName("price")
    private String price;

    @SerializedName("date")
    private String date;

    @SerializedName("active")
    private String active;

    @SerializedName("report")
    private String report;

    @SerializedName("reson_report")
    private String reason_report;

    @SerializedName("address")
    private String address;

    @SerializedName("rejected")
    private String rejected;

    @SerializedName("lat")
    private String latitude;

    @SerializedName("image")
    private ImageModel adsImage;

    @SerializedName("images")
    private List<ImageModel> listAdsImage;

    @SerializedName("long")
    private String longtitude;

    @SerializedName("category")
    private CategoryModel category;

    @SerializedName("subcategory")
    private SubCategoryModel subcategory;


    @SerializedName("is_favourite")
    private int is_favourite;

    @SerializedName("phone")
    private String phone;

    @SerializedName("able_disscussion")
    private int able_disscussion;


    public String getAdsID() {
        return adsID;
    }

    public void setAdsID(String adsID) {
        this.adsID = adsID;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(String subCategory) {
        this.subCategory = subCategory;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getSubAreaId() {
        return subAreaId;
    }

    public void setSubAreaId(String aubAreaId) {
        this.subAreaId = aubAreaId;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public String getReason_report() {
        return reason_report;
    }

    public void setReason_report(String reason_report) {
        this.reason_report = reason_report;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getRejected() {
        return rejected;
    }

    public void setRejected(String rejected) {
        this.rejected = rejected;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public ImageModel getAdsImage() {
        return adsImage;
    }

    public void setAdsImage(ImageModel adsImage) {
        this.adsImage = adsImage;
    }

    public String getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(String longtitude) {
        this.longtitude = longtitude;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public SubCategoryModel getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(SubCategoryModel subcategory) {
        this.subcategory = subcategory;
    }

    public List<ImageModel> getListAdsImage() {
        return listAdsImage;
    }

    public void setListAdsImage(List<ImageModel> listAdsImage) {
        this.listAdsImage = listAdsImage;
    }


    public int getIs_favourite() { return is_favourite; }

    public void setIs_favourite(int is_favourite) { this.is_favourite = is_favourite; }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAble_disscussion() {
        return able_disscussion;
    }

    public void setAble_disscussion(int able_disscussion) {
        this.able_disscussion = able_disscussion;
    }
}
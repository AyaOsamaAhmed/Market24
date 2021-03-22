        package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SubMainModel implements Serializable {

    @SerializedName("categories")
    private List<CategoryModel> categories;

    @SerializedName("subcategories")
    private List<SubCategoryModel> subcategories;

    @SerializedName("areas")
    private List<AreaModel> areas;

    @SerializedName("subareas")
    private List<SubAreaModel> subareas;

    @SerializedName("payment_images")
    private List<PannerModel> paymentImages;

    @SerializedName("payment_ads")
    private List<PaymentAdsModel> paymentAds;

    @SerializedName("normal_ads")
    private List<ProductModel> normalAds;


    public SubMainModel() {

    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }

    public List<SubCategoryModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubCategoryModel> subcategories) {
        this.subcategories = subcategories;
    }

    public List<AreaModel> getAreas() {
        return areas;
    }

    public void setAreas(List<AreaModel> areas) {
        this.areas = areas;
    }

    public List<SubAreaModel> getSubareas() {
        return subareas;
    }

    public void setSubareas(List<SubAreaModel> subareas) {
        this.subareas = subareas;
    }

    public List<PannerModel> getPaymentImages() {
        return paymentImages;
    }

    public void setPaymentImages(List<PannerModel> paymentImages) {
        this.paymentImages = paymentImages;
    }

    public List<PaymentAdsModel> getPaymentAds() {
        return paymentAds;
    }

    public void setPaymentAds(List<PaymentAdsModel> paymentAds) {
        this.paymentAds = paymentAds;
    }

    public List<ProductModel> getNormalAds() {
        return normalAds;
    }

    public void setNormalAds(List<ProductModel> normalAds) {
        this.normalAds = normalAds;
    }
}

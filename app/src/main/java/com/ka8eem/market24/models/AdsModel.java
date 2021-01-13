package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AdsModel implements Serializable {
    @SerializedName("ID_ADS")
    private String ID_ADS;

    public String getID_ADS() {
        return ID_ADS;
    }

    public void setID_ADS(String ID_ADS) {
        this.ID_ADS = ID_ADS;
    }

    @SerializedName("title")
    private String adsTitle;

    @SerializedName("category")
    private String adsCatId;

    @SerializedName("sub_category")
    private String adsSubCategoryId;

    @SerializedName("user_id")
    private String adsUserId;

    @SerializedName("area_id")
    private String adsAreaId;

    @SerializedName("sub_area_id")
    private String adsSubAreaId;

    @SerializedName("other_area")
    private String otherArea;

    @SerializedName("price")
    private String adsPrice;

    @SerializedName("type_price")
    private String type_price;

    @SerializedName("constant_details_Vichels")
    private VehiclesConstantDetailsModel vehiclesConstantDetailsModel;

    @SerializedName("constant_details_Biulding")
    private BuildingConstantDetailsModel buildingConstantDetailsModel;

    @SerializedName("constant_details_other")
    private OtherConstantDetailsModel otherConstantDetailsModel;

    public AdsModel() {
    }

    public AdsModel(String adsTitle, String adsCatId, String adsSubCategoryId, String adsUserId, String adsAreaId, String adsSubAreaId, String otherArea, String adsPrice, String type_price) {
        this.adsTitle = adsTitle;
        this.adsCatId = adsCatId;
        this.adsSubCategoryId = adsSubCategoryId;
        this.adsUserId = adsUserId;
        this.adsAreaId = adsAreaId;
        this.adsSubAreaId = adsSubAreaId;
        this.otherArea = otherArea;
        this.adsPrice = adsPrice;
        this.type_price = type_price;
    }

    public AdsModel(String adsTitle, String adsCatId, String adsSubCategoryId, String adsUserId, String adsAreaId,
                    String adsSubAreaId, String otherArea, String adsPrice,
                    String type_price, VehiclesConstantDetailsModel vehiclesConstantDetailsModel,
                    BuildingConstantDetailsModel buildingConstantDetailsModel, OtherConstantDetailsModel otherConstantDetailsModel) {
        this.adsTitle = adsTitle;
        this.adsCatId = adsCatId;
        this.adsSubCategoryId = adsSubCategoryId;
        this.adsUserId = adsUserId;
        this.adsAreaId = adsAreaId;
        this.adsSubAreaId = adsSubAreaId;
        this.otherArea = otherArea;
        this.adsPrice = adsPrice;
        this.type_price = type_price;
        this.vehiclesConstantDetailsModel = vehiclesConstantDetailsModel;
        this.buildingConstantDetailsModel = buildingConstantDetailsModel;
        this.otherConstantDetailsModel = otherConstantDetailsModel;
    }

    public String getAdsTitle() {
        return adsTitle;
    }

    public String getAdsCatId() {
        return adsCatId;
    }

    public String getAdsSubCategoryId() {
        return adsSubCategoryId;
    }

    public String getAdsUserId() {
        return adsUserId;
    }

    public String getAdsAreaId() {
        return adsAreaId;
    }

    public String getAdsSubAreaId() {
        return adsSubAreaId;
    }

    public String getOtherArea() {
        return otherArea;
    }

    public String getAdsPrice() {
        return adsPrice;
    }

    public String getType_price() {
        return type_price;
    }

    public VehiclesConstantDetailsModel getVehiclesConstantDetailsModel() {
        return vehiclesConstantDetailsModel;
    }

    public BuildingConstantDetailsModel getBuildingConstantDetailsModel() {
        return buildingConstantDetailsModel;
    }

    public OtherConstantDetailsModel getOtherConstantDetailsModel() {
        return otherConstantDetailsModel;
    }
}

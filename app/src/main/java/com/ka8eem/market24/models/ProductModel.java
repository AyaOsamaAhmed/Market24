package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ProductModel implements Serializable {

    @SerializedName("ads_id")
    private int productID;

    @SerializedName("account")
    private String Email_user;

    @SerializedName("user_id")
    private int userID;

    @SerializedName("phone")
    private String phoneNumber;

    @SerializedName("name")
    private String userName;

    @SerializedName("most_req")
    private int mostRequest;

    @SerializedName("cat_id")
    private int categoryID;

    @SerializedName("sub_cat")
    private String subCatId;

    @SerializedName("area_id")
    private String areaId;

    @SerializedName("sub_area_id")
    private String subAreaId;

    @SerializedName("cat_name_A")
    private String categoryName;

    @SerializedName("cat_name")
    private String categoryNameEn;

    @SerializedName("sub_name_A")
    private String subCatName;

    @SerializedName("sub_name")
    private String subCatNameEn;

    @SerializedName("area_name")
    private String cityName;

    @SerializedName("area_name_E")
    private String cityNameEn;

    @SerializedName("Sub_area_name")
    private String subCityName;

    @SerializedName("Sub_area_name_E")
    private String subCityNameEn;

    @SerializedName("product_name")
    private String productName;

    @SerializedName("price")
    private String price;

    @SerializedName("type_price")
    private String priceType;

    @SerializedName("date")
    private String dateTime;

    @SerializedName("Vehicles")
    private String isVehicles;

    @SerializedName("images")
    private ArrayList<ImageModel> productImages;

    @SerializedName("constant_details")
    private VehiclesConstantDetailsModel vehiclesConstantDetails;

    @SerializedName("constant_details_other")
    private OtherConstantDetailsModel otherConstantDetails;

    @SerializedName("constant_details_building")
    private BuildingConstantDetailsModel buildingConstantDetails;

    public ProductModel() {
    }

    public String getSubCatId() {
        return subCatId;
    }

    public String getSubAreaId() {
        return subAreaId;
    }

    public int getProductID() {
        return productID;
    }

    public String getEmail_user() {
        return Email_user;
    }

    public int getUserID() {
        return userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public int getMostRequest() {
        return mostRequest;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public String getAreaId() {
        return areaId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryNameEn() {
        return categoryNameEn;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public String getSubCatNameEn() {
        return subCatNameEn;
    }

    public String getCityName() {
        return cityName;
    }

    public String getCityNameEn() {
        return cityNameEn;
    }

    public String getSubCityName() {
        return subCityName;
    }

    public String getSubCityNameEn() {
        return subCityNameEn;
    }

    public String getProductName() {
        return productName;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceType() {
        return priceType;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getIsVehicles() {
        return isVehicles;
    }

    public ArrayList<ImageModel> getProductImages() {
        return productImages;
    }

    public VehiclesConstantDetailsModel getVehiclesConstantDetails() {
        return vehiclesConstantDetails;
    }

    public OtherConstantDetailsModel getOtherConstantDetails() {
        return otherConstantDetails;
    }

    public BuildingConstantDetailsModel getBuildingConstantDetails() {
        return buildingConstantDetails;
    }
}
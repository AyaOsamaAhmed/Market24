package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VehiclesConstantDetailsModel implements Serializable {

    @SerializedName("type_pro")
    private String vehicleType;

    @SerializedName("model")
    private String vehicleModel;

    @SerializedName("year")
    private String productionYear;

    @SerializedName("trans")
    private String transmissionType;

    @SerializedName("status")
    private String vehicleStatus;

    @SerializedName("engine")
    private String enginePower;

    @SerializedName("kilometers")
    private String kilometers;

    @SerializedName("color")
    private String color;

    @SerializedName("daiteals")
    private String vehicleDetails;

    public VehiclesConstantDetailsModel() {
    }

    public VehiclesConstantDetailsModel(String vehicleType, String vehicleModel, String productionYear, String transmissionType, String vehicleStatus, String enginePower, String kilometers, String color, String vehicleDetails) {
        this.vehicleType = vehicleType;
        this.vehicleModel = vehicleModel;
        this.productionYear = productionYear;
        this.transmissionType = transmissionType;
        this.vehicleStatus = vehicleStatus;
        this.enginePower = enginePower;
        this.kilometers = kilometers;
        this.color = color;
        this.vehicleDetails = vehicleDetails;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public String getProductionYear() {
        return productionYear;
    }

    public String getTransmissionType() {
        return transmissionType;
    }

    public String getVehicleStatus() {
        return vehicleStatus;
    }

    public String getEnginePower() {
        return enginePower;
    }

    public String getKilometers() {
        return kilometers;
    }

    public String getColor() {
        return color;
    }

    public String getVehicleDetails() {
        return vehicleDetails;
    }
}

package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BuildingConstantDetailsModel implements Serializable {

    @SerializedName("area")
    private String space;

    @SerializedName("num_room")
    private String numOfRooms;

    @SerializedName("num_bathroom")
    private String numOfBathroom;

    @SerializedName("finishing")
    private String finishing;

    @SerializedName("Lux")
    private String Lux;

    @SerializedName("floor")
    private String floorNumber;

    @SerializedName("furnished")
    private String furnished;

    @SerializedName("deatails")
    private String details;

    public BuildingConstantDetailsModel() {
    }

    public BuildingConstantDetailsModel(String space, String numOfRooms, String numOfBathroom, String finishing, String lux, String floorNumber, String furnished, String details) {
        this.space = space;
        this.numOfRooms = numOfRooms;
        this.numOfBathroom = numOfBathroom;
        this.finishing = finishing;
        Lux = lux;
        this.floorNumber = floorNumber;
        this.furnished = furnished;
        this.details = details;
    }

    public String getSpace() {
        return space;
    }

    public String getNumOfRooms() {
        return numOfRooms;
    }

    public String getNumOfBathroom() {
        return numOfBathroom;
    }

    public String getFinishing() {
        return finishing;
    }

    public String getLux() {
        return Lux;
    }

    public String getFloorNumber() {
        return floorNumber;
    }

    public String getFurnished() {
        return furnished;
    }

    public String getDetails() {
        return details;
    }
}

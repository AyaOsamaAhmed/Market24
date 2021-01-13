package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ColorModel implements Serializable {

    @SerializedName("id_color")
    private int id_color;

    @SerializedName("name_color")
    private String name_color;

    @SerializedName("name_color_E")
    private String name_color_E;

    public ColorModel(){}

    public ColorModel(int id_color, String name_color) {
        this.id_color = id_color;
        this.name_color = name_color;}

    public int getId_color() {
        return id_color;
    }

    public String getName_color() {
        return name_color;
    }

    public String getName_color_E() {
        return name_color_E;
    }

}

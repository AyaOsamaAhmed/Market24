package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelsOfCarModel implements Serializable {

    @SerializedName("id_model")
    private int id_model;

    @SerializedName("name_model")
    private String name_model;

    @SerializedName("name_model_E")
    private String name_model_E;

    public ModelsOfCarModel(){}
    public ModelsOfCarModel(int id_model, String name_model)
    {
        this.id_model = id_model;
        this.name_model = name_model;

    }
    public int getId_model() {
        return id_model;
    }

    public String getName_model() {
        return name_model;
    }

    public String getName_model_E() {
        return name_model_E;
    }
}

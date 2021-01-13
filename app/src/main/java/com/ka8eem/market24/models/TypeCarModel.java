package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TypeCarModel implements Serializable {

    @SerializedName("id_type")
    private int id_type;

    @SerializedName("name_type")
    private String name_type;

    @SerializedName("name_type_E")
    private String name_type_E;

    public TypeCarModel(){

    }
    public TypeCarModel(int id, String typeName) {
        this.id_type = id;
        this.name_type = typeName;
    }
    public int getId_type() {
        return id_type;
    }



    public String getName_type() {
        return name_type;
    }



    public String getName_type_E() {
        return name_type_E;
    }




}

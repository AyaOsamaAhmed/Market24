package com.ka8eem.market24.models;

import android.widget.ArrayAdapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class UploadImageModel implements Serializable {

    @SerializedName("ads_id")
    private String id;
    @SerializedName("ConvertImage")
    private ArrayList<String> list;

    public UploadImageModel() {
    }

    public UploadImageModel(String id, ArrayList<String> list) {
        this.id = id;
        this.list = list;
    }
}

package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MySearchProductModel implements Serializable {

    @SerializedName("current_page")
    private String current_page;

    @SerializedName("data")
    private List<ProductModel> data;

    @SerializedName("first_page_url")
    private String first_page_url;

    @SerializedName("from")
    private int from;

    @SerializedName("last_page")
    private int last_page;

    @SerializedName("last_page_url")
    private String last_page_url;

    @SerializedName("next_page_url")
    private String next_page_url;

    @SerializedName("path")
    private String path;

    @SerializedName("per_page")
    private String per_page;

    @SerializedName("prev_page_url")
    private int prev_page_url;

    @SerializedName("to")
    private int to;

    @SerializedName("total")
    private int total;

    public String getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(String current_page) {
        this.current_page = current_page;
    }

    public List<ProductModel> getData() {
        return data;
    }

    public void setData(List<ProductModel> data) {
        this.data = data;
    }
}
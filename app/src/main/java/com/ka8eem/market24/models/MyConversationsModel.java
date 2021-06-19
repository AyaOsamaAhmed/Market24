package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class MyConversationsModel implements Serializable {

    @SerializedName("current_page")
    private int current_page;

    @SerializedName("data")
    private List<ConversationModel> data;

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
    private int per_page;

    @SerializedName("prev_page_url")
    private String prev_page_url;

    @SerializedName("to")
    private int to;

    @SerializedName("total")
    private int total;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<ConversationModel> getData() {
        return data;
    }

    public void setData(List<ConversationModel> data) {
        this.data = data;
    }

    public String getFirst_page_url() {
        return first_page_url;
    }

    public int getFrom() {
        return from;
    }

    public int getLast_page() {
        return last_page;
    }

    public String getLast_page_url() {
        return last_page_url;
    }

    public String getNext_page_url() {
        return next_page_url;
    }

    public String getPath() {
        return path;
    }

    public int getPer_page() {
        return per_page;
    }

    public String getPrev_page_url() {
        return prev_page_url;
    }

    public int getTo() {
        return to;
    }

    public int getTotal() {
        return total;
    }
}
package com.ka8eem.market24.models;

public class NewMessageModel {
    private String type_sender;
    private String message;
    private String ads_id;
    private String seller_id;
    private String buyer_id;

    public NewMessageModel() {
    }

    public NewMessageModel(String type_sender, String message, String ads_id, String seller_id, String buyer_id) {
        this.type_sender = type_sender;
        this.message = message;
        this.ads_id = ads_id;
        this.seller_id = seller_id;
        this.buyer_id = buyer_id;
    }

    public String getType_sender() {
        return type_sender;
    }

    public String getMessage() {
        return message;
    }

    public String getAds_id() {
        return ads_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public String getBuyer_id() {
        return buyer_id;
    }
}


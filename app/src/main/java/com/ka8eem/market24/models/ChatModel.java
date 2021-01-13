package com.ka8eem.market24.models;

public class ChatModel {
    private String sender , receiver , message , ID_ADS , img_ad , name_ADS , date;
    private boolean isSeen;

    public boolean isSeen() {
        return isSeen;
    }

    public void setSeen(boolean seen) {
        isSeen = seen;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName_ADS() {
        return name_ADS;
    }

    public void setName_ADS(String name_ADS) {
        this.name_ADS = name_ADS;
    }

    public String getID_ADS() {
        return ID_ADS;
    }

    public void setID_ADS(String ID_ADS) {
        this.ID_ADS = ID_ADS;
    }

    public String getImg_ad() {
        return img_ad;
    }

    public void setImg_ad(String img_ad) {
        this.img_ad = img_ad;
    }

    public ChatModel (){}



    public ChatModel (String sender , String receiver , String message ,String ID_ADS , String img_ad , String name_ADS , String date , boolean isSeen){
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.ID_ADS = ID_ADS;
        this.img_ad = img_ad;
        this.name_ADS = name_ADS;
        this.date = date;
        this.isSeen = isSeen;



    }
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

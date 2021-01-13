package com.ka8eem.market24.models;

import java.util.Date;

public class ChatlistModel implements Comparable<ChatlistModel>{
    public  String   id_ads , img_ad , name , id_user , name_product ;
public Date str_date;
public int num_seen;

    public int getNum_seen() {
        return num_seen;
    }

    public void setNum_seen(int num_seen) {
        this.num_seen = num_seen;
    }

    public Date getStr_date() {
        return str_date;
    }

    public void setStr_date(Date str_date) {
        this.str_date = str_date;
    }

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public ChatlistModel() {
    }

    public ChatlistModel( String id_ads, String img_ad, String name , String id_user , String name_product , Date str_date , int num_seen) {
        this.id_user = id_user;
        this.id_ads = id_ads;
        this.img_ad = img_ad;
        this.name = name;
        this.name_product = name_product;
        this.str_date = str_date;
        this.num_seen = num_seen;

    }

    @Override
    public int compareTo(ChatlistModel o) {
        return getStr_date().compareTo(o.getStr_date());
    }



    public String getId_ads() {
        return id_ads;
    }

    public void setId_ads(String id_ads) {
        this.id_ads = id_ads;
    }

    public String getImg_ad() {
        return img_ad;
    }

    public void setImg_ad(String img_ad) {
        this.img_ad = img_ad;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

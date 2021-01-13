package com.ka8eem.market24.Notification;

public class Data {
    private String user , id_ADS ,img_ADS , name_ADS;

    public String getId_ADS() {
        return id_ADS;
    }

    public void setId_ADS(String id_ADS) {
        this.id_ADS = id_ADS;
    }

    public String getImg_ADS() {
        return img_ADS;
    }

    public void setImg_ADS(String img_ADS) {
        this.img_ADS = img_ADS;
    }

    public String getName_ADS() {
        return name_ADS;
    }

    public void setName_ADS(String name_ADS) {
        this.name_ADS = name_ADS;
    }

    private int icon;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSented() {
        return sented;
    }

    public void setSented(String sented) {
        this.sented = sented;
    }

    public Data() {
    }

    private String body;

    public Data(String user, int icon, String body, String title, String sented , String id_ADS , String img_ADS , String name_ADS) {
        this.user = user;
        this.icon = icon;
        this.body = body;
        this.title = title;
        this.sented = sented;
        this.id_ADS = id_ADS;
        this.img_ADS = img_ADS;
        this.name_ADS = name_ADS;
    }

    private String title;
    private String sented;
}

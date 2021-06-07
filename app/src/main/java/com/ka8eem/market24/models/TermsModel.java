package com.ka8eem.market24.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TermsModel implements Serializable {


    @SerializedName("en_terms")
    private String en_terms;

    @SerializedName("ar_terms")
    private String ar_terms;

    public TermsModel() {
    }

    public TermsModel(String en_terms, String ar_terms) {
        this.en_terms = en_terms;
        this.ar_terms = ar_terms;
    }

    public String getEn_terms() {
        return en_terms;
    }

    public void setEn_terms(String en_terms) {
        this.en_terms = en_terms;
    }

    public String getAr_terms() {
        return ar_terms;
    }

    public void setAr_terms(String ar_terms) {
        this.ar_terms = ar_terms;
    }
}

package com.ka8eem.market24.models;

import java.io.Serializable;

public class ReportModel implements Serializable {

    private int adsId;
    private String reasonForReport;

    public ReportModel(int adsId, String reasonForReport) {
        this.adsId = adsId;
        this.reasonForReport = reasonForReport;
    }

    public int getAdsId() {
        return adsId;
    }

    public String getReasonForReport() {
        return reasonForReport;
    }
}

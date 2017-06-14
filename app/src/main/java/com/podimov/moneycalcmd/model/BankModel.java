package com.podimov.moneycalcmd.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BankModel implements Serializable {

    private Integer date;

    @SerializedName("organizations")
    @Expose
    private Organizations organizations;

    public BankModel() {
    }

    public Integer getDate() {
        return date;
    }

    public Organizations getOrganizations() {
        return organizations;
    }
}

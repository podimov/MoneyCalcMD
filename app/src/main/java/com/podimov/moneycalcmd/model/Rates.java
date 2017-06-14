
package com.podimov.moneycalcmd.model;

import com.google.gson.annotations.SerializedName;

public class Rates {

    @SerializedName("EUR")
    private com.podimov.moneycalcmd.model.EUR mEUR;
    @SerializedName("GBP")
    private com.podimov.moneycalcmd.model.GBP mGBP;
    @SerializedName("RON")
    private com.podimov.moneycalcmd.model.RON mRON;
    @SerializedName("RUB")
    private com.podimov.moneycalcmd.model.RUB mRUB;
    @SerializedName("UAH")
    private com.podimov.moneycalcmd.model.UAH mUAH;
    @SerializedName("USD")
    private com.podimov.moneycalcmd.model.USD mUSD;

    public com.podimov.moneycalcmd.model.EUR getEUR() {
        return mEUR;
    }

    public void setEUR(com.podimov.moneycalcmd.model.EUR EUR) {
        mEUR = EUR;
    }

    public com.podimov.moneycalcmd.model.GBP getGBP() {
        return mGBP;
    }

    public void setGBP(com.podimov.moneycalcmd.model.GBP GBP) {
        mGBP = GBP;
    }

    public com.podimov.moneycalcmd.model.RON getRON() {
        return mRON;
    }

    public void setRON(com.podimov.moneycalcmd.model.RON RON) {
        mRON = RON;
    }

    public com.podimov.moneycalcmd.model.RUB getRUB() {
        return mRUB;
    }

    public void setRUB(com.podimov.moneycalcmd.model.RUB RUB) {
        mRUB = RUB;
    }

    public com.podimov.moneycalcmd.model.UAH getUAH() {
        return mUAH;
    }

    public void setUAH(com.podimov.moneycalcmd.model.UAH UAH) {
        mUAH = UAH;
    }

    public com.podimov.moneycalcmd.model.USD getUSD() {
        return mUSD;
    }

    public void setUSD(com.podimov.moneycalcmd.model.USD USD) {
        mUSD = USD;
    }

}

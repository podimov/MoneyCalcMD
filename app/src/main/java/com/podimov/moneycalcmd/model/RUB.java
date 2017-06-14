
package com.podimov.moneycalcmd.model;

import com.google.gson.annotations.SerializedName;

public class RUB {

    @SerializedName("buy")
    private Double mBuy;
    @SerializedName("sell")
    private Double mSell;

    public Double getBuy() {
        return mBuy;
    }

    public void setBuy(Double buy) {
        mBuy = buy;
    }

    public Double getSell() {
        return mSell;
    }

    public void setSell(Double sell) {
        mSell = sell;
    }

}

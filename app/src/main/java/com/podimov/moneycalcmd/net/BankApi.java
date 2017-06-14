package com.podimov.moneycalcmd.net;

import com.podimov.moneycalcmd.model.BankModel;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BankApi {
    @GET("/finansy/rates/")
    Call<BankModel> getData();
}
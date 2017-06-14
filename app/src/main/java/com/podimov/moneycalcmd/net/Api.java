package com.podimov.moneycalcmd.net;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Api {
    private Retrofit retrofit;
    public Api() {

        retrofit = new Retrofit.Builder()
                .baseUrl("https://point.md")
//                .client(new OkHttpClient.Builder()
//                        .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
//                        .addInterceptor(new Interceptor() {
//                            @Override
//                            public okhttp3.Response intercept(Chain chain) throws IOException {
//                                Request originalRequest = chain.request();
//                                Request.Builder builder = originalRequest.newBuilder();
//
//                                Request newRequest = builder.build();
//
//                                return chain.proceed(newRequest);
//                            }
//                        })
//                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public BankApi get(){
        return retrofit.create(BankApi.class);
    }


}

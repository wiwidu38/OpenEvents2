package com.williambastos.openevents.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class APIConnect {
    // API formatted with ip because retrofit doesn't work with classic url. I do ping to find correct ip
    private static final String API_URL ="http://172.16.205.68/api/v2/";
    private static Retrofit retrofit;

    public static Retrofit getRetrofitInstance(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}

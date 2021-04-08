package com.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRetrofit {
    private static final String BASE_URL="https://uniqueandrocode.000webhostapp.com/onbordingscreen/";
    private static MyRetrofit myClient;
    private Retrofit retrofit;
 
    private MyRetrofit(){
        retrofit=new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    }
    public static synchronized com.util.MyRetrofit getInstance(){
        if (myClient==null){
            myClient=new com.util.MyRetrofit();
        }
        return myClient;
    }
    public MySliderApi getMyApi(){
        return retrofit.create(MySliderApi.class);
    }
}
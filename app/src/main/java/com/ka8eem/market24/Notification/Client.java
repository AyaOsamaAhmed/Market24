package com.ka8eem.market24.Notification;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Client {
    private  static Retrofit retrofit = null ;

    public static Retrofit getClient(String url)
    {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        if(retrofit == null)
        {
            retrofit= new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                            .addInterceptor(loggingInterceptor)
                            .build())
                    .build();
        }
        return retrofit;
    }
}

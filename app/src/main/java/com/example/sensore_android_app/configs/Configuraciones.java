package com.example.sensore_android_app.configs;


import static com.example.sensore_android_app.utils.Const.URL;

import com.example.sensore_android_app.interfaces.HumedadApi;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class Configuraciones {

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .connectTimeout(5000L, TimeUnit.MILLISECONDS)
                .readTimeout(5000L, TimeUnit.MILLISECONDS)
                .build();
    }

    @Bean
    public Retrofit retrofit(OkHttpClient okHttpClient, ObjectMapper objectMapper) {
        return retrofitBase(URL, okHttpClient, objectMapper)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    private Retrofit.Builder retrofitBase(String baseUrl, OkHttpClient okHttpClient, ObjectMapper objectMapper){
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient);
    }

    @Bean
    public HumedadApi humedadApi(Retrofit retrofit) {
        return retrofit.create(HumedadApi.class);
    }
}

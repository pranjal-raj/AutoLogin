package com.example.autologin

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class api_controller {
    companion object{

        fun clinten() : OkHttpClient {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder().addInterceptor(logging).build()
            return client
        }

        var gson = GsonBuilder()
            .setLenient()
            .create()

                val retrofit = Retrofit.Builder()
                .baseUrl("https://api.agridhaan.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(clinten())
                .build()
        val retrofit_nonLenient = Retrofit.Builder()
            .baseUrl("https://api.agridhaan.com")
            .addConverterFactory(GsonConverterFactory.create())
            .client(clinten())
            .build()

    }
}
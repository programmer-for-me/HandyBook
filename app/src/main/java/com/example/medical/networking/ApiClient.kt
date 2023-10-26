package com.example.medical.networking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://handybook.uz")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }
}
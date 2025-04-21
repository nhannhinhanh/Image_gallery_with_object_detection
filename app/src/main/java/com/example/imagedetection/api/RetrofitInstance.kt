// api/RetrofitInstance.kt
package com.example.imagedetection.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    val api: PixabayApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://pixabay.com/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PixabayApiService::class.java)
    }
}
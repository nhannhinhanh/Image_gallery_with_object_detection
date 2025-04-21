// api/PixabayApiService.kt
package com.example.imagedetection.api

import com.example.imagedetection.data.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

    @GET(".")
    suspend fun searchImages(
        @Query("key") apiKey: String = "49853739-934e9f6b32c200a03199bd004",
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Response
}
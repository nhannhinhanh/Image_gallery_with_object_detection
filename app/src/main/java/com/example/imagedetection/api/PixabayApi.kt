
package com.example.imagedetection.api

import com.example.imagedetection.data.NewResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {

    @GET(".")
    suspend fun searchImages(
        @Query("key") apiKey: String = "43709645-f31331e117d8c8bbab31a2cd5",
        @Query("q") query: String,
        @Query("image_type") imageType: String = "photo",
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): NewResponse
}
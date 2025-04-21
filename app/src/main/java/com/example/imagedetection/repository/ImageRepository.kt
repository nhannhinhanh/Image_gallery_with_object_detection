// repository/ImageRepository.kt
package com.example.imagedetection.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imagedetection.api.PixabayApiService
import com.example.imagedetection.api.RetrofitInstance
import com.example.imagedetection.data.ImageItem
import com.example.imagedetection.paging.PagingSource
import kotlinx.coroutines.flow.Flow

class ImageRepository(
    private val apiService: PixabayApiService = RetrofitInstance.api
) {
    fun getImageStream(query: String): Flow<PagingData<ImageItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),

            pagingSourceFactory = { PagingSource(query, apiService) }
        ).flow
    }
}
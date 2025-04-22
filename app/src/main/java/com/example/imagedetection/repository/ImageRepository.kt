package com.example.imagedetection.repository

import androidx.paging.*
import com.example.imagedetection.api.PixabayApi
import com.example.imagedetection.api.RetrofitInstance
import com.example.imagedetection.data.ImageItem
import com.example.imagedetection.paging.NewPagingSource
import kotlinx.coroutines.flow.Flow

class ImageRepository(
    private val apiService: PixabayApi = RetrofitInstance.api
) {
    private val pagingConfig = PagingConfig(
        pageSize = 10,
        enablePlaceholders = false
    )

    fun getImageStream(query: String): Flow<PagingData<ImageItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = { createPagingSource(query) }
        ).flow
    }

    private fun createPagingSource(query: String): PagingSource<Int, ImageItem> {
        return NewPagingSource(query, apiService)
    }
}

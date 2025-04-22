package com.example.imagedetection.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagedetection.api.PixabayApi
import com.example.imagedetection.data.ImageItem
import retrofit2.HttpException
import java.io.IOException

class NewPagingSource(
    private val query: String,
    private val apiService: PixabayApi
) : PagingSource<Int, ImageItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
        val currentPage = params.key ?: 1
        val perPage = params.loadSize.coerceIn(3, 200) // Đảm bảo perPage nằm trong khoảng 3-200

        return try {
            val response = apiService.searchImages(
                query = query,
                page = currentPage,
                perPage = perPage
            )
            val images = response.hits

            val prevKey = if (currentPage > 1) currentPage - 1 else null
            val nextKey = if (images.isNotEmpty()) currentPage + 1 else null

            LoadResult.Page(
                data = images,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(Exception("Unknown error occurred", e))
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageItem>): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.let { page ->
                page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
            }
        }
    }
}

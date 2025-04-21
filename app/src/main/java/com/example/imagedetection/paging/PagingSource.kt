package com.example.imagedetection.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagedetection.api.PixabayApiService
import com.example.imagedetection.data.ImageItem
import retrofit2.HttpException
import java.io.IOException

class PagingSource(
    private val query: String,
    private val apiService: PixabayApiService
) : PagingSource<Int, ImageItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ImageItem> {
        val page = params.key ?: 1
        return try {
            val response = apiService.searchImages(
                query = query,
                page = page,
                perPage = params.loadSize
            )
            val images = response.hits
            LoadResult.Page(
                data = images,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (images.isNotEmpty()) page + 1 else null
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ImageItem>): Int? {
        val anchor = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchor)
        return page?.prevKey?.plus(1) ?: page?.nextKey?.minus(1)
    }
}

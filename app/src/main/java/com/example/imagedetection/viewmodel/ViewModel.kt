package com.example.imagedetection.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.imagedetection.analyzer.ImageAnalyzer
import com.example.imagedetection.data.ImageItem
import com.example.imagedetection.data.ProcessedImage
import com.example.imagedetection.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext

class ViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ImageRepository()
    private val analyzer = ImageAnalyzer(application.applicationContext)
    private val currentQuery = MutableStateFlow("technology")

    val imageFlow: Flow<PagingData<ProcessedImage>> = currentQuery
        .flatMapLatest { query ->
            repository.getImageStream(query).map { pagingData ->
                pagingData.map { item ->
                    val labels = analyzeInBackground(item.webformatURL)
                    ProcessedImage(
                        id = item.id,
                        imageUrl = item.webformatURL,
                        tags = item.tags,
                        user = item.user,
                        detectedLabels = labels
                    )
                }
            }
        }
        .cachedIn(viewModelScope)

    private suspend fun analyzeInBackground(imageUrl: String): List<String> =
        withContext(Dispatchers.IO) {
            analyzer.analyzeImage(imageUrl)
        }

    fun searchImages(query: String) {
        currentQuery.value = query.trim().ifEmpty { "technology" }
    }
}

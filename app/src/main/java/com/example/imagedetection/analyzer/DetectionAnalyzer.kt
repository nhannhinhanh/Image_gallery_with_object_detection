package com.example.imagedetection.analyzer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import coil.ImageLoader
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class DetectionAnalyzer(private val context: Context) {
    private val labeler by lazy { ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS) }

    suspend fun analyzeImage(imageUrl: String): List<String> {
        return runCatching {
            val bitmap = fetchBitmapFromUrl(imageUrl) ?: return emptyList()
            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()
            labels.take(3).map { it.text }
        }.getOrElse {
            emptyList()
        }
    }

    private suspend fun fetchBitmapFromUrl(imageUrl: String): Bitmap? {
        val imageRequest = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()

        val result = ImageLoader(context).execute(imageRequest)
        val drawable = result.drawable
        return (drawable as? BitmapDrawable)?.bitmap
    }
}

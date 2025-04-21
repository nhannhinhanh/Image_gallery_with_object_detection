package com.example.imagedetection.analyzer

import android.content.Context
import coil.imageLoader
import coil.request.ImageRequest
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.tasks.await

class ImageAnalyzer(private val context: Context) {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun analyzeImage(imageUrl: String): List<String> {
        return try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false)
                .build()

            val result = context.imageLoader.execute(request)
            val bitmap = (result.drawable as? android.graphics.drawable.BitmapDrawable)?.bitmap
                ?: return emptyList()

            val image = InputImage.fromBitmap(bitmap, 0)
            val labels = labeler.process(image).await()
            labels.take(3).map { it.text }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

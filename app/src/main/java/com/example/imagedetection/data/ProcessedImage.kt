package com.example.imagedetection.data

data class ProcessedImage(
    val id: Long,
    val imageUrl: String,
    val tags: String,
    val user: String,
    val detectedLabels: List<String>
)
package com.example.imagedetection.data

data class NewResponse(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageItem>
)

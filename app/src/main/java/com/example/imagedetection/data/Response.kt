package com.example.imagedetection.data

data class Response(
    val total: Int,
    val totalHits: Int,
    val hits: List<ImageItem>
)

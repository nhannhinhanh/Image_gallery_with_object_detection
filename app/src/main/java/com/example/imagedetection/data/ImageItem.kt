// data/ImageItem.kt
package com.example.imagedetection.data

data class ImageItem(
    val id: Long,
    val tags: String,
    val imageURL: String?,
    val imageWidth: Int,
    val imageHeight: Int,
    val imageSize: Int,
    val webformatURL: String,
    val user: String,
)

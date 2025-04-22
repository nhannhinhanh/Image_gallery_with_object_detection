package com.example.imagedetection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imagedetection.R
import com.example.imagedetection.data.ProcessedImage
import com.example.imagedetection.databinding.ItemImageBinding

class DetectionAdapter : PagingDataAdapter<ProcessedImage, DetectionAdapter.ImageViewHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ImageViewHolder(private val binding: ItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ProcessedImage?) {
            if (item == null) {
                binding.imageView.setImageResource(R.drawable.ic_launcher_background)
                binding.tvLabels.text = "Loading..."
            } else {
                binding.imageView.load(item.imageUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_background)
                    error(R.drawable.ic_launcher_foreground)
                }
                binding.tvLabels.text = item.detectedLabels.takeIf { it.isNotEmpty() }?.joinToString() ?: "N/A"
            }
        }
    }

    companion object {
        private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ProcessedImage>() {
            override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
                oldItem == newItem
        }
    }
}

package com.mack.docscan.Adapter

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mack.docscan.data.FilterData
import com.mack.docscan.databinding.FilterLayoutBinding
import com.mack.docscan.utils.FilterType
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageColorInvertFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGaussianBlurFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGrayscaleFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSaturationFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSepiaToneFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter
import jp.co.cyberagent.android.gpuimage.filter.GPUImageVignetteFilter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FilterAdapter(
    private val context: Context,
    private val filters: List<FilterData>,
    private val originalBitmap: Bitmap,
    private val onFilterSelected: (Bitmap) -> Unit
) : RecyclerView.Adapter<FilterAdapter.FilterViewHolder>() {

    private val adapterScope = CoroutineScope(Dispatchers.Main + Job())
    private val filterCache = mutableMapOf<FilterType, Bitmap?>() // Cache filtered bitmaps

    inner class FilterViewHolder(private val binding: FilterLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(filter: FilterData) {
            binding.filterType.text = filter.name

            // Use preview image for faster filter application
            val previewBitmap = getResizedBitmap(originalBitmap, 100, 100)

            // Apply filter on preview bitmap for fast UI update
            adapterScope.launch(Dispatchers.IO) {
                val filterPreviewBitmap = applyGPUFilter(filter.type, previewBitmap)
                withContext(Dispatchers.Main) {
                    binding.filterImage.setImageBitmap(filterPreviewBitmap)
                }
            }

            binding.root.setOnClickListener {
                // Apply full-size filter lazily
                applyFullSizeFilter(filter.type)
            }
        }

        private fun applyFullSizeFilter(filterType: FilterType) {
            // Check if filtered bitmap is cached
            val cachedBitmap = filterCache[filterType]
            if (cachedBitmap != null) {
                onFilterSelected(cachedBitmap)
                return
            }

            // Log the start time
            val startTime = System.currentTimeMillis()
            Log.d("FilterAdapter", "Start applying filter: ${filterType.name} at $startTime")

            adapterScope.launch(Dispatchers.IO) {
                // Apply the filter on the full-sized image in the background
                val filteredBitmap = applyGPUFilter(filterType, originalBitmap)

                // Cache the filtered bitmap
                filterCache[filterType] = filteredBitmap

                // Log the end time after the task finishes
                withContext(Dispatchers.Main) {
                    onFilterSelected(filteredBitmap)
                    val endTime = System.currentTimeMillis()
                    Log.d("FilterAdapter", "Finished applying filter: ${filterType.name} at $endTime")
                    Log.d("FilterAdapter", "Time taken to apply filter ${filterType.name}: ${endTime - startTime} ms")
                }
            }
        }
    }

    private fun applyGPUFilter(filterType: FilterType, bitmap: Bitmap): Bitmap {
        // Apply filters using GPUImage
        val gpuImage = GPUImage(context)
        gpuImage.setImage(bitmap)

        val filter = when (filterType) {
            FilterType.ORIGINAL -> GPUImageFilter()
            FilterType.SEPIA -> GPUImageSepiaToneFilter()
            FilterType.GRAYSCALE -> GPUImageGrayscaleFilter()
            FilterType.SATURATION -> GPUImageSaturationFilter()
            FilterType.BRIGHTNESS -> GPUImageBrightnessFilter()
            FilterType.SHARPEN -> GPUImageSharpenFilter()
            FilterType.INVERT -> GPUImageColorInvertFilter()
            FilterType.VIGNETTE -> GPUImageVignetteFilter()
            FilterType.BLUR -> GPUImageGaussianBlurFilter()
        }
        gpuImage.setFilter(filter)
        return gpuImage.bitmapWithFilterApplied
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterViewHolder {
        val binding = FilterLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FilterViewHolder(binding)
    }

    override fun getItemCount(): Int = filters.size

    override fun onBindViewHolder(holder: FilterViewHolder, position: Int) {
        holder.bind(filters[position])
    }

    // When the adapter is detached we clean the coroutine scope
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        adapterScope.cancel()
    }

    private fun getResizedBitmap(originalBitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(originalBitmap, width, height, false)
    }
}
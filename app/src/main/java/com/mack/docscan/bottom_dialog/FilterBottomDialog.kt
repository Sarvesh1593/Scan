package com.mack.docscan.bottom_dialog

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mack.docscan.Adapter.FilterAdapter
import com.mack.docscan.data.FilterData
import com.mack.docscan.databinding.FilterBottomSheetDialogBinding
import com.mack.docscan.utils.FilterType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.logging.Filter

class FilterBottomDialog(
    private val originalBitmap: Bitmap,
    private val onFilterApplied : (Bitmap) -> Unit
) : BottomSheetDialogFragment()  {

    private var binding : FilterBottomSheetDialogBinding? = null
    private lateinit var filterAdapter : FilterAdapter
    private var currentFilteredBitmap : Bitmap? = null

    private val dialogScope  = CoroutineScope(Dispatchers.IO + Job())
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FilterBottomSheetDialogBinding.inflate(layoutInflater,container,false)


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val filters = listOf(
            FilterData("ORIGINAL",FilterType.ORIGINAL),
            FilterData("SEPIA",FilterType.SEPIA),
            FilterData("GRAYSCALE",FilterType.GRAYSCALE),
            FilterData("SATURATION",FilterType.SATURATION),
            FilterData("BRIGHTNESS",FilterType.BRIGHTNESS),
            FilterData("SHARPEN",FilterType.SHARPEN),
            FilterData("INVERT",FilterType.INVERT),
            FilterData("VIGNETTE",FilterType.VIGNETTE),
            FilterData("BLUR",FilterType.BLUR)
        )
        filterAdapter = FilterAdapter(requireContext(),filters,originalBitmap) {filterBitmap ->
            dialogScope.launch(Dispatchers.Main){
                currentFilteredBitmap = filterBitmap
            onFilterApplied(filterBitmap)
            }
        }

        binding?.filterRv?.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
        binding?.filterRv?.adapter = filterAdapter

        binding?.cancel?.setOnClickListener {
            dismiss()
        }

        binding?.done?.setOnClickListener {
            dialogScope.launch(Dispatchers.IO) {
                currentFilteredBitmap?.let { filteredBitmap ->
                    withContext(Dispatchers.Main) {
                        onFilterApplied(filteredBitmap)
                    }
                }
                dismiss()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        dialogScope.cancel()
        binding = null
    }
}
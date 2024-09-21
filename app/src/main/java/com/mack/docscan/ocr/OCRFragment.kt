package com.mack.docscan.ocr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.mack.docscan.ViewModel.ImageSharedViewModel
import com.mack.docscan.databinding.FragmentOCRBinding

class OCRFragment : Fragment() {

    private var binding : FragmentOCRBinding? = null
    private val imageSharedViewModel: ImageSharedViewModel by lazy {
         ViewModelProvider(requireActivity())[ImageSharedViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentOCRBinding.inflate(layoutInflater,container,false)

        imageSharedViewModel.recognizedText.observe(viewLifecycleOwner){
            binding?.OCRTextview?.text = it
        }

        return binding!!.root
    }
}
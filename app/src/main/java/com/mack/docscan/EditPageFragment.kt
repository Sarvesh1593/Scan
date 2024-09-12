package com.mack.docscan

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mack.docscan.ViewModel.ImageSharedViewModel
import com.mack.docscan.ViewModel.RotateSharedViewModel
import com.mack.docscan.databinding.FragmentEditPageBinding
import com.yalantis.ucrop.UCrop
import java.io.File


class EditPageFragment : Fragment() {

    private var binding: FragmentEditPageBinding? = null
    private lateinit var rotateSharedViewModel: RotateSharedViewModel
    private lateinit var imageSharedViewModel: ImageSharedViewModel
    private var currentUri: Uri? = null
    private lateinit var cropActivityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        cropActivityResultLauncher= registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ){ result ->
            if(result.resultCode == Activity.RESULT_OK){
                result.data?.let { data ->
                    val resultUri = UCrop.getOutput(data)
                    binding?.IVPage?.setImageURI(resultUri)
                    currentUri = resultUri
                }
            }

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditPageBinding.inflate(layoutInflater, container, false)

        binding?.tvRotate?.setOnClickListener {
            // Share the most recent image (original or rotated)
            currentUri?.let { uri ->
                rotateSharedViewModel.setUri(uri)
                findNavController().navigate(EditPageFragmentDirections.actionEditPageFragmentToRotateFragment())
            }
        }

        binding?.tvCrop?.setOnClickListener {
            imageCrop(currentUri)
        }
        return binding?.root
    }

    private fun imageCrop(uri: Uri?) {
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir,"CroppedImage.jpg"))
        val option = UCrop.Options()

        option.setToolbarTitle("Image Crop")
        option.setFreeStyleCropEnabled(true)
        option.setCropGridColor(4)
        option.setHideBottomControls(true)
        option.setImageToCropBoundsAnimDuration(1000)



        val ucrop = uri?.let { UCrop.of(it,destinationUri).withOptions(option) }
        if (ucrop != null) {
            cropActivityResultLauncher.launch(ucrop.getIntent(requireContext()))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageSharedViewModel = ViewModelProvider(requireActivity())[ImageSharedViewModel::class.java]
        rotateSharedViewModel = ViewModelProvider(requireActivity())[RotateSharedViewModel::class.java]

        // Observe imageSharedViewModel to handle the initial or default image
        imageSharedViewModel.imageUri.observe(viewLifecycleOwner) { uri ->
            currentUri = uri  // Always update currentUri
            Log.d("EditPageFragment", "Default image URI: $uri")
            updateImageView(uri)
        }

        // Observe rotateSharedViewModel to handle rotated images
        rotateSharedViewModel.uri.observe(viewLifecycleOwner) { uri ->
            currentUri = uri  // Always update currentUri
            Log.d("EditPageFragment", "Rotated image URI: $uri")
            updateImageView(uri)
        }
    }

    // Helper function to update the ImageView
    private fun updateImageView(uri: Uri?) {
        binding?.IVPage?.apply {
            setImageURI(null)  // Clear existing image
            setImageURI(uri)   // Set new image
        }
    }
}

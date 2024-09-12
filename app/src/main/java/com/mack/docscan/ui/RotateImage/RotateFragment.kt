package com.mack.docscan.ui.RotateImage

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mack.docscan.ViewModel.RotateSharedViewModel
import com.mack.docscan.databinding.FragmentRotateBinding
import java.io.File
import java.io.FileOutputStream


class RotateFragment : Fragment() {

    private lateinit var binding : FragmentRotateBinding
    private var currentBitmap: Bitmap? = null
    private var originalBitmap: Bitmap? = null
    private var currentAngle = 0f
    private var currentUri : Uri? = null
    private lateinit var rotateSharedViewModel : RotateSharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRotateBinding.inflate(layoutInflater, container,false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // For initialize the rotated Shared viewModel
        rotateSharedViewModel = ViewModelProvider(requireActivity())[RotateSharedViewModel::class.java]
        rotateSharedViewModel.uri.observe(viewLifecycleOwner){ uri ->
            Log.d("RotateFragment", "Updated image URI: $uri")
            if (uri != currentUri) {
                currentUri = uri
                updateImageView(uri)
                currentBitmap = uriToBitmap(uri)
                originalBitmap = currentBitmap // Reset angle when new image is loaded
            }

        }
        binding.rotateBtn.setOnClickListener {
            rotateImage()
        }

        binding.resetBtn.setOnClickListener {
            resetImage()
        }
        binding.doneBtn.setOnClickListener {
            currentBitmap.let { bitmap ->
                val uri = saveBitmapToFile(bitmap!!)
                rotateSharedViewModel.setImageUri(uri)
                Log.d("RotatedImage",uri.toString())
                findNavController().navigate(RotateFragmentDirections.actionRotateFragmentToEditPageFragment())
            }
        }

        binding.crossBtn.setOnClickListener {
            findNavController().navigate(RotateFragmentDirections.actionRotateFragmentToEditPageFragment())
        }
    }

    // Helper function to update the ImageView
    private fun updateImageView(uri: Uri?) {
        binding.imageviewId.apply {
            setImageURI(null)  // Clear existing image
            setImageURI(uri)   // Set new image
        }
    }

    private fun saveBitmapToFile(bitmap: Bitmap): Uri {
        val file = File(requireContext().cacheDir, "rotated_image.jpg")
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
        }
        return Uri.fromFile(file)
    }
    // Change the image type from uri to bitmap
    private fun uriToBitmap(uri: Uri): Bitmap? {
        return uri.path?.let {
            val file = File(it)
            BitmapFactory.decodeFile(file.absolutePath)
        }
    }

    // This function for rotating the image
    private fun rotateImage(){
        currentAngle +=90f
        currentBitmap?.let {
            currentBitmap = rotateBitMap(it,currentAngle)
            binding.imageviewId.setImageBitmap(currentBitmap)
        }
    }

    // The is function to reset the image
    private fun resetImage(){
        currentAngle = 0f
        currentBitmap = originalBitmap
        binding.imageviewId.setImageBitmap(currentBitmap)
    }

    // For Rotate the image
    private fun rotateBitMap(bitmap: Bitmap,angle: Float) : Bitmap{
        val matrix = android.graphics.Matrix().apply { postRotate(angle) }
        return Bitmap.createBitmap(bitmap,0,0,bitmap.width,bitmap.height,matrix,true)
    }


}
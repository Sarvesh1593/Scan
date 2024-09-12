package com.mack.docscan.ui.mainScreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.mack.docscan.R
import com.mack.docscan.ViewModel.ImageSharedViewModel
import com.mack.docscan.ViewModel.SharedViewModel
import com.mack.docscan.databinding.FragmentCameraBinding
import java.io.File

class CameraFragment : Fragment() {

    private var binding : FragmentCameraBinding? = null
    private var imageCapture: ImageCapture? = null
    private var currentFlashModeIndex = 0
    private lateinit var popupWindow : PopupWindow
    private lateinit var imageSharedViewModel: ImageSharedViewModel
    private val flashIcons = listOf(
        R.drawable.off_flash,
        R.drawable.on_flash,
        R.drawable.autoflash
    )
    private var isPhotoBeingTaken = false

    private val args : CameraFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(layoutInflater, container, false)

        startCamera()


        binding!!.clickButton.setOnClickListener {
            if (!isPhotoBeingTaken) {
                isPhotoBeingTaken = true
                Log.d("Capture", "Image capture initiated")
                takePhoto()
            }
            photoClickAnimateBtn(binding!!.clickButton)
        }
        binding!!.grid.setOnClickListener {
            gridButton()
        }
        binding!!.flashMode.setOnClickListener {
            showFlashMode()
        }
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageSharedViewModel  = ViewModelProvider(requireActivity())[ImageSharedViewModel::class.java]
        binding?.cancel?.setOnClickListener {
            findNavController().navigate(R.id.action_cameraFragment_to_mainScreen)
        }

    }
    @SuppressLint("Recycle")
    private fun photoClickAnimateBtn(btn : ImageButton) {
        val scaleDownX = ObjectAnimator.ofFloat(btn, "scaleX", 0.8f)
        val scaleDownY = ObjectAnimator.ofFloat(btn, "scaleY", 0.8f)
        scaleDownX.duration = 500
        scaleDownY.duration = 500

        val scaleUpX = ObjectAnimator.ofFloat(btn, "scaleX", 1f)
        val scaleUpY = ObjectAnimator.ofFloat(btn, "scaleY", 1f)
        scaleUpX.duration = 500
        scaleUpY.duration = 500

        val animatorSet = AnimatorSet()
        animatorSet.play(scaleDownX).with(scaleDownY)
        animatorSet.play(scaleUpX).with(scaleUpY).after(scaleDownX)
        animatorSet.start()
    }

    private fun gridButton() {
        if (binding!!.gridOverlay.visibility == View.VISIBLE) {
            binding!!.gridOverlay.visibility = View.GONE
        } else {
            binding!!.gridOverlay.visibility = View.VISIBLE
        }
    }

    @SuppressLint("InflateParams")
    private fun showFlashMode() {
        val view = layoutInflater.inflate(R.layout.custom_flash_mode_layout, null)
        popupWindow = PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true)

        val flashOff = view.findViewById<ImageButton>(R.id.btn_flash_off)
        val flashOn = view.findViewById<ImageButton>(R.id.btn_flash_on)
        val flashAuto = view.findViewById<ImageButton>(R.id.btn_flash_auto)

        flashOff.setOnClickListener {
            currentFlashModeIndex = 0
            imageCapture!!.flashMode = ImageCapture.FLASH_MODE_OFF
            updateFlashModeIcon()
            popupWindow.dismiss()
        }
        flashOn.setOnClickListener {
            currentFlashModeIndex = 1
            imageCapture!!.flashMode = ImageCapture.FLASH_MODE_ON
            updateFlashModeIcon()
            popupWindow.dismiss()
        }
        flashAuto.setOnClickListener {
            currentFlashModeIndex = 2
            imageCapture!!.flashMode = ImageCapture.FLASH_MODE_AUTO
            updateFlashModeIcon()
            popupWindow.dismiss()
        }
        popupWindow.showAsDropDown(binding!!.flashMode)
    }

    private fun updateFlashModeIcon() {
        binding!!.flashMode.setImageResource(flashIcons[currentFlashModeIndex])
    }

    private fun startCamera() {
        val cameraScreen = binding!!.cameraScreen
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraScreen.surfaceProvider)
                }

            if (imageCapture == null) {
                imageCapture = ImageCapture.Builder().build()
            }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider?.unbindAll()
                cameraProvider?.bindToLifecycle(
                    viewLifecycleOwner, cameraSelector, preview, imageCapture
                )
            } catch (exc: Exception) {
                Log.e("CameraFragment", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"${System.currentTimeMillis()}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri?: Uri.fromFile(photoFile)
                    handleCapturedImage(savedUri)
                }
                override fun onError(exception: ImageCaptureException) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
    private fun handleCapturedImage(capturedImageUri: Uri) {
        val currentIndex = imageSharedViewModel.currentIndex.value
        if (currentIndex != null && currentIndex >= 0) {
            imageSharedViewModel.replaceImage(capturedImageUri, currentIndex)
        } else {
            imageSharedViewModel.addImage(capturedImageUri)
        }
        findNavController().navigate(R.id.action_cameraFragment_to_editFragment)
    }
}

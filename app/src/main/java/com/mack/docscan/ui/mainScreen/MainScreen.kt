package com.mack.docscan.ui.mainScreen


import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.mack.docscan.R
import com.mack.docscan.ViewModel.SharedViewModel
import com.mack.docscan.bottom_dialog.UploadFileDialog
import com.mack.docscan.databinding.FragmentMainScreenBinding


class MainScreen : Fragment() {

    private var _binding: FragmentMainScreenBinding? = null
    private val binding get() = _binding!!
    private val bottomSheetDialogFragment = UploadFileDialog()
    private lateinit var viewModel : SharedViewModel
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("ExampleFragment", "onCreateView called")
        // Inflate the layout for this fragment
         _binding = FragmentMainScreenBinding.inflate(inflater, container, false)


        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.bg_color)
        requireActivity().window.navigationBarColor = ContextCompat.getColor(requireContext(),R.color.bg_color)
        checkCameraPermission()
        requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.READ_MEDIA_IMAGES))

        // for opening the bottom sheet Dialog
        _binding?.bottombtn?.setOnClickListener {
            bottomSheetDialogFragment.show(childFragmentManager,"UploadFile")
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        viewModel.pdfUri.observe(viewLifecycleOwner) { uri ->
            if (uri != null) {
                findNavController().navigate(R.id.action_mainScreen_to_PDFViewer)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun checkCameraPermission(){
        if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.CAMERA,android.Manifest.permission.READ_MEDIA_IMAGES))
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.entries.forEach { it ->
            val permissionName = it.key
            val isGranted = it.value
            if(isGranted){
                Log.d("Permissions", "$permissionName granted")
            }

        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        bottomSheetDialogFragment.dismiss()
    }
}
package com.mack.docscan.ui.pdfViewer

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.mack.docscan.R
import com.mack.docscan.databinding.ActivityPdfviewerActiviyBinding

class PDFViewerActivity : AppCompatActivity() {
    private val _binding : ActivityPdfviewerActiviyBinding by lazy {
        ActivityPdfviewerActiviyBinding.inflate(layoutInflater)
    }
    private var pdfUri : Uri? = null
    private lateinit var navController : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(_binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.pdf_nav_control) as?
                NavHostFragment
        navHostFragment?.let{
            navController = it.findNavController()
        }
        pdfUri = intent?.data
        if(pdfUri != null){
            val pdfViewer = PDFViewer()
            pdfViewer.arguments = Bundle().apply {
                putString("pdf_uri",pdfUri.toString())
                loadFragment(pdfViewer)
            }
        }
    }
    private fun loadFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction()
            .replace(R.id.pdf_nav_control, fragment)
            .commit()

    }

}
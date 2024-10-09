package com.mack.docscan.ui.mainScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mack.docscan.Adapter.DocumentDetailAdapter
import com.mack.docscan.R
import com.mack.docscan.database.pagedatabase.PageDatabase
import com.mack.docscan.databinding.FragmentDocumentDetailBinding
import kotlinx.coroutines.launch

class DocumentDetailFragment : Fragment() {

    private var binding : FragmentDocumentDetailBinding? = null
    private val args: DocumentDetailFragmentArgs by navArgs()
    private lateinit var pagerecyclerview : RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDocumentDetailBinding.inflate(layoutInflater,container,false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagerecyclerview = binding?.documentDetailRv!!
        pagerecyclerview.layoutManager = LinearLayoutManager(requireContext())
        val documentId = args.documentId
        lifecycleScope.launch {
            val pageDao = PageDatabase.getInstance(requireContext()).pageDao()
            pageDao.getPagesDocument(documentId).observe(viewLifecycleOwner){ pageList ->
                if(pageList != null){
                    val adapter = DocumentDetailAdapter(pageList)
                    pagerecyclerview.adapter = adapter
                }
            }
        }
    }

}
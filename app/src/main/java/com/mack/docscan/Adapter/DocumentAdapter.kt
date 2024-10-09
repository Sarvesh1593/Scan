package com.mack.docscan.Adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mack.docscan.R
import com.mack.docscan.database.documentdatabase.Document

class DocumentAdapter(
    private val documentList : List<Document>
) : RecyclerView.Adapter<DocumentAdapter.DocumentViewHolder>() {

    inner class DocumentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val documentNameTextView : TextView = itemView.findViewById(R.id.document_name)
        val pageCountTextView : TextView = itemView.findViewById(R.id.document_page_count)
        val documentImageView : ImageView = itemView.findViewById(R.id.document_image)

        init {
            itemView.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showdocumentlayout,parent,false)
        return DocumentViewHolder(view)
    }


    override fun getItemCount(): Int {
        return documentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: DocumentViewHolder, position: Int) {
        val document = documentList[position]

        holder.documentNameTextView.text = document.name

        holder.pageCountTextView.text = "Pages : ${document.pageCount}"

        Glide.with(holder.itemView.context)
            .load(document.imagePath)
            .into(holder.documentImageView)
    }
}
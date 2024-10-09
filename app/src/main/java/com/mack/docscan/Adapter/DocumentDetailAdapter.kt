package com.mack.docscan.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mack.docscan.R
import com.mack.docscan.database.pagedatabase.Page

class DocumentDetailAdapter(
    private val PagesList : List<Page>
) : RecyclerView.Adapter<DocumentDetailAdapter.DocumentDetailViewHolder>() {

    inner class DocumentDetailViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val pageNameTextView : TextView = itemView.findViewById(R.id.document_name)
        val documentImageView : ImageView = itemView.findViewById(R.id.document_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentDetailViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.showdocumentlayout,parent,false)

        return DocumentDetailViewHolder(view)
    }

    override fun getItemCount(): Int {
        return PagesList.size
    }

    override fun onBindViewHolder(holder: DocumentDetailViewHolder, position: Int) {
        val page = PagesList[position]
        holder.pageNameTextView.text = page.documentName

        Glide.with(holder.itemView.context)
            .load(page.path)
            .into(holder.documentImageView)
    }
}
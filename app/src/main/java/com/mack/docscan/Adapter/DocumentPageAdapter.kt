package com.mack.docscan.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mack.docscan.R

class DocumentPagerAdapter(private var images : List<Uri>): RecyclerView.Adapter<DocumentPagerAdapter.ImageViewHolder>() {
    class ImageViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        val imageView : ImageView = itemView.findViewById(R.id.IV_ImageShow)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val viewLayout = LayoutInflater.from(parent.context).inflate(R.layout.image_layout_show_image,parent,false)
        return ImageViewHolder(viewLayout)
    }

    override fun getItemCount(): Int {
         return images.size
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = images[position]
        Log.d("uriProblem",uri.toString())
        Glide.with(holder.imageView.context)
            .load(uri)
            .apply(RequestOptions().override(800, 600))
            .error(uri.toString())
            .into(holder.imageView)
    }
    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newImages : List<Uri>){
        images = newImages
        notifyDataSetChanged()
    }
    fun getImageUri(position: Int): Uri {
        return images[position]
    }
}
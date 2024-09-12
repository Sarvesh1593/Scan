package com.mack.docscan.ui.ui.onboard

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.mack.docscan.R

class onboardingAdapter(private val context : Context) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(context).inflate(R.layout.onboard_layout,container,false)

        val image_heading  = view.findViewById<ImageView>(R.id.onboard_iv)
        val heading = view.findViewById<TextView>(R.id.onboard_heading_tv)
        val sub_heading = view.findViewById<TextView>(R.id.onboard_subheading_tv)

        when(position){
            0->{
                image_heading.setImageResource(R.drawable.icons)
                heading.setText(R.string.welcome_to_the_docscan)
                sub_heading.setText(R.string.quickly_scan_sign_and_share_document_with_any_format)
            }
            1->{
                image_heading.setImageResource(R.drawable.icons_1)
                heading.setText(R.string.scan_and_edit_anything)
                sub_heading.setText(R.string.easly_scan_any_documents_to_pdf_jpg_or_txt_edit_on_the_go)
            }
            2->{
                image_heading.setImageResource(R.drawable.ocr)
                heading.setText(R.string.best_signing_and_text_recognition_ocr)
                sub_heading.setText(R.string.convert_picture_to_text_immediately_easily_edit_text_and_share)
            }
        }

        container.addView(view)
        return view
    }
    override fun getCount(): Int {
        return 3
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return  view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
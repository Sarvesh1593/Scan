package com.mack.docscan.ocr

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.mack.docscan.R

class TextViewOCR @JvmOverloads constructor(
    context : Context,
    attr: AttributeSet? = null,
    defStyleAttr : Int =0
) : AppCompatTextView(context,attr,defStyleAttr){
    init{
        setTextIsSelectable(true)
        customSelectionActionModeCallback = object : android.view.ActionMode.Callback2() {
            override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                mode?.menuInflater?.inflate(R.menu.menu,menu)
                return true
            }

            override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
                when(item?.itemId){
                    R.id.Copy ->{
                        copyText()
                        return true
                    }
                    R.id.share ->{
                        shareText()
                        return true
                    }
                }
                return false
            }
            override fun onDestroyActionMode(mode: ActionMode?) {}

        }
    }

    private fun shareText() {

        val selectedText = text.subSequence(selectionStart,selectionEnd).toString()

        if(selectedText.isNotEmpty()){
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,selectedText)
                type = "text/plain"
            }
            context.startActivity(Intent.createChooser(shareIntent,"Share via"))
        }
    }

    private fun copyText() {
        val clipboard = ContextCompat.getSystemService(context,ClipboardManager::class.java)
        val clip = ClipData.newPlainText("Copied Text",text.subSequence(selectionStart,selectionEnd))
        clipboard?.setPrimaryClip(clip)
    }
}
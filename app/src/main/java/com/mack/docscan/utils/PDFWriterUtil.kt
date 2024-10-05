package com.mack.docscan.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.widget.Toast
import com.mack.docscan.utils.ImageUtils.loadImageFromUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.jvm.Throws


class PDFWriterUtil {

    private val document = PdfDocument()

    fun addFile(bitmapFile : File){
        val options = BitmapFactory.Options().apply {
            inPreferredConfig = Bitmap.Config.ARGB_8888
        }
        val bitmapSt = BitmapFactory.decodeFile(bitmapFile.absolutePath,options)

        val pageInfo = PdfDocument.PageInfo.Builder(bitmapSt.width,bitmapSt.height,1).create()

        val page = document.startPage(pageInfo)

        val canvas : Canvas = page.canvas
        val paint = Paint().apply {
            color = Color.parseColor("#FFFFFF")
        }
        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmapSt,0f,0f, null)
        document.finishPage(page)
    }

    fun addBitmap(bitmap: Bitmap){
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width,bitmap.height,1).create()
        val page = document.startPage(pageInfo)

        val canvas : Canvas = page.canvas
        val paint = Paint().apply {
            color = Color.parseColor("#FFFFFF")
        }
        canvas.drawPaint(paint)
        canvas.drawBitmap(bitmap,0f,0f,null)
        document.finishPage(page)
    }
    @Throws(IOException::class)
    fun write(out: FileOutputStream){
        document.writeTo(out)
    }
    @Throws(IOException::class)
    fun getPageCount() : Int{
        return document.pages.size
    }
    fun generatePdfFromImages(context: Context, imageUriList: List<Uri>) {
        // Initialize PDF writer utility
        val pdfWriter = PDFWriterUtil()

        try {
            // Loop through each image URI
            for (uri in imageUriList) {
                val bitmap = loadImageFromUri(context, uri)
                bitmap.let {
                    // Add bitmap to PDF
                    pdfWriter.addBitmap(it)
                }
            }

            // Save the generated PDF file
            val directoryPath = context.getExternalFilesDir(null)?.path + "/PDFs"
            val pdfFile = File(directoryPath, "scanned_images.pdf")

            if (!pdfFile.parentFile.exists()) {
                pdfFile.parentFile.mkdirs()
            }

            val outputStream = FileOutputStream(pdfFile)
            pdfWriter.write(outputStream)

            Toast.makeText(context, "PDF created: ${pdfFile.absolutePath}", Toast.LENGTH_LONG).show()

            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error while generating PDF: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
        } finally {
            pdfWriter.close()
        }
    }
    private fun close(){
        document.close()
    }


}
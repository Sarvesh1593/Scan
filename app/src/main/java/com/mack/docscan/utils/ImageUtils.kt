package com.mack.docscan.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ImageUtils {
    private const val TAG = "ImageUtils"

    // Function to load image from URI
    fun loadImageFromUri(context: Context, uri: Uri?): Bitmap {

            val inputStream = uri?.let { context.contentResolver.openInputStream(it) }
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Log the bitmap dimensions
            Log.d(TAG, "Bitmap loaded with dimensions: ${bitmap?.width}x${bitmap?.height}")
            bitmap
        return  bitmap
    }

    // Function to convert bitmap to URI
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        // Create an empty file in the cache directory
        val file = File(context.cacheDir, "image_${System.currentTimeMillis()}.png")
        return try {
            // Write the bitmap to the file
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.flush()
            stream.close()

            // Return the Uri for the file
            Uri.fromFile(file)
        } catch (e: IOException) {
            Log.e(TAG, "Error saving bitmap to file", e)
            null
        }
    }

}

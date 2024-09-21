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
    fun loadImageFromUri(context: Context, uri: Uri?): Bitmap? {
        if (uri == null) {
            Log.e(TAG, "URI is null")
            return null
        }

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            inputStream?.close()

            // Log the bitmap dimensions
            Log.d(TAG, "Bitmap loaded with dimensions: ${bitmap?.width}x${bitmap?.height}")
            bitmap
        } catch (e: Exception) {
            Log.e(TAG, "Error loading image from URI", e)
            null
        }
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

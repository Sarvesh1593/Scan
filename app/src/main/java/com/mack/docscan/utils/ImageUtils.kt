package com.mack.docscan.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.util.Log
import androidx.exifinterface.media.ExifInterface
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
        return bitmap
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

    fun correctImageRotation(context: Context, bitmap: Bitmap, uri: Uri): Bitmap {
        val inputStream = context.contentResolver.openInputStream(uri)
        val exifInterface = inputStream?.let { ExifInterface(it) }
        val orientation = exifInterface?.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )

        val rotationDegrees = when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> 90
            ExifInterface.ORIENTATION_ROTATE_180 -> 180
            ExifInterface.ORIENTATION_ROTATE_270 -> 270
            else -> 0
        }

        return if (rotationDegrees != 0) {
            val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
            Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
        } else {
            bitmap
        }
    }
}

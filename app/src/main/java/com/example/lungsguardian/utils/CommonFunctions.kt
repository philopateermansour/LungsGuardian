package com.example.lungsguardian.utils

import android.content.Context
import android.content.ContextWrapper
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CommonFunctions {
    fun uriToFile(context: Context, uri: Uri): File? {
        var inputStream: InputStream? = null
        var outputStream: FileOutputStream? = null
        var file: File? = null

        try {
            inputStream = context.contentResolver.openInputStream(uri)
            val extension = context.contentResolver.getType(uri)?.substringAfter("/")
            val fileName = "temp_file.${extension ?: "jpg"}"
            file = File(context.cacheDir, fileName)
            outputStream = FileOutputStream(file)

            inputStream?.let {
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (it.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }

            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            inputStream?.close()
            outputStream?.close()
        }

        return null
    }
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(context)
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${System.currentTimeMillis()}.jpg")

        try {
            val stream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }

        return Uri.parse(file.absolutePath)
    }
    /*private fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        var inputStream: InputStream? = null
        try {
            // Open an input stream from the URI
            inputStream = context.contentResolver.openInputStream(uri)

            // Decode the input stream into a Bitmap
            return BitmapFactory.decodeStream(inputStream)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } finally {
            try {
                // Close the input stream
                inputStream?.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return null
    }*/

    fun bitmapToFile(context: Context, bitmap: Bitmap): File? {
        // Get the directory where the file will be saved
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "my_images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Create a file object with a unique name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_${timeStamp}.jpg"
        val file = File(directory, fileName)

        // Write the bitmap data to the file
        var fos: FileOutputStream? = null
        try {
            fos = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
            fos.flush()
            return file
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            fos?.close()
        }

        return null
    }
}
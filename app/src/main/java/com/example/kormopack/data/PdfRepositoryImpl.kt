package com.example.kormopack.data

import android.content.Context
import com.example.kormopack.domain.instructionfrag.PdfRepository
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class PdfRepositoryImpl(private val context: Context) : PdfRepository {
    override fun getPdfFileFromAssets(fileName: String): File {
        val file = File(context.cacheDir, fileName)
        try {
            context.assets.open(fileName).use { inputStream ->
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return file
    }
}
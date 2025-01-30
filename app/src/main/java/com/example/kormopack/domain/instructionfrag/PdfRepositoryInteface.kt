package com.example.kormopack.domain.instructionfrag

import java.io.File

interface PdfRepository {
    fun getPdfFileFromAssets(fileName: String): File
}
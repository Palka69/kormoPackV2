package com.example.kormopack.domain.instructionfrag

import java.io.File

class LoadPdfUseCase(private val pdfRepository: PdfRepository) {
    fun execute(fileName: String): File {
        return pdfRepository.getPdfFileFromAssets(fileName)
    }
}